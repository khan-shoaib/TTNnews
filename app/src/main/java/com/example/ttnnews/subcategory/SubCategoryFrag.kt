package com.example.ttnnews.subcategory

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ttnnews.R
import com.example.ttnnews.constants.Constant
import com.example.ttnnews.databse.AppRoomDatabase
import com.example.ttnnews.databse.NewModelRoom
import com.example.ttnnews.databse.RoomDatabaseBuilder
import com.example.ttnnews.model.NewsModel
import com.example.ttnnews.viewmodel.SubViewModel
import com.example.ttnnews.webview.WebViewFrag
import java.util.concurrent.Executors

class SubCategoryFrag : Fragment() {

    companion object {
        fun newInstance() = SubCategoryFrag()
    }

    private lateinit var viewModel: SubViewModel

    private lateinit var roomDatabaseBuilder: AppRoomDatabase
    private lateinit var rcNews: RecyclerView
    private lateinit var imgNointernet: ImageView
    private lateinit var tvNointernet: TextView
    private lateinit var edSearch: EditText
    var dataList = ArrayList<NewsModel>()

    private lateinit var customAdapter: SubCategoryAdapter
    lateinit var bar: ProgressBar

    lateinit var sourceName: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.activity_subcategory, container, false)
        findviews(view)
    return  view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel =  ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
            .create(SubViewModel(activity!!.application)::class.java)


        if(arguments!!.getString(Constant.SOURCENAME)!!.isNotEmpty()){
            sourceName = arguments!!.getString(Constant.SOURCENAME)!!
        }

        if (!Constant.isOnline(activity!!.application))  //internet check
        {
            tvNointernet.visibility = View.VISIBLE
            imgNointernet.visibility = View.VISIBLE
            return
        }

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
            .create(viewModel::class.java)


        roomDatabaseBuilder = RoomDatabaseBuilder.getInstance(activity!!.application)
        customAdapter = SubCategoryAdapter(activity!!.application, dataList) { newsdata: NewsModel, type: Int ->

            if (type == 0) {  // title clicked
//                val intent = Intent(activity!!.application, WebViewActivity::class.java)
//                intent.putExtra(Constant.URL, newsdata.url)
//                startActivity(intent)
                val frag = WebViewFrag.newInstance(newsdata.url)
                activity!!.supportFragmentManager.beginTransaction().replace(R.id.container,frag).addToBackStack(null).commit()


            } else {   // favourite clicked
                if (newsdata.isFav) {
                    Executors.newSingleThreadExecutor().execute {
                        roomDatabaseBuilder.newsDao().deleteData(
                            NewModelRoom(
                                title = newsdata.title,
                                url = newsdata.url,
                                image = newsdata.image,
                                desc = newsdata.description,
                                isFav = false
                            )
                        )
                    }
                    // remove from table

                    newsdata.isFav = false
                } else {
                    Executors.newSingleThreadExecutor().execute {
                        roomDatabaseBuilder.newsDao().insertData(
                            NewModelRoom(
                                title = newsdata.title,
                                url = newsdata.url,
                                image = newsdata.image,
                                desc = newsdata.description,
                                isFav = true
                            )
                        )
                    }
                    // inert val first time
                    newsdata.isFav = true
                }
                customAdapter.notifyDataSetChanged()
            }
        }

        rcNews.adapter = customAdapter
        val linearLayoutManager = LinearLayoutManager(activity!!.application, LinearLayoutManager.VERTICAL, false)
        rcNews.layoutManager = linearLayoutManager
        edSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
            {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
            {


                if (!Constant.isOnline(activity!!.application))
                {
                    tvNointernet.visibility = View.VISIBLE
                    imgNointernet.visibility = View.VISIBLE
                    rcNews.visibility = View.GONE
                    return
                } else {
                    rcNews.visibility = View.VISIBLE
                    tvNointernet.visibility = View.GONE
                    imgNointernet.visibility = View.GONE
                }

                if (s.isNullOrEmpty()) {
                    viewModel.getMutableLiveData(Constant.API_KEY, sourceName)
                        .observe(activity!!, {
                            if (it.isNotEmpty()) {
                                if (dataList.size > 0)
                                    dataList.clear()
                                dataList.addAll(it)
                                customAdapter.notifyDataSetChanged()
                                checkAlreadySelected()
                            } else
                                Toast.makeText(
                                    activity!!.application,
                                    "No Item found",
                                    Toast.LENGTH_SHORT
                                ).show()
                        })


                } else {
                    viewModel.getMutableLiveDataSearch(
                        Constant.API_KEY,
                        sourceName,
                        s.toString()
                    )
                        .observe(activity!!, {
                            if (it.isNotEmpty()) {
                                if (dataList.size > 0)
                                    dataList.clear()
                                dataList.addAll(it)
                                customAdapter.notifyDataSetChanged()
                                checkAlreadySelected()
                            } else
                                Toast.makeText(
                                    activity!!.application,
                                    "No Item found",
                                    Toast.LENGTH_SHORT
                                ).show()
                        })
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        bar.visibility = View.VISIBLE
        viewModel.getMutableLiveData(Constant.API_KEY, sourceName).observe(this, {
            bar.visibility = View.GONE

            if (it.isNotEmpty()) {
                if (dataList.size > 0)
                    dataList.clear()
                dataList.addAll(it)
                customAdapter.notifyDataSetChanged()
                checkAlreadySelected()  // check if any already favorite
            } else
                Toast.makeText(activity!!.application, "No Item found", Toast.LENGTH_SHORT).show()
        })
        // on failure toast message
        viewModel.getErrorLiveData().observe(this, {
            if (it.isNotEmpty())
                Toast.makeText(activity!!.application, "Error from APi-->$it", Toast.LENGTH_SHORT).show()
        })
        viewModel.getErrorLiveDataCode().observe(this, {
            if (it.isNotEmpty())
                Toast.makeText(activity!!.application, "Error from Server--->$it", Toast.LENGTH_SHORT).show()
        })




    }
    private fun findviews(view: View) {
        bar = view.findViewById(R.id.prbar)
        rcNews = view.findViewById(R.id.recyclerView)
        edSearch = view.findViewById(R.id.ed_search)
        tvNointernet = view.findViewById(R.id.tvnointernet)
        imgNointernet = view.findViewById(R.id.imgnointernetnet)
    }


    private fun checkAlreadySelected() {
        viewModel.getRoomData().observe(this, {
            if (it.isNotEmpty()) {  // set data to true if any marked fav
                for (item in dataList) {
                    for (data in it) {
                        if (item.title == data.title) {
                            item.isFav = true
                        }
                    }
                }
                customAdapter.notifyDataSetChanged()
            }

        })
    }

}
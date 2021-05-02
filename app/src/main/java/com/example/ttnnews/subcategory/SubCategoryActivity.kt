package com.example.ttnnews.subcategory

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ttnnews.Constants.Constant
import com.example.ttnnews.R
import com.example.ttnnews.databse.AppRoomDatabase
import com.example.ttnnews.databse.NewModelRoom
import com.example.ttnnews.databse.RoomDatabaseBuilder
import com.example.ttnnews.model.NewsModel
import com.example.ttnnews.viewmodel.MyViewModel
import com.example.ttnnews.webview.WebViewActivity
import kotlinx.android.synthetic.main.fav_view.*
import java.util.concurrent.Executors

class SubCategoryActivity : AppCompatActivity() {

    private lateinit var roomDatabaseBuilder: AppRoomDatabase
    private var rcNews: RecyclerView? = null
    private var imgNointernet: ImageView? = null
    private var tvNointernet: TextView? = null
    private var edSearch: EditText? = null
    var dataList = ArrayList<NewsModel>()

    private lateinit var customAdapter: SubCategoryAdapter
    lateinit var bar: ProgressBar

    lateinit var sourceName: String
    private lateinit var myViewModel: MyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activitytwo_main)
        findviews()
        if (!Constant.isOnline(applicationContext)) {
            tvNointernet!!.visibility = View.VISIBLE
            imgNointernet!!.visibility = View.VISIBLE
            return
        }



        myViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(MyViewModel::class.java)

        if (intent.hasExtra(Constant.SOURCENAME)) {
            sourceName = intent.getStringExtra(Constant.SOURCENAME).toString()
            Log.i(Constant.SOURCENAME, sourceName)
        }

        roomDatabaseBuilder = RoomDatabaseBuilder.getInstance(this)
        customAdapter = SubCategoryAdapter(this, dataList) { newsdata: NewsModel, type: Int ->

            if (type == 0) {
                val intent = Intent(this@SubCategoryActivity, WebViewActivity::class.java)
                intent.putExtra(Constant.URL, newsdata.url)
                startActivity(intent)

            } else {
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
                runOnUiThread {
                    customAdapter.notifyDataSetChanged()
                }
            }
        }

        rcNews!!.adapter = customAdapter
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rcNews!!.layoutManager = linearLayoutManager
        edSearch!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


                if (!Constant.isOnline(applicationContext)) {
                    tvNointernet!!.visibility = View.VISIBLE
                    imgNointernet!!.visibility = View.VISIBLE
                    rcNews!!.visibility = View.GONE
                    return
                } else {
                    rcNews!!.visibility = View.VISIBLE
                    tvNointernet!!.visibility = View.GONE
                    imgNointernet!!.visibility = View.GONE
                }

                if (s.isNullOrEmpty()) {
                    myViewModel.getMutableLiveData(Constant.API_KEY, sourceName)
                        .observe(this@SubCategoryActivity, {
                            if (it.isNotEmpty()) {
                                if (dataList.size > 0)
                                    dataList.clear()
                                dataList.addAll(it)
                                customAdapter.notifyDataSetChanged()
                                checkAlreadySelected()
                            } else
                                Toast.makeText(
                                    applicationContext,
                                    "No Item found",
                                    Toast.LENGTH_SHORT
                                ).show()
                        })


                } else {
                    myViewModel.getMutableLiveDataSearch(Constant.API_KEY, sourceName, s.toString())
                        .observe(this@SubCategoryActivity, {
                            if (it.isNotEmpty()) {
                                if (dataList.size > 0)
                                    dataList.clear()
                                dataList.addAll(it)
                                customAdapter.notifyDataSetChanged()
                                checkAlreadySelected()
                            } else
                                Toast.makeText(
                                    applicationContext,
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
        myViewModel.getMutableLiveData(Constant.API_KEY, sourceName).observe(this, {
            bar.visibility = View.GONE

            if (it.isNotEmpty()) {
                if (dataList.size > 0)
                    dataList.clear()
                dataList.addAll(it)
                customAdapter.notifyDataSetChanged()
                checkAlreadySelected()
            } else
                Toast.makeText(applicationContext, "No Item found", Toast.LENGTH_SHORT).show()
        })
        myViewModel.getErrorLiveData().observe(this, {
            if (it.isNotEmpty())
                Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_SHORT).show()
        })


    }

    private fun findviews() {
        bar = findViewById(R.id.prbar)
        rcNews = findViewById(R.id.recyclerView)
        edSearch = findViewById(R.id.ed_search)
        tvNointernet = findViewById(R.id.tvnointernet)
        imgNointernet = findViewById(R.id.imgnointernetnet)
    }


    private fun checkAlreadySelected() {
        myViewModel.getRoomData().observe(this, {
            if (it.isNotEmpty()) {  // set data to true if any marked fav
                for (item in dataList) {
                    for (data in it) {
                        if (item.title == data.title) {
                            item.isFav = true
                        }
                    }
                }
                runOnUiThread {
                    customAdapter.notifyDataSetChanged()
                }
            }

        })
    }

}
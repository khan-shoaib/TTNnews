package com.example.ttnnews.fav

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ttnnews.R
import com.example.ttnnews.databse.NewModelRoom
import com.example.ttnnews.databse.RoomDatabaseBuilder
import com.example.ttnnews.viewmodel.FavViewModel
import com.example.ttnnews.webview.WebViewActivity
import com.example.ttnnews.webview.WebViewFrag
import java.util.concurrent.Executors

class FragFav : Fragment() {

    lateinit var rc_fav: RecyclerView
    lateinit var tvNodata: TextView
    lateinit var imgnodata: ImageView
    lateinit var favAdapter: FavAdapter
    private lateinit var favViewModel: FavViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view   =  inflater.inflate(R.layout.fav_view, container, false)
        findview(view)
        return view;
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
            .create(FavViewModel::class.java)

        rc_fav.layoutManager = LinearLayoutManager(activity!!.applicationContext, RecyclerView.VERTICAL, false)
        val roomDatabaseBuilder = RoomDatabaseBuilder.getInstance(activity!!.applicationContext)
        favViewModel.getRoomData().observe(this, {
            if (it.isEmpty()) {
                tvNodata.visibility = View.VISIBLE
                imgnodata.visibility = View.VISIBLE
                return@observe
            }
            favAdapter = FavAdapter(activity!!.applicationContext, list = it)
            rc_fav.adapter = favAdapter
            favAdapter.onItemClick = { newsdata ->
                if (newsdata.isFav!!) {
                    Executors.newSingleThreadExecutor().execute {

                        roomDatabaseBuilder.newsDao().deleteData(
                            NewModelRoom(
                                title = newsdata.title,
                                url = newsdata.url,
                                image = newsdata.image,
                                desc = newsdata.desc,
                                isFav = false
                            )
                        )
                    }// remove from table

                    newsdata.isFav = false
                } else {
                    Executors.newSingleThreadExecutor().execute {

                        roomDatabaseBuilder.newsDao().insertData(
                            NewModelRoom(
                                title = newsdata.title,
                                url = newsdata.url,
                                image = newsdata.image,
                                desc = newsdata.desc,
                                isFav = true
                            )
                        )
                    }// inert val first time

                    newsdata.isFav = true
                }
                favAdapter.notifyDataSetChanged()
            }
            favAdapter.onTitleClick = { newsModel ->
//                val intent = Intent(this, WebViewActivity::class.java)
//                intent.putExtra("url", newsModel.url)
//                startActivity(intent)
                val frag = WebViewFrag.newInstance(newsModel.url!!)
                activity!!.supportFragmentManager.beginTransaction().replace(R.id.container,frag).addToBackStack(null).commit()



            }
        })

    }

    private fun findview(view: View) {
        rc_fav = view.findViewById(R.id.rc_fav)
        tvNodata = view.findViewById(R.id.tvNodata)
        imgnodata = view.findViewById(R.id.imgnodata)
    }

    companion object {
        fun newInstance() = FragFav()
    }
}
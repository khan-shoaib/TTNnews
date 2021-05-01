package com.example.ttnnews.fav

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ttnnews.databse.RoomDatabaseBuilder
import com.example.ttnnews.R
import com.example.ttnnews.databse.NewModelRoom
import com.example.ttnnews.viewmodel.FavViewModel
import com.example.ttnnews.viewmodel.MyViewModel
import com.example.ttnnews.webview.WebViewActivity
import java.util.concurrent.Executors

class FavActivity : AppCompatActivity() {
    var rc_fav: RecyclerView? = null
    var favAdapter: FavAdapter?=null
    private lateinit var favViewModel : FavViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fav_view)
        favViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(FavViewModel::class.java)
        rc_fav = findViewById(R.id.rc_fav)
        rc_fav!!.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val roomDatabaseBuilder = RoomDatabaseBuilder.getInstance(this)
        favViewModel.getRoomData().observe(this,{

            favAdapter = FavAdapter(this,list = it)
            rc_fav!!.adapter = favAdapter
            favAdapter!!.onItemClick = { newsdata ->
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
                runOnUiThread {
                    favAdapter!!.notifyDataSetChanged()
                }
            }
            favAdapter!!.onTitleClick = { newsModel ->
                val intent = Intent(this, WebViewActivity::class.java)
                intent.putExtra("url", newsModel.url)
                startActivity(intent)
            }
        })
    }
}
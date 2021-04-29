package com.example.ttnnews.fav

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidbootcamp2021.roomdemo.RoomDatabaseBuilder
import com.example.fav.FavAdapter
import com.example.ttnnews.R
import com.example.ttnnews.databse.NewModelRoom
import com.example.ttnnews.webview.WebViewActivity
import java.util.concurrent.Executors

class FavActivity : AppCompatActivity() {
    var rc_fav: RecyclerView? = null
    var favAdapter:FavAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fav_view)
        rc_fav = findViewById<RecyclerView>(R.id.rc_fav)
        rc_fav!!.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val roomDatabaseBuilder = RoomDatabaseBuilder.getInstance(this)
        Executors.newSingleThreadExecutor().execute {

            val listData = roomDatabaseBuilder.newsDao().getAllData()



            if(listData.size>0)
            runOnUiThread {
                favAdapter = FavAdapter(this,list = listData)
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
                        }// remove from tabale

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
            }
        }

    }
}
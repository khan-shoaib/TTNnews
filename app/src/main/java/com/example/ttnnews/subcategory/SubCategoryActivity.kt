package com.example.ttnnews.subcategory

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidbootcamp2021.roomdemo.AppRoomDatabase
import com.example.androidbootcamp2021.roomdemo.RoomDatabaseBuilder
import com.example.finalapp.model.DataModel
import com.example.finalapp.model.NewsModel
import com.example.finalapp.retrofit.ApiClient
import com.example.ttnnews.R
import com.example.ttnnews.databse.NewModelRoom
import com.example.ttnnews.webview.WebViewActivity
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

class SubCategoryActivity : AppCompatActivity() {

    private lateinit var roomDatabaseBuilder: AppRoomDatabase
    private var recyclerView: RecyclerView? = null
    private var edSearch: EditText? = null
    var dataList = ArrayList<NewsModel>()
    private val KEY = "8f4f5d6c37ee3540afb179e31b506364"
    private lateinit var customAdapter: SubCategoryAdapter

    var sourceName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activitytwo_main)

        if (intent.hasExtra("sourcename")) {
            sourceName = intent.getStringExtra("sourcename")
            Log.i("SorceName", sourceName!!)
        }
        recyclerView = findViewById(R.id.recyclerView)
        edSearch = findViewById(R.id.ed_search)
        roomDatabaseBuilder = RoomDatabaseBuilder.getInstance(this)
        customAdapter = SubCategoryAdapter(this, dataList)
        recyclerView!!.adapter = customAdapter
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.layoutManager = linearLayoutManager
        edSearch!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                val call = ApiClient.getClient.getNewsSearch(KEY,"en",sourceName,s.toString());
//                call.enqueue(object : Callback<DataModel>{
//                    override fun onResponse(
//                        call: retrofit2.Call<DataModel>,
//                        response: Response<DataModel>
//                    ) {
//                        dataList.addAll(response.body()?.data?:ArrayList())
//                        Log.i("MainActivity", "data is: ${dataList}\n\n")
//                        customAdapter.notifyDataSetChanged()
//                    }
//
//                    override fun onFailure(call: retrofit2.Call<DataModel>, t: Throwable) {
//                        Log.i("Failure","${call}")
//                        Toast.makeText(applicationContext,
//                            "there is some error while getting data",
//                            Toast.LENGTH_SHORT).show()
//                    }
//
//
//                })


            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        getData()
    }

    private fun getData() {
        val call = ApiClient.getClient.getNews(KEY, "en", sourceName)
        call.enqueue(object : Callback<DataModel> {
            override fun onResponse(
                call: retrofit2.Call<DataModel>,
                response: Response<DataModel>
            ) {
                dataList.addAll(response.body()?.data ?: ArrayList())
                Log.i("MainActivity", "data is: ${dataList}\n\n")
                customAdapter.notifyDataSetChanged()
                setupUI()

            }

            override fun onFailure(call: retrofit2.Call<DataModel>, t: Throwable) {
                Log.i("Failure", "$call")
                Toast.makeText(this@SubCategoryActivity,"there is some error while getting data",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupUI() {

        Executors.newSingleThreadExecutor().execute {

            val _databaseList = roomDatabaseBuilder.newsDao().getAllData()

            if (_databaseList.size > 0) {  // set data to true if any marked fav
                for (item in dataList) {
                    for (data in _databaseList) {
                        if (item.title == data.title) {
                            item.isFav = true
                        }
                    }
                }
            }
            runOnUiThread {
                customAdapter.notifyDataSetChanged()
            }

            customAdapter.onItemClick = { newsdata ->
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
                      // remove from tabale

                    newsdata.isFav = false
                }
                else {
                    Executors.newSingleThreadExecutor().execute {
                        roomDatabaseBuilder.newsDao().insertData(NewModelRoom(
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

            customAdapter.onTitleClick = { newsModel ->
                val intent = Intent(this@SubCategoryActivity, WebViewActivity::class.java)
                intent.putExtra("url", newsModel.url)
                startActivity(intent)
            }
        }

    }

}
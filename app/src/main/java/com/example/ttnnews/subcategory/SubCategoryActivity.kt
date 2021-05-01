package com.example.ttnnews.subcategory

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ttnnews.viewmodel.MyViewModel
import com.example.ttnnews.R
import com.example.ttnnews.databse.AppRoomDatabase
import com.example.ttnnews.databse.NewModelRoom
import com.example.ttnnews.databse.RoomDatabaseBuilder
import com.example.ttnnews.model.NewsModel
import com.example.ttnnews.webview.WebViewActivity
import java.util.concurrent.Executors

class SubCategoryActivity : AppCompatActivity() {

    private lateinit var roomDatabaseBuilder: AppRoomDatabase
    private var recyclerView: RecyclerView? = null
    private var edSearch: EditText? = null
    var dataList = ArrayList<NewsModel>()
    private val KEY = "8f4f5d6c37ee3540afb179e31b506364"
    private lateinit var customAdapter: SubCategoryAdapter

    var sourceName: String? = null
   private lateinit var myViewModel : MyViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activitytwo_main)

          myViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(MyViewModel::class.java)

        if (intent.hasExtra("sourcename")) {
            sourceName = intent.getStringExtra("sourcename")
            Log.i("SorceName", sourceName!!)
        }
        recyclerView = findViewById(R.id.recyclerView)
        edSearch = findViewById(R.id.ed_search)
        roomDatabaseBuilder = RoomDatabaseBuilder.getInstance(this)
        customAdapter = SubCategoryAdapter(this, dataList){newsdata: NewsModel,type:Int ->

            if(type ==0)
            {
                val intent = Intent(this@SubCategoryActivity, WebViewActivity::class.java)
                    intent.putExtra("url", newsdata.url)
                    startActivity(intent)

            }else
            {
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

        recyclerView!!.adapter = customAdapter
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.layoutManager = linearLayoutManager
        edSearch!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(s.isNullOrEmpty())
                {
                    myViewModel.getMutableLiveData(KEY,sourceName!!).observe(this@SubCategoryActivity, {
                        if(it.size>0){
                            if(dataList.size>0)
                                dataList.clear()
                            dataList.addAll(it)
                            customAdapter.notifyDataSetChanged()
                            checkAlreadySelected()
                        }else
                            Toast.makeText(applicationContext,"No Item found",Toast.LENGTH_SHORT).show()
                    })


                }else
                {
                    myViewModel.getMutableLiveDataSearch(KEY,sourceName!!,s.toString()).observe(this@SubCategoryActivity, {
                        if(it.size>0){
                            if(dataList.size>0)
                                dataList.clear()
                            dataList.addAll(it)
                            customAdapter.notifyDataSetChanged()
                            checkAlreadySelected()
                        }else
                            Toast.makeText(applicationContext,"No Item found",Toast.LENGTH_SHORT).show()
                    })
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        myViewModel.getMutableLiveData(KEY,sourceName!!).observe(this, {
            if(it.size>0){
                if(dataList.size>0)
                    dataList.clear()
                dataList.addAll(it)
                customAdapter.notifyDataSetChanged()
                checkAlreadySelected()
            }else
                Toast.makeText(applicationContext,"No Item found",Toast.LENGTH_SHORT).show()
        })

        myViewModel.getErrorLiveData().observe(this, {
            if(it.isNotEmpty())
                Toast.makeText(applicationContext,it.toString(),Toast.LENGTH_SHORT).show()
        })



    }


    private fun checkAlreadySelected(){
        myViewModel.getRoomData().observe(this,{
            if (it.size > 0) {  // set data to true if any marked fav
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
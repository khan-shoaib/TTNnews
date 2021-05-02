package com.example.ttnnews.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.ttnnews.databse.AppRoomDatabase
import com.example.ttnnews.databse.NewModelRoom
import com.example.ttnnews.databse.RoomDatabaseBuilder
import com.example.ttnnews.model.DataModel
import com.example.ttnnews.model.NewsModel
import com.example.ttnnews.retrofit.ApiClient
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors


class SubCategoryViewModel(application: Application) : AndroidViewModel(application)  {
    private val mutableLiveDataNews: MutableLiveData<List<NewsModel>> = MutableLiveData<List<NewsModel>>()
    private val mutableLiveDataNewsSearch: MutableLiveData<List<NewsModel>> = MutableLiveData<List<NewsModel>>()
    private val mutableLiveDataNewsRoom: MutableLiveData<List<NewModelRoom>> = MutableLiveData<List<NewModelRoom>>()
    private val mutableLiveDataerror: MutableLiveData<String> = MutableLiveData<String>()
    private lateinit var roomDatabaseBuilder: AppRoomDatabase


    fun getMutableLiveData(key:String,sourcename:String): MutableLiveData<List<NewsModel>> {
        val call = ApiClient.getClient.getNews(key, "en", sourcename,100,country = "in")
        call.enqueue(object : Callback<DataModel> {
            override fun onResponse(
                call: retrofit2.Call<DataModel>,
                response: Response<DataModel>
            ) {

                mutableLiveDataNews.value = response.body()!!.data

            }

            override fun onFailure(call: retrofit2.Call<DataModel>, t: Throwable) {
                Log.i("Failure", "$call")

                mutableLiveDataerror.value = call.toString()
            }
        })
     return  mutableLiveDataNews
    }

    fun getMutableLiveDataSearch(key:String,sourcename:String,searchtext:String): MutableLiveData<List<NewsModel>> {
        val call = ApiClient.getClient.getNewsSearch(key, "en",sourcename, searchtext,100,country = "in")
        call.enqueue(object : Callback<DataModel> {
            override fun onResponse(
                call: retrofit2.Call<DataModel>,
                response: Response<DataModel>
            ) {mutableLiveDataNewsSearch.value = response.body()!!.data
            }

            override fun onFailure(call: retrofit2.Call<DataModel>, t: Throwable) {
                Log.i("Failure", "$call")

                mutableLiveDataerror.value = call.toString()
            }
        })
     return  mutableLiveDataNewsSearch
    }
    fun getErrorLiveData(): MutableLiveData<String>{
        return  mutableLiveDataerror
    }
    fun getRoomData(): MutableLiveData<List<NewModelRoom>>{
        roomDatabaseBuilder = RoomDatabaseBuilder.getInstance(getApplication<Application>().applicationContext)
        Executors.newSingleThreadExecutor().execute {
            val _databaseList = roomDatabaseBuilder.newsDao().getAllData()
            mutableLiveDataNewsRoom.postValue(_databaseList)
        }
        return  mutableLiveDataNewsRoom
    }
}


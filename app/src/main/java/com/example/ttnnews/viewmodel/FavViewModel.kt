package com.example.ttnnews.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ttnnews.databse.AppRoomDatabase
import com.example.ttnnews.databse.NewModelRoom
import com.example.ttnnews.databse.RoomDatabaseBuilder
import com.example.ttnnews.model.DataModel
import com.example.ttnnews.model.NewsModel
import com.example.ttnnews.retrofit.ApiClient
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors


class FavViewModel(application: Application) : AndroidViewModel(application)  {
    private val mutableLiveDataNewsRoom: MutableLiveData<List<NewModelRoom>> = MutableLiveData<List<NewModelRoom>>()
    private val mutableLiveDataerror: MutableLiveData<String> = MutableLiveData<String>()
    private lateinit var roomDatabaseBuilder: AppRoomDatabase


    // for future purpose
    fun getErrorLiveData(): MutableLiveData<String>{
        return  mutableLiveDataerror
    }
    fun getRoomData(): MutableLiveData<List<NewModelRoom>>{
        roomDatabaseBuilder = RoomDatabaseBuilder.getInstance(getApplication<Application>().applicationContext)
        Executors.newSingleThreadExecutor().execute {
            val _databaseList = roomDatabaseBuilder.newsDao().getAllData()
         mutableLiveDataNewsRoom.postValue(_databaseList);
        }


        return  mutableLiveDataNewsRoom
    }
}


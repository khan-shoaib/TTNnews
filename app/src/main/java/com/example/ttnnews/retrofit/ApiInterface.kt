package com.example.ttnnews.retrofit

import com.example.ttnnews.model.DataModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("news")
    fun getNews(
        @Query("access_key") key: String,
        @Query("languages") language: String,
        @Query("catgories") sourceName: String?,
        @Query("limit") limit:Int,
        @Query("countries") country:String
    ): Call<DataModel>

    @GET("news")
    fun getNewsSearch(
        @Query("access_key") key: String,
        @Query("languages") language: String,
        @Query("catgories") sourceName: String?,
        @Query("search") search: String?,
        @Query("limit") limit:Int,
        @Query("countries") country:String


    ): Call<DataModel>
}
package com.example.ttnnews.model

import com.google.gson.annotations.SerializedName

data class DataModel(
    @SerializedName("data")
    var data: ArrayList<NewsModel>
)
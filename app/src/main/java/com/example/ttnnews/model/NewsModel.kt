package com.example.finalapp.model

import com.google.gson.annotations.SerializedName

data class NewsModel(
    @SerializedName("title")
    var title: String,
    @SerializedName("url")
    var url: String,
    @SerializedName("image")
    var image: String,
    @SerializedName("description")
    var description: String,

    var isFav:Boolean = false
)
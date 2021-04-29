package com.example.ttnnews.databse

import androidx.room.Entity
import androidx.room.PrimaryKey

// This data class is being used for both
// SQLite and Androidx Room

@Entity(tableName = "news_table")
data class NewModelRoom(
    @PrimaryKey
    var title: String,
    var url: String? = null,
    var image: String? = null,
    var desc: String? = null,
    var isFav: Boolean? = false
)
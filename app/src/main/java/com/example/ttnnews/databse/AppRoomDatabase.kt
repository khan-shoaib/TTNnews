package com.example.androidbootcamp2021.roomdemo

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.finalapp.model.NewsModel
import com.example.ttnnews.databse.NewModelRoom

// Entity means a row
@Database(entities = arrayOf(NewModelRoom::class), version = 1)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun newsDao(): RoomNewsDoa
}

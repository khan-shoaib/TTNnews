package com.example.ttnnews.databse

import androidx.room.Database
import androidx.room.RoomDatabase

// Entity means a row
@Database(entities = [NewModelRoom::class], version = 2)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun newsDao(): RoomNewsDoa
}

package com.example.androidbootcamp2021.roomdemo

import androidx.room.*
import com.example.ttnnews.databse.NewModelRoom

@Dao
interface RoomNewsDoa {

    @Query("SELECT * FROM news_table")
    fun getAllData(): List<NewModelRoom>

    @Insert
    fun insertData(data: NewModelRoom)

    @Delete
    fun deleteData(data: NewModelRoom)
}

package com.example.ttnnews.databse

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RoomNewsDoa {

    @Query("SELECT * FROM news_table")
    fun getAllData(): List<NewModelRoom>

    @Insert
    fun insertData(data: NewModelRoom)

    @Delete
    fun deleteData(data: NewModelRoom)
}

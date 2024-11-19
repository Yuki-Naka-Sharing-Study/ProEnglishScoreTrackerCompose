package com.example.proenglishscoretracker

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EnglishInfoDao {
    @Insert
    suspend fun insertEnglishInfo(item: EnglishInfo)

    @Delete
    suspend fun deleteEnglishInfo(item: EnglishInfo)

    @Query("SELECT * FROM english_info")
    suspend fun getAllEnglishInfo(): List<EnglishInfo>
}

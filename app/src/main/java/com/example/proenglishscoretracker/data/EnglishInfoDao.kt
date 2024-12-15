package com.example.proenglishscoretracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.proenglishscoretracker.data.EnglishInfo

@Dao
interface EnglishInfoDao {
    @Insert
    suspend fun insertEnglishInfo(item: EnglishInfo)

    @Delete
    suspend fun deleteEnglishInfo(item: EnglishInfo)

    @Query("SELECT * FROM english_info")
    suspend fun getAllEnglishInfo(): List<EnglishInfo>
}

package com.example.proenglishscoretracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface EnglishInfoDao {
    // TOEIC
    @Insert
    suspend fun insertToeicInfo(item: EnglishTestInfo.TOEIC)
    @Delete
    suspend fun deleteToeicInfo(item: EnglishTestInfo.TOEIC)
    @Query("SELECT * FROM toeic")
    suspend fun getAllToeicInfo(): List<EnglishTestInfo.TOEIC>
    @Update
    suspend fun updateToeicInfo(item: EnglishTestInfo.TOEIC)

    // TOEIC SW
    @Insert
    suspend fun insertToeicSwInfo(item: EnglishTestInfo.TOEIC_SW)
    @Query("SELECT * FROM toeic_sw")
    suspend fun getAllToeicSwInfo(): List<EnglishTestInfo.TOEIC_SW>

    // EIKEN


    // TOEFL
    @Insert
    suspend fun insertToeflInfo(item: EnglishTestInfo.TOEFL)
    @Query("SELECT * FROM toefl")
    suspend fun getAllToeflInfo(): List<EnglishTestInfo.TOEFL>

}

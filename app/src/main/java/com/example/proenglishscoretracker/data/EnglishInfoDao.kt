package com.example.proenglishscoretracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface EnglishInfoDao {
    // TOEIC
    // 指定された年度の受験回数を取得
    @Query("SELECT COUNT(*) FROM toeic WHERE strftime('%Y', date) = :year")
    suspend fun countToeicEntriesByYear(year: String): Int

    // 指定された受験日と一致するデータの数を取得
    @Query("SELECT COUNT(*) FROM toeic WHERE date = :date")
    suspend fun countToeicEntriesByDate(date: String): Int
    @Insert
    suspend fun insertToeicInfo(item: EnglishTestInfo.TOEIC)
    @Delete
    suspend fun deleteToeicInfo(item: EnglishTestInfo.TOEIC)
    @Update
    suspend fun updateToeicInfo(item: EnglishTestInfo.TOEIC)
    @Query("SELECT * FROM toeic")
    suspend fun getAllToeicInfo(): List<EnglishTestInfo.TOEIC>
    @Query("SELECT * FROM toeic WHERE id = :toeicId")
    suspend fun getToeicInfoById(toeicId: String): EnglishTestInfo.TOEIC?


    // TOEIC SW
    @Insert
    suspend fun insertToeicSwInfo(item: EnglishTestInfo.TOEIC_SW)
    @Delete
    suspend fun deleteToeicSwInfo(item: EnglishTestInfo.TOEIC_SW)
    @Update
    suspend fun updateToeicSwInfo(item: EnglishTestInfo.TOEIC_SW)
    @Query("SELECT * FROM toeic_sw")
    suspend fun getAllToeicSwInfo(): List<EnglishTestInfo.TOEIC_SW>
    @Query("SELECT * FROM toeic_sw WHERE id = :toeicSwId")
    suspend fun getToeicSwInfoById(toeicSwId: String): EnglishTestInfo.TOEIC_SW?


    // EIKEN
    @Query("SELECT COUNT(*) FROM eiken_second WHERE grade = :grade AND strftime('%Y', date) = :year")
    suspend fun countEntriesByGradeAndYear(grade: String, year: String): Int
    @Insert
    suspend fun insertEikenInfo(item: EnglishTestInfo.EIKEN_SECOND)
    @Delete
    suspend fun deleteEikenInfo(item: EnglishTestInfo.EIKEN_SECOND)
    @Update
    suspend fun updateEikenInfo(item: EnglishTestInfo.EIKEN_SECOND)
    @Query("SELECT * FROM eiken_second")
    suspend fun getAllEikenInfo(): List<EnglishTestInfo.EIKEN_SECOND>
    @Query("SELECT * FROM eiken_second WHERE id = :eikenId")
    suspend fun getEikenInfoById(eikenId: String): EnglishTestInfo.EIKEN_SECOND?



    // TOEFL
    @Insert
    suspend fun insertToeflInfo(item: EnglishTestInfo.TOEFL)
    @Delete
    suspend fun deleteToeflIbtInfo(item: EnglishTestInfo.TOEFL)
    @Update
    suspend fun updateToeflIbtInfo(item: EnglishTestInfo.TOEFL)
    @Query("SELECT * FROM toefl")
    suspend fun getAllToeflIbtInfo(): List<EnglishTestInfo.TOEFL>
    @Query("SELECT * FROM toefl WHERE id = :toeflIbtId")
    suspend fun getToeflIbtInfoById(toeflIbtId: String): EnglishTestInfo.TOEFL?


}

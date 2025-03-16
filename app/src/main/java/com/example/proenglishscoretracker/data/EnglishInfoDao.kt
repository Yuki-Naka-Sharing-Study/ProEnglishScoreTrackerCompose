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
    // 指定された年度の受験回数を取得
    @Query("SELECT COUNT(*) FROM toeic_sw WHERE strftime('%Y', date) = :year")
    suspend fun countToeicSwEntriesByYear(year: String): Int

    // 指定された受験日と一致するデータの数を取得
    @Query("SELECT COUNT(*) FROM toeic_sw WHERE date = :date")
    suspend fun countToeicSwEntriesByDate(date: String): Int
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
    @Query("SELECT COUNT(*) FROM eiken WHERE date = :date AND grade = :grade")
    suspend fun countEikenEntriesByDateAndGrade(date: String, grade: String): Int
    @Query("SELECT COUNT(*) FROM eiken WHERE strftime('%Y-%m', date) = :yearMonth AND grade = :grade")
    suspend fun countEikenEntriesByYearMonthAndGrade(yearMonth: String, grade: String): Int
    @Query("SELECT COUNT(*) FROM eiken WHERE grade = :grade AND strftime('%Y', date) = :year")
    suspend fun countEntriesByGradeAndYear(grade: String, year: String): Int
    @Insert
    suspend fun insertEikenInfo(item: EnglishTestInfo.EIKEN)
    @Delete
    suspend fun deleteEikenInfo(item: EnglishTestInfo.EIKEN)
    @Update
    suspend fun updateEikenInfo(item: EnglishTestInfo.EIKEN)
    @Query("SELECT * FROM eiken")
    suspend fun getAllEikenInfo(): List<EnglishTestInfo.EIKEN>
    @Query("SELECT * FROM eiken WHERE id = :eikenId")
    suspend fun getEikenInfoById(eikenId: String): EnglishTestInfo.EIKEN?


    // TOEFL
    // 指定された受験日と一致するデータの数を取得
    @Query("SELECT COUNT(*) FROM toefl WHERE date = :date")
    suspend fun countToeflIbtEntriesByDate(date: String): Int
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


    // IELTS
    // 指定された受験日と一致するデータの数を取得
    @Query("SELECT COUNT(*) FROM ielts WHERE date = :date")
    suspend fun countIeltsEntriesByDate(date: String): Int
    @Insert
    suspend fun insertIeltsInfo(item: EnglishTestInfo.IELTS)
    @Delete
    suspend fun deleteIeltsInfo(item: EnglishTestInfo.IELTS)
    @Update
    suspend fun updateIeltsInfo(item: EnglishTestInfo.IELTS)
    @Query("SELECT * FROM ielts")
    suspend fun getAllIeltsInfo(): List<EnglishTestInfo.IELTS>
    @Query("SELECT * FROM ielts WHERE id = :ieltsId")
    suspend fun getIeltsInfoById(ieltsId: String): EnglishTestInfo.IELTS?
}

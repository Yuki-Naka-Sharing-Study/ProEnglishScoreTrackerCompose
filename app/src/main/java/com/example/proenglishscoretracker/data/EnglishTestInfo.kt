package com.example.proenglishscoretracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

sealed class EnglishTestInfo {
    @Entity(tableName = "toeic")
    data class TOEIC(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val date: String,
        val readingScore: Int,
        val listeningScore: Int,
        val memo: String
    ) : EnglishTestInfo()

    @Entity(tableName = "toeic_sw")
    data class TOEIC_SW(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val date: String,
        val writingScore: Int,
        val speakingScore: Int,
        val memo: String
    ) : EnglishTestInfo()

    @Entity(tableName = "eiken_first")
    data class EIKEN_FIRST(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val date: String,
        val grade: String,
        val cseScore: Int,
        val readingScore: Int,
        val listeningScore: Int,
        val writingScore: Int,
        val memo: String
    ) : EnglishTestInfo()

    @Entity(tableName = "eiken_second")
    data class EIKEN_SECOND(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val date: String,
        val grade: String,
        val readingScore: Int,
        val listeningScore: Int,
        val writingScore: Int,
        val speakingScore: Int,
        val cseScore: Int,
        val memo: String
    ) : EnglishTestInfo()

    @Entity(tableName = "toefl")
    data class TOEFL(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val date: String,
        val overallScore: Int,
        val readingScore: Int,
        val listeningScore: Int,
        val writingScore: Int,
        val speakingScore: Int,
        val memo: String
    ) : EnglishTestInfo()

    @Entity(tableName = "ielts")
    data class IELTS(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val date: String,
        val overallScore: Int,
        val readingScore: Int,
        val listeningScore: Int,
        val writingScore: Int,
        val speakingScore: Int,
        val memo: String
    ) : EnglishTestInfo()
}

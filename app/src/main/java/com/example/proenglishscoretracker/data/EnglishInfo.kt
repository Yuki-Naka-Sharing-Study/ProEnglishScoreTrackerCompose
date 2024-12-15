package com.example.proenglishscoretracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "english_info")
data class EnglishInfo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cseScore: Int,
    val readingScore: Int,
    val listeningScore: Int,
    val writingScore: Int,
    val speakingScore: Int,
    val memoText: String,
)

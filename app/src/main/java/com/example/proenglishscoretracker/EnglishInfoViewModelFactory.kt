package com.example.proenglishscoretracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EnglishInfoViewModelFactory(
    private val repository: EnglishInfoRepository,
    private val englishInfoDao: EnglishInfoDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EnglishInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EnglishInfoViewModel(repository, englishInfoDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

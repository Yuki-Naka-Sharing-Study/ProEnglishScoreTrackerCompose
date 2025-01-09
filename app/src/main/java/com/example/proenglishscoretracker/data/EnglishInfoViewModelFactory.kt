package com.example.proenglishscoretracker.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EnglishInfoViewModelFactory(
    private val repository: EnglishInfoRepository,
    private val englishInfoDao: EnglishInfoDao,
    private val dataStore: DataStore<Preferences>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EnglishInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EnglishInfoViewModel(repository, englishInfoDao, dataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

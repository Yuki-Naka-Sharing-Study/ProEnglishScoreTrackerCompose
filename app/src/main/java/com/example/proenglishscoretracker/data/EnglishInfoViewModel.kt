package com.example.proenglishscoretracker.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proenglishscoretracker.data.EnglishInfo
import com.example.proenglishscoretracker.data.EnglishInfoDao
import com.example.proenglishscoretracker.data.EnglishInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class EnglishInfoViewModel(
    private val repository: EnglishInfoRepository,
    private val englishInfoDao: EnglishInfoDao,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val _englishInfo = MutableStateFlow<List<EnglishInfo>>(emptyList())
    val englishInfo: StateFlow<List<EnglishInfo>> = _englishInfo

    private val ONBOARDING_COMPLETED_KEY = booleanPreferencesKey("onboarding_completed")

    // 初回起動かどうかを監視するFlow
    val isFirstLaunch: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[ONBOARDING_COMPLETED_KEY] ?: true
    }

    // Onboarding 完了時にフラグを保存
    fun completeOnboarding() {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[ONBOARDING_COMPLETED_KEY] = false
            }
        }
    }

    init {
        viewModelScope.launch {
            _englishInfo.value = englishInfoDao.getAllEnglishInfo()
        }
    }

    fun saveEikenIchijiValues(cseScore: Int,
                              readingScore: Int,
                              listeningScore: Int,
                              writingScore: Int,
                              memoText: String) {
        viewModelScope.launch {
            repository.saveEikenIchijiInfo(
                cseScore,
                readingScore,
                listeningScore,
                writingScore,
                memoText
            )
        }
    }

    fun saveEikenNijiValues(cseScore: Int,
                            speakingScore: Int,
                            shortSpeechScore: Int,
                            interactionScore: Int,
                            grammarAndVocabularyScore: Int,
                            pronunciationScore: Int,
                            memoText: String) {
        viewModelScope.launch {
            repository.saveEikenNijiInfo(
                cseScore,
                speakingScore,
                shortSpeechScore,
                interactionScore,
                grammarAndVocabularyScore,
                pronunciationScore,
                memoText
            )
        }
    }

    fun saveToeicValues(readingScore: Int,
                        listeningScore: Int,
                        memoText: String) {
        viewModelScope.launch {
            repository.saveToeicInfo(
                readingScore,
                listeningScore,
                memoText
            )
        }
    }

    fun saveToeicSwValues(writingScore: Int,
                          speakingScore: Int,
                          memoText: String) {
        viewModelScope.launch {
            repository.saveToeicSwInfo(
                writingScore,
                speakingScore,
                memoText
            )
        }
    }

    fun saveToeflIbtValues(overallScore: Int,
                           readingScore: Int,
                           listeningScore: Int,
                           writingScore: Int,
                           speakingScore: Int,
                           memoText: String) {
        viewModelScope.launch {
            repository.saveToeflIbtInfo(
                overallScore,
                readingScore,
                listeningScore,
                writingScore,
                speakingScore,
                memoText
            )
        }
    }

    fun saveIeltsValues(overallScore: Float,
                        readingScore: Float,
                        listeningScore: Float,
                        writingScore: Float,
                        speakingScore: Float,
                        memoText: String) {
        viewModelScope.launch {
            repository.saveIeltsInfo(
                overallScore,
                readingScore,
                listeningScore,
                writingScore,
                speakingScore,
                memoText
            )
        }
    }

    fun insertMusicInfo(musicInfo: EnglishInfo) {
        viewModelScope.launch {
            englishInfoDao.insertEnglishInfo(musicInfo)
            _englishInfo.value = englishInfoDao.getAllEnglishInfo()
        }
    }

    fun deleteMusicInfo(musicInfo: EnglishInfo) {
        viewModelScope.launch {
            englishInfoDao.deleteEnglishInfo(musicInfo)
            _englishInfo.value = englishInfoDao.getAllEnglishInfo()
        }
    }
}

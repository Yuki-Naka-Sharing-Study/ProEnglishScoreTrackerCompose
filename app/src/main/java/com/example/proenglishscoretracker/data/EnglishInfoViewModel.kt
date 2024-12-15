package com.example.proenglishscoretracker.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proenglishscoretracker.data.EnglishInfo
import com.example.proenglishscoretracker.data.EnglishInfoDao
import com.example.proenglishscoretracker.data.EnglishInfoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EnglishInfoViewModel(
    private val repository: EnglishInfoRepository,
    private val englishInfoDao: EnglishInfoDao
) : ViewModel() {

    private val _englishInfo = MutableStateFlow<List<EnglishInfo>>(emptyList())
    val englishInfo: StateFlow<List<EnglishInfo>> = _englishInfo

    init {
        viewModelScope.launch {
            _englishInfo.value = englishInfoDao.getAllEnglishInfo()
        }
    }

    fun saveEikenIchijiValues(cseScore: String,
                              readingScore: String,
                              listeningScore: String,
                              writingScore: String,
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

    fun saveEikenNijiValues(cseScore: String,
                            speakingScore: String,
                            shortSpeechScore: String,
                            interactionScore: String,
                            grammarAndVocabularyScore: String,
                            pronunciationScore: String,
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

    fun saveToeicSwValues(writingScore: String,
                          speakingScore: String,
                          memoText: String) {
        viewModelScope.launch {
            repository.saveToeicSwInfo(
                writingScore,
                speakingScore,
                memoText
            )
        }
    }

    fun saveToeflIbtValues(overallScore: String,
                           readingScore: String,
                           listeningScore: String,
                           writingScore: String,
                           speakingScore: String,
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

    fun saveIeltsValues(overallScore: String,
                        readingScore: String,
                        listeningScore: String,
                        writingScore: String,
                        speakingScore: String,
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

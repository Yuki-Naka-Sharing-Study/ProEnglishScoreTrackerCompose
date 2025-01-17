package com.example.proenglishscoretracker.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val _toeicInfo = MutableStateFlow<List<EnglishTestInfo.TOEIC>>(emptyList())
    val toeicInfo: StateFlow<List<EnglishTestInfo.TOEIC>> = _toeicInfo

    private val _toeicSwInfo = MutableStateFlow<List<EnglishTestInfo.TOEIC_SW>>(emptyList())
    val toeicSwInfo: StateFlow<List<EnglishTestInfo.TOEIC_SW>> = _toeicSwInfo

    private val _eikenFirstInfo = MutableStateFlow<List<EnglishTestInfo.EIKEN_FIRST>>(emptyList())
    val eikenFirstInfo: StateFlow<List<EnglishTestInfo.EIKEN_FIRST>> = _eikenFirstInfo

    private val _toeflInfo = MutableStateFlow<List<EnglishTestInfo.TOEFL>>(emptyList())
    val toeflInfo: StateFlow<List<EnglishTestInfo.TOEFL>> = _toeflInfo

    private val _ieltsInfo = MutableStateFlow<List<EnglishTestInfo.IELTS>>(emptyList())
    val ieltsInfo: StateFlow<List<EnglishTestInfo.IELTS>> = _ieltsInfo


    private val ONBOARDING_COMPLETED_KEY = booleanPreferencesKey("onboarding_completed")

    // 初回起動かどうかを監視するFlow
    val isFirstLaunch: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[ONBOARDING_COMPLETED_KEY] ?: true
    }
    private val _unsavedChanges = MutableStateFlow(false)
    val unsavedChanges: StateFlow<Boolean> = _unsavedChanges
    private val _sumScore = MutableStateFlow(0)
    val sumScore: StateFlow<Int> = _sumScore
    private val _readingScore = MutableStateFlow(0)
    val readingScore: StateFlow<Int> = _readingScore
    private val _listeningScore = MutableStateFlow(0)
    val listeningScore: StateFlow<Int> = _listeningScore
    private val _writingScore = MutableStateFlow(0)
    val writingScore: StateFlow<Int> = _writingScore
    private val _speakingScore = MutableStateFlow(0)
    val speakingScore: StateFlow<Int> = _speakingScore
    private val _memoText = MutableStateFlow("")
    val memoText: StateFlow<String> = _memoText

    // Onboarding 完了時にフラグを保存
    fun completeOnboarding() {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[ONBOARDING_COMPLETED_KEY] = false
            }
        }
    }

    // TOEICデータの保存
    fun saveToeicValues(
        date: String,
        readingScore: Int,
        listeningScore: Int,
        memo: String
    ) {
        viewModelScope.launch {
            repository.saveToeicInfo(
                date,
                readingScore,
                listeningScore,
                memo
            )
            loadAllToeicInfo() // データを保存後に再読み込み
        }
    }

    // TOEIC情報を取得
    fun loadAllToeicInfo() {
        viewModelScope.launch {
            _toeicInfo.value = repository.getAllToeicInfo()
        }
    }

    fun saveToeicSwValues(
        date: String,
        writingScore: Int,
        speakingScore: Int,
        memo: String
    ) {
        viewModelScope.launch {
            repository.saveToeicSwInfo(
                date,
                writingScore,
                speakingScore,
                memo
            )
            loadAllToeicSwInfo() // データを保存後に再読み込み
        }
    }

    fun loadAllToeicSwInfo() {
        viewModelScope.launch {
            _toeicSwInfo.value = repository.getAllToeicSwInfo()
        }
    }

    fun setSumScore(value: Int) {
        _sumScore.value = value
        _unsavedChanges.value = value > 0
    }
    fun setReadingScore(value: Int) {
        _readingScore.value = value
        _unsavedChanges.value = value > 0
    }
    fun setListeningScore(value: Int) {
        _listeningScore.value = value
        _unsavedChanges.value = value > 0
    }
    fun setWritingScore(value: Int) {
        _writingScore.value = value
        _unsavedChanges.value = value > 0
    }
    fun setSpeakingScore(value: Int) {
        _speakingScore.value = value
        _unsavedChanges.value = value > 0
    }
    fun setMemoText(value: String) {
        _memoText.value = value
        _unsavedChanges.value = value.isNotEmpty()
    }
}
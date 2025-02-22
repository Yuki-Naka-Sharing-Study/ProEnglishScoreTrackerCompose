package com.example.proenglishscoretracker.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    // 特定のTOEIC情報を保持するStateFlow
    private val _selectedToeicInfo = MutableStateFlow<EnglishTestInfo.TOEIC?>(null)
    val selectedToeicInfo: StateFlow<EnglishTestInfo.TOEIC?> = _selectedToeicInfo

    private val _toeicSwInfo = MutableStateFlow<List<EnglishTestInfo.TOEIC_SW>>(emptyList())
    val toeicSwInfo: StateFlow<List<EnglishTestInfo.TOEIC_SW>> = _toeicSwInfo

    private val _selectedToeicSwInfo = MutableStateFlow<EnglishTestInfo.TOEIC_SW?>(null)
    val selectedToeicSwInfo: StateFlow<EnglishTestInfo.TOEIC_SW?> = _selectedToeicSwInfo

    private val _eikenFirstInfo = MutableStateFlow<List<EnglishTestInfo.EIKEN_FIRST>>(emptyList())
    val eikenFirstInfo: StateFlow<List<EnglishTestInfo.EIKEN_FIRST>> = _eikenFirstInfo

    private val _eikenSecondInfo = MutableStateFlow<List<EnglishTestInfo.EIKEN_SECOND>>(emptyList())
    val eikenSecondInfo: StateFlow<List<EnglishTestInfo.EIKEN_SECOND>> = _eikenSecondInfo

    private val _selectedEikenInfo = MutableStateFlow<EnglishTestInfo.EIKEN_SECOND?>(null)
    val selectedEikenInfo: StateFlow<EnglishTestInfo.EIKEN_SECOND?> = _selectedEikenInfo

    private val _toeflIbtInfo = MutableStateFlow<List<EnglishTestInfo.TOEFL>>(emptyList())
    val toeflIbtInfo: StateFlow<List<EnglishTestInfo.TOEFL>> = _toeflIbtInfo

    private val _selectedToeflIbtInfo = MutableStateFlow<EnglishTestInfo.TOEFL?>(null)
    val selectedToeflIbtInfo: StateFlow<EnglishTestInfo.TOEFL?> = _selectedToeflIbtInfo

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

    // TOEIC
    init {
        viewModelScope.launch {
            _toeicInfo.value = englishInfoDao.getAllToeicInfo()
        }
    }
    init {
        loadAllToeicInfo()
    }
    fun loadToeicInfoById(toeicId: String) {
        viewModelScope.launch {
            val toeicInfo = repository.getToeicInfoById(toeicId)
            _selectedToeicInfo.value = toeicInfo
        }
    }
    fun loadAllToeicInfo() {
        viewModelScope.launch {
            _toeicInfo.value = repository.getAllToeicInfo()
        }
    }
    fun saveToeicValues(
        date: String,
        readingScore: Int,
        listeningScore: Int,
        memo: String,
        showAlert: (String) -> Unit
    ) {
        viewModelScope.launch {
            val year = date.substring(0, 4)
            val yearCount = repository.getEntryCountByYear(year)
            val dateCount = repository.getEntryCountByDate(date)

            if (dateCount > 0) {
                showAlert("同一年月日で既に登録済です。")
                return@launch
            }
            if (yearCount >= 10) {
                showAlert("TOEICは年間で11回以上試験を受験することはできません。")
                return@launch
            }
            repository.saveToeicInfo(date, readingScore, listeningScore, memo)
            loadAllToeicInfo() // データ保存後にリストを更新
        }
    }

    // TOEICデータを削除
    fun deleteToeicInfo(toeicId: String) {
        viewModelScope.launch {
            val info = repository.getToeicInfoById(toeicId)
            info?.let {
                repository.deleteToeicInfo(it)
                loadAllToeicInfo() // データ削除後に最新のリストを取得
            }
        }
    }
    fun updateToeicValues(toeicInfo: EnglishTestInfo.TOEIC) {
        viewModelScope.launch {
            repository.updateToeicInfo(toeicInfo)
            this@EnglishInfoViewModel._toeicInfo.value = englishInfoDao.getAllToeicInfo()
        }
    }


    // TOEIC SW
    init {
        viewModelScope.launch {
            _toeicSwInfo.value = englishInfoDao.getAllToeicSwInfo()
        }
    }
    init {
        loadAllToeicSwInfo()
    }
    fun loadAllToeicSwInfo() {
        viewModelScope.launch {
            _toeicSwInfo.value = repository.getAllToeicSwInfo()
        }
    }
    fun loadToeicSwInfoById(toeicSwId: String) {
        viewModelScope.launch {
            val toeicSwInfo = repository.getToeicSwInfoById(toeicSwId)
            _selectedToeicSwInfo.value = toeicSwInfo
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
    // TOEICデータを削除
    fun deleteToeicSwInfo(toeicSwId: String) {
        viewModelScope.launch {
            val info = repository.getToeicSwInfoById(toeicSwId)
            info?.let {
                repository.deleteToeicSwInfo(it)
                loadAllToeicSwInfo() // データ削除後に最新のリストを取得
            }
        }
    }
    fun updateToeicSwValues(toeicInfo: EnglishTestInfo.TOEIC_SW) {
        viewModelScope.launch {
            repository.updateToeicSwInfo(toeicInfo)
            this@EnglishInfoViewModel._toeicSwInfo.value = englishInfoDao.getAllToeicSwInfo()
        }
    }



    // 英検
    private val _eikenErrorMessage = MutableLiveData<String?>()
    val eikenSameYearErrorMessage: LiveData<String?> = _eikenErrorMessage

    init {
        viewModelScope.launch {
            _eikenSecondInfo.value = englishInfoDao.getAllEikenInfo()
        }
    }
    init {
        loadAllEikenInfo()
    }
    fun loadAllEikenInfo() {
        viewModelScope.launch {
//            _toeicInfo.value = repository.getAllToeicInfo()
            _eikenSecondInfo.value = repository.getAllEikenInfo()
        }
    }
    fun loadEikenInfoById(eikenId: String) {
        viewModelScope.launch {
            val eikenInfo = repository.getEikenInfoById(eikenId)
            _selectedEikenInfo.value = eikenInfo
        }
    }
    fun saveEikenValues(
        date: String,
        grade: String,
        readingScore: Int,
        listeningScore: Int,
        writingScore: Int,
        speakingScore: Int,
        cseScore: Int,
        memo: String
    ) {
        viewModelScope.launch {
            val year = date.substring(0, 4) // 'yyyy-MM-dd'形式の日付から年を抽出
            val count = repository.getEntryCountByGradeAndYear(grade, year)
            if (count >= 3) {
                _eikenErrorMessage.value = "同一級は年間で3回までしか登録できません。"
            } else {
                repository.saveEikenInfo(
                    date,
                    grade,
                    readingScore,
                    listeningScore,
                    writingScore,
                    speakingScore,
                    cseScore,
                    memo
                )
                loadAllEikenInfo() // データを保存後に再読み込み
                _eikenErrorMessage.value = null // エラーメッセージをクリア
            }
        }
    }
    fun clearErrorMessage() {
        _eikenErrorMessage.value = null
    }
    fun deleteEikenInfo(eikenId: String) {
        viewModelScope.launch {
            val info = repository.getEikenInfoById(eikenId)
            info?.let {
                repository.deleteEikenInfo(it)
                loadAllEikenInfo() // データ削除後に最新のリストを取得
            }
        }
    }
    fun updateEikenValues(eikenInfo: EnglishTestInfo.EIKEN_SECOND) {
        viewModelScope.launch {
            repository.updateEikenInfo(eikenInfo)
            this@EnglishInfoViewModel._eikenSecondInfo.value = englishInfoDao.getAllEikenInfo()
        }
    }



    // TOEFL
    init {
        viewModelScope.launch {
            _toeflIbtInfo.value = englishInfoDao.getAllToeflIbtInfo()
        }
    }
    init {
        loadAllToeflInfo()
    }
    fun loadAllToeflInfo() {
        viewModelScope.launch {
            _toeflIbtInfo.value = repository.getAllToeflInfo()
        }
    }
    fun loadToeflIbtInfoById(toeflIbtId: String) {
        viewModelScope.launch {
            val toeflIbtInfo = repository.getToeflIbtInfoById(toeflIbtId)
            _selectedToeflIbtInfo.value = toeflIbtInfo
        }
    }
    fun saveToeflValues(
        date: String,
        readingScore: Int,
        listeningScore: Int,
        writingScore: Int,
        speakingScore: Int,
        overallScore: Int,
        memo: String
    ) {
        viewModelScope.launch {
            repository.saveToeflInfo(
                date,
                readingScore,
                listeningScore,
                writingScore,
                speakingScore,
                overallScore,
                memo
            )
            loadAllToeflInfo() // データを保存後に再読み込み
        }
    }
    fun deleteToeflIbtInfo(toeflIbtId: String) {
        viewModelScope.launch {
            val info = repository.getToeflIbtInfoById(toeflIbtId)
            info?.let {
                repository.deleteToeflIbtInfo(it)
                loadAllToeflInfo() // データ削除後に最新のリストを取得
            }
        }
    }
    fun updateToeflIbtValues(toeflIbtInfo: EnglishTestInfo.TOEFL) {
        viewModelScope.launch {
            repository.updateToeflIbtInfo(toeflIbtInfo)
            this@EnglishInfoViewModel._toeflIbtInfo.value = englishInfoDao.getAllToeflIbtInfo()
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
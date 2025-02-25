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



    // TOEIC
    // TODO : TOEICの情報喪失アラートの実装

    private val _toeicInfo = MutableStateFlow<List<EnglishTestInfo.TOEIC>>(emptyList())
    val toeicInfo: StateFlow<List<EnglishTestInfo.TOEIC>> = _toeicInfo

    // 特定のTOEIC情報を保持するStateFlow
    private val _selectedToeicInfo = MutableStateFlow<EnglishTestInfo.TOEIC?>(null)
    val selectedToeicInfo: StateFlow<EnglishTestInfo.TOEIC?> = _selectedToeicInfo

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
            val yearCount = repository.getToeicEntryCountByYear(year)
            val dateCount = repository.getToeicEntryCountByDate(date)

            if (dateCount > 0) {
                showAlert("同一年月日で既に登録済です。")
                return@launch
            }
            if (yearCount >= 10) {
                showAlert("TOEICは年間で11回以上試験を受験することはできません。")
                return@launch
            }
            repository.saveToeicInfo(date, readingScore, listeningScore, memo)
            loadAllToeicInfo()
        }
    }
    fun deleteToeicInfo(toeicId: String) {
        viewModelScope.launch {
            val info = repository.getToeicInfoById(toeicId)
            info?.let {
                repository.deleteToeicInfo(it)
                loadAllToeicInfo()
            }
        }
    }
    fun updateToeicInfo(toeicInfo: EnglishTestInfo.TOEIC) {
        viewModelScope.launch {
            repository.updateToeicInfo(toeicInfo)
            loadAllToeicInfo()
        }
    }



    // TOEIC SW
    private val _toeicSwInfo = MutableStateFlow<List<EnglishTestInfo.TOEIC_SW>>(emptyList())
    val toeicSwInfo: StateFlow<List<EnglishTestInfo.TOEIC_SW>> = _toeicSwInfo

    private val _selectedToeicSwInfo = MutableStateFlow<EnglishTestInfo.TOEIC_SW?>(null)
    val selectedToeicSwInfo: StateFlow<EnglishTestInfo.TOEIC_SW?> = _selectedToeicSwInfo

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
        memo: String,
        showAlert: (String) -> Unit
    ) {
        viewModelScope.launch {
            val year = date.substring(0, 4)
            val yearCount = repository.getToeicSwEntryCountByYear(year)
            val dateCount = repository.getToeicSwEntryCountByDate(date)

            if (dateCount > 0) {
                showAlert("同一年月日で既に登録済です。")
                return@launch
            }
            if (yearCount >= 24) {
                showAlert("TOEICは年間で25回以上試験を受験することはできません。")
                return@launch
            }
            repository.saveToeicSwInfo(date, writingScore, speakingScore, memo)
            loadAllToeicSwInfo()
        }
    }
    fun deleteToeicSwInfo(toeicSwId: String) {
        viewModelScope.launch {
            val info = repository.getToeicSwInfoById(toeicSwId)
            info?.let {
                repository.deleteToeicSwInfo(it)
                loadAllToeicSwInfo()
            }
        }
    }
    fun updateToeicSwInfo(toeicSwInfo: EnglishTestInfo.TOEIC_SW) {
        viewModelScope.launch {
            repository.updateToeicSwInfo(toeicSwInfo)
            loadAllToeicSwInfo()
        }
    }



    // 英検
    private val _eikenSecondInfo = MutableStateFlow<List<EnglishTestInfo.EIKEN>>(emptyList())
    val eikenSecondInfo: StateFlow<List<EnglishTestInfo.EIKEN>> = _eikenSecondInfo

    private val _selectedEikenInfo = MutableStateFlow<EnglishTestInfo.EIKEN?>(null)
    val selectedEikenInfo: StateFlow<EnglishTestInfo.EIKEN?> = _selectedEikenInfo

    private val _eikenUnsavedChanges = MutableStateFlow(false)
    val eikenUnsavedChanges: StateFlow<Boolean> = _eikenUnsavedChanges
    private val _eikenGrade = MutableStateFlow("")
    val eikenGrade: StateFlow<String> = _eikenGrade
    private val _eikenReadingScore = MutableStateFlow(0)
    val eikenReadingScore: StateFlow<Int> = _eikenReadingScore
    private val _eikenListeningScore = MutableStateFlow(0)
    val eikenListeningScore: StateFlow<Int> = _eikenListeningScore
    private val _eikenWritingScore = MutableStateFlow(0)
    val eikenWritingScore: StateFlow<Int> = _eikenWritingScore
    private val _eikenSpeakingScore = MutableStateFlow(0)
    val eikenSpeakingScore: StateFlow<Int> = _eikenSpeakingScore
    private val _eikenMemoText = MutableStateFlow("")
    val eikenMemoText: StateFlow<String> = _eikenMemoText

    fun setEikenGrade(value: String) {
        _eikenGrade.value = value
        _eikenUnsavedChanges.value = value.isNotEmpty()
    }
    fun setEikenReadingScore(value: Int) {
        _eikenReadingScore.value = value
        _eikenUnsavedChanges.value = value > 0
    }
    fun setEikenListeningScore(value: Int) {
        _eikenListeningScore.value = value
        _eikenUnsavedChanges.value = value > 0
    }
    fun setEikenWritingScore(value: Int) {
        _eikenWritingScore.value = value
        _eikenUnsavedChanges.value = value > 0
    }
    fun setEikenSpeakingScore(value: Int) {
        _eikenSpeakingScore.value = value
        _eikenUnsavedChanges.value = value > 0
    }
    fun setEikenMemoText(value: String) {
        _eikenMemoText.value = value
        _eikenUnsavedChanges.value = value.isNotEmpty()
    }

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
        memo: String,
        showAlert: (String) -> Unit
    ) {
        viewModelScope.launch {
            if (grade.isEmpty()) {
                showAlert("受験級が選択されていません。")
                return@launch
            }

            // 同一年月日かつ同一級の登録チェック
            val dateAndGradeCount = repository.getEikenEntryCountByDateAndGrade(date, grade)
            if (dateAndGradeCount > 0) {
                showAlert("同一年月日で且つ同一級を既に登録済です。")
                return@launch
            }

            // 同一月かつ同一級の登録チェック
            val yearMonth = date.substring(0, 7)
            val monthAndGradeCount = repository.getEikenEntryCountByYearMonthAndGrade(yearMonth, grade)
            if (monthAndGradeCount > 0) {
                showAlert("同一月で同一級を既に登録済です。")
                return@launch
            }

            // 同一年度内での登録回数チェック
            val year = date.substring(0, 4)
            val count = repository.getEntryCountByGradeAndYear(grade, year)
            if (count >= 3) {
                showAlert("同一級は年間で3回までしか登録できません。")
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
                loadAllEikenInfo()
            }
        }
    }
    fun deleteEikenInfo(eikenId: String) {
        viewModelScope.launch {
            val info = repository.getEikenInfoById(eikenId)
            info?.let {
                repository.deleteEikenInfo(it)
                loadAllEikenInfo()
            }
        }
    }
    fun updateEikenInfo(eikenInfo: EnglishTestInfo.EIKEN) {
        viewModelScope.launch {
            repository.updateEikenInfo(eikenInfo)
            loadAllEikenInfo()
        }
    }



    // TOEFL
    private val _toeflIbtInfo = MutableStateFlow<List<EnglishTestInfo.TOEFL>>(emptyList())
    val toeflIbtInfo: StateFlow<List<EnglishTestInfo.TOEFL>> = _toeflIbtInfo

    private val _selectedToeflIbtInfo = MutableStateFlow<EnglishTestInfo.TOEFL?>(null)
    val selectedToeflIbtInfo: StateFlow<EnglishTestInfo.TOEFL?> = _selectedToeflIbtInfo

    init {
        viewModelScope.launch {
            _toeflIbtInfo.value = englishInfoDao.getAllToeflIbtInfo()
        }
    }
    init {
        loadAllToeflIbtInfo()
    }
    fun loadAllToeflIbtInfo() {
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
        memo: String,
        showAlert: (String) -> Unit
    ) {
        viewModelScope.launch {
            val dateCount = repository.getToeflIbtEntryCountByDate(date)

            if (dateCount > 0) {
                showAlert("同一年月日で既に登録済です。")
                return@launch
            }

            repository.saveToeflInfo(
                date,
                readingScore,
                listeningScore,
                writingScore,
                speakingScore,
                overallScore,
                memo
            )
            loadAllToeflIbtInfo()
        }
    }
    fun deleteToeflIbtInfo(toeflIbtId: String) {
        viewModelScope.launch {
            val info = repository.getToeflIbtInfoById(toeflIbtId)
            info?.let {
                repository.deleteToeflIbtInfo(it)
                loadAllToeflIbtInfo()
            }
        }
    }
    fun updateToeflIbtInfo(toeflIbtInfo: EnglishTestInfo.TOEFL) {
        viewModelScope.launch {
            repository.updateToeflIbtInfo(toeflIbtInfo)
            loadAllToeflIbtInfo()
        }
    }



    // IELTS
    private val _ieltsInfo = MutableStateFlow<List<EnglishTestInfo.IELTS>>(emptyList())
    val ieltsInfo: StateFlow<List<EnglishTestInfo.IELTS>> = _ieltsInfo
}
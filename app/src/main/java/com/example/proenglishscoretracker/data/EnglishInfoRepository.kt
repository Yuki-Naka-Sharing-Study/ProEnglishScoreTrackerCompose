package com.example.proenglishscoretracker.data

class EnglishInfoRepository(private val dao: EnglishInfoDao) {
    // TOEIC
    // TOEICデータを保存（制限チェック用）
    suspend fun getToeicEntryCountByYear(year: String): Int {
        return dao.countToeicEntriesByYear(year)
    }
    suspend fun getToeicEntryCountByDate(date: String): Int {
        return dao.countToeicEntriesByDate(date)
    }
    suspend fun saveToeicInfo(
        date: String,
        readingScore: Int,
        listeningScore: Int,
        memo: String
    ) {
        val toeicInfo = EnglishTestInfo.TOEIC(
            id = 0,
            date = date,
            readingScore = readingScore,
            listeningScore = listeningScore,
            memo = memo
        )
        dao.insertToeicInfo(toeicInfo)
    }
    suspend fun getAllToeicInfo(): List<EnglishTestInfo.TOEIC> {
        return dao.getAllToeicInfo()
    }
    suspend fun deleteToeicInfo(toeicInfo: EnglishTestInfo.TOEIC) {
        dao.deleteToeicInfo(toeicInfo)
    }
    suspend fun updateToeicInfo(toeicInfo: EnglishTestInfo.TOEIC) {
        dao.updateToeicInfo(toeicInfo)
    }
    suspend fun getToeicInfoById(toeicId: String): EnglishTestInfo.TOEIC? {
        return dao.getToeicInfoById(toeicId)
    }


    // TOEIC SW
    // TOEICデータを保存（制限チェック用）
    suspend fun getToeicSwEntryCountByYear(year: String): Int {
        return dao.countToeicSwEntriesByYear(year)
    }
    suspend fun getToeicSwEntryCountByDate(date: String): Int {
        return dao.countToeicSwEntriesByDate(date)
    }
    suspend fun saveToeicSwInfo(
        date: String,
        writingScore: Int,
        speakingScore: Int,
        memo: String
    ) {
        val toeicSwInfo = EnglishTestInfo.TOEIC_SW(
            id = 0,
            date = date,
            writingScore = writingScore,
            speakingScore = speakingScore,
            memo = memo
        )
        dao.insertToeicSwInfo(toeicSwInfo)
    }
    suspend fun deleteToeicSwInfo(toeicSwInfo: EnglishTestInfo.TOEIC_SW) {
        dao.deleteToeicSwInfo(toeicSwInfo)
    }
    suspend fun updateToeicSwInfo(toeicSwInfo: EnglishTestInfo.TOEIC_SW) {
        dao.updateToeicSwInfo(toeicSwInfo)
    }
    suspend fun getAllToeicSwInfo(): List<EnglishTestInfo.TOEIC_SW> {
        return dao.getAllToeicSwInfo()
    }
    suspend fun getToeicSwInfoById(toeicSwId: String): EnglishTestInfo.TOEIC_SW? {
        return dao.getToeicSwInfoById(toeicSwId)
    }


    // 英検
    suspend fun getEikenEntryCountByDate(date: String): Int {
        return dao.countEIkenEntriesByDate(date)
    }
    suspend fun getEntryCountByGradeAndYear(grade: String, year: String): Int {
        return dao.countEntriesByGradeAndYear(grade, year)
    }
    suspend fun saveEikenInfo(
        date: String,
        grade: String,
        readingScore: Int,
        listeningScore: Int,
        writingScore: Int,
        speakingScore: Int,
        cseScore: Int,
        memo: String,
    ) {
        val eikenInfo = EnglishTestInfo.EIKEN_SECOND(
            id = 0,
            date = date,
            grade = grade,
            readingScore = readingScore,
            listeningScore = listeningScore,
            writingScore = writingScore,
            speakingScore = speakingScore,
            cseScore = cseScore,
            memo = memo
        )
        dao.insertEikenInfo(eikenInfo)
    }
    suspend fun getAllEikenInfo(): List<EnglishTestInfo.EIKEN_SECOND> {
        return dao.getAllEikenInfo()
    }
    suspend fun deleteEikenInfo(eikenInfo: EnglishTestInfo.EIKEN_SECOND) {
        dao.deleteEikenInfo(eikenInfo)
    }
    suspend fun updateEikenInfo(eikenInfo: EnglishTestInfo.EIKEN_SECOND) {
        dao.updateEikenInfo(eikenInfo)
    }
    suspend fun getEikenInfoById(eikenId: String): EnglishTestInfo.EIKEN_SECOND? {
        return dao.getEikenInfoById(eikenId)
    }


    // TOEFL
    suspend fun getToeflIbtEntryCountByDate(date: String): Int {
        return dao.countToeflIbtEntriesByDate(date)
    }
    suspend fun saveToeflInfo(
        date: String,
        readingScore: Int,
        listeningScore: Int,
        writingScore: Int,
        speakingScore: Int,
        overallScore: Int,
        memo: String
    ) {
        val toeflInfo = EnglishTestInfo.TOEFL(
            id = 0,
            date = date,
            readingScore = readingScore,
            listeningScore = listeningScore,
            writingScore = writingScore,
            speakingScore = speakingScore,
            overallScore = overallScore,
            memo = memo
        )
        dao.insertToeflInfo(toeflInfo)
    }
    suspend fun deleteToeflIbtInfo(toeflIbtInfo: EnglishTestInfo.TOEFL) {
        dao.deleteToeflIbtInfo(toeflIbtInfo)
    }
    suspend fun getAllToeflInfo(): List<EnglishTestInfo.TOEFL> {
        return dao.getAllToeflIbtInfo()
    }
    suspend fun updateToeflIbtInfo(toeflIbtInfo: EnglishTestInfo.TOEFL) {
        dao.updateToeflIbtInfo(toeflIbtInfo)
    }
    suspend fun getToeflIbtInfoById(toeflIbtId: String): EnglishTestInfo.TOEFL? {
        return dao.getToeflIbtInfoById(toeflIbtId)
    }
}

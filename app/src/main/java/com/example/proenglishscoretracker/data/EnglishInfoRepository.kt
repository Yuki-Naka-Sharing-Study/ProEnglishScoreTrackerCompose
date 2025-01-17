package com.example.proenglishscoretracker.data

class EnglishInfoRepository(private val dao: EnglishInfoDao) {
    // TOEIC
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
    suspend fun deleteToeicInfo(toeicInfo: EnglishTestInfo.TOEIC) {
        dao.deleteToeicInfo(toeicInfo)
    }
    suspend fun getAllToeicInfo(): List<EnglishTestInfo.TOEIC> {
        return dao.getAllToeicInfo()
    }
    suspend fun updateToeicInfo(toeicInfo: EnglishTestInfo.TOEIC) {
        dao.updateToeicInfo(toeicInfo)
    }

    // TOEIC SW
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
    suspend fun getAllToeicSwInfo(): List<EnglishTestInfo.TOEIC_SW> {
        return dao.getAllToeicSwInfo()
    }

    // TOEFL
    suspend fun saveToeflInfo(
        date: String,
        overallScore: Int,
        readingScore: Int,
        listeningScore: Int,
        writingScore: Int,
        speakingScore: Int,
        memo: String
    ) {
        val toeflInfo = EnglishTestInfo.TOEFL(
            id = 0,
            date = date,
            overallScore = overallScore,
            readingScore = readingScore,
            listeningScore = listeningScore,
            writingScore = writingScore,
            speakingScore = speakingScore,
            memo = memo
        )
        dao.insertToeflInfo(toeflInfo)
    }
    suspend fun getAllToeflInfo(): List<EnglishTestInfo.TOEFL> {
        return dao.getAllToeflInfo()
    }
}

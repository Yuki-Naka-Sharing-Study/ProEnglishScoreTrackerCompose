package com.example.proenglishscoretracker.data

class EnglishInfoRepository(private val dao: EnglishInfoDao) {
    // TOEICデータの保存
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
}

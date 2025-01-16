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
    // TOEICデータの取得
    suspend fun getAllToeicInfo(): List<EnglishTestInfo.TOEIC> {
        return dao.getAllToeicInfo()
    }
}

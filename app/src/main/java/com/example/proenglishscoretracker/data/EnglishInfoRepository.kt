package com.example.proenglishscoretracker.data

class EnglishInfoRepository {
    fun saveEikenIchijiInfo(cseScore: Int,
                            readingScore: Int,
                            listeningScore: Int,
                            writingScore: Int,
                            memoText: String
    ){

    }

    fun saveEikenNijiInfo(cseScore: Int,
                          speakingScore: Int,
                          shortSpeechScore: Int,
                          interactionScore: Int,
                          grammarAndVocabularyScore: Int,
                          pronunciationScore: Int,
                          memoText: String
    ){

    }

    fun saveToeicInfo(readingScore: Int,
                      listeningScore: Int,
                      memoText: String
    ){

    }

    fun saveToeicSwInfo(writingScore: Int,
                        speakingScore: Int,
                        memoText: String
    ){

    }

    fun saveToeflIbtInfo(overallScore: String,
                         readingScore: String,
                         listeningScore: String,
                         writingScore: String,
                         speakingScore: String,
                         memoText: String
    ){

    }

    fun saveIeltsInfo(overallScore: Float,
                      readingScore: Float,
                      listeningScore: Float,
                      writingScore: Float,
                      speakingScore: Float,
                      memoText: String
    ){

    }
}

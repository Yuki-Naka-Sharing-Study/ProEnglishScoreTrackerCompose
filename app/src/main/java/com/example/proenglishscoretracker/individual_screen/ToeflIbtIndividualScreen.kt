package com.example.proenglishscoretracker.individual_screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.ui.unit.dp
import com.example.proenglishscoretracker.data.EnglishInfoViewModel
import com.example.proenglishscoretracker.data.EnglishTestInfo

@Composable
fun ToeflIbtIndividualScreen(viewModel: EnglishInfoViewModel) {
    val toeflInfoList = viewModel.toeflInfo.collectAsState().value
    // LazyColumnでデータを表示
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(items = toeflInfoList) { toeflInfo ->
            ToeflItem(toeflInfo = toeflInfo)
        }
    }
}

@Composable
private fun ToeflItem(toeflInfo: EnglishTestInfo.TOEFL) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "受験日: ${toeflInfo.date}")
            Text(text = "Overallスコア: ${toeflInfo.overallScore}")
            Text(text = "Readingスコア: ${toeflInfo.readingScore}")
            Text(text = "Listeningスコア: ${toeflInfo.listeningScore}")
            Text(text = "Writingスコア: ${toeflInfo.writingScore}")
            Text(text = "Speakingスコア: ${toeflInfo.speakingScore}")
            Text(text = "メモ: ${toeflInfo.memo}")
        }
    }
}

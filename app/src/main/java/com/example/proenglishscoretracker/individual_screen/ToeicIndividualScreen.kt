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
fun ToeicIndividualScreen(viewModel: EnglishInfoViewModel) {
    // ViewModelからTOEICデータを取得
    val toeicInfoList = viewModel.toeicInfo.collectAsState().value
    // LazyColumnでデータを表示
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(items = toeicInfoList) { toeicInfo ->
            ToeicItem(toeicInfo = toeicInfo)
        }
    }
}
// 個別のTOEICデータを表示
@Composable
fun ToeicItem(toeicInfo: EnglishTestInfo.TOEIC) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "受験日: ${toeicInfo.date}")
            Text(text = "リーディングスコア: ${toeicInfo.readingScore}")
            Text(text = "リスニングスコア: ${toeicInfo.listeningScore}")
            Text(text = "メモ: ${toeicInfo.memo}")
        }
    }
}

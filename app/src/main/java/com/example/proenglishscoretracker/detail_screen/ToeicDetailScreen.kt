package com.example.proenglishscoretracker.detail_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proenglishscoretracker.data.EnglishInfoViewModel

@Composable
fun ToeicDetailScreen(toeicId: String, viewModel: EnglishInfoViewModel) {
    // 画面表示時にデータを取得
    LaunchedEffect(toeicId) {
        viewModel.loadToeicInfoById(toeicId)
    }

    // selectedToeicInfoを監視
    val toeicInfo by viewModel.selectedToeicInfo.collectAsState()

    if (toeicInfo != null) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "受験日: ${toeicInfo!!.date}", fontSize = 20.sp)
            Text(text = "リーディングスコア: ${toeicInfo!!.readingScore}", fontSize = 20.sp)
            Text(text = "リスニングスコア: ${toeicInfo!!.listeningScore}", fontSize = 20.sp)
            Text(text = "メモ: ${toeicInfo!!.memo}", fontSize = 20.sp)
        }
    } else {
        Text(
            text = "データが見つかりませんでした。",
            color = androidx.compose.ui.graphics.Color.Red,
            modifier = Modifier.padding(16.dp)
        )
    }
}

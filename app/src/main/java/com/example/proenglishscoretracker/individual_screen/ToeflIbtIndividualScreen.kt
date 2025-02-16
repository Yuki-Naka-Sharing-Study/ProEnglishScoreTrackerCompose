package com.example.proenglishscoretracker.individual_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ToeflIbtIndividualScreen(
    viewModel: EnglishInfoViewModel,
    navController: NavController
) {
    val toeflIbtInfoList = viewModel.toeflIbtInfo.collectAsState().value

    // 日付の降順でソート
    val sortedToeflIbtInfoList = rememberSaveable(toeflIbtInfoList) {
        toeflIbtInfoList.sortedByDescending { it.date }
    }

    if (sortedToeflIbtInfoList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "まだスコアが登録されていません。",
                fontSize = 18.sp,
                color = Color.Gray
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(items = sortedToeflIbtInfoList) { toeflIbtInfo ->
                ToeflIbtInfo(
                    toeflIbtInfo = toeflIbtInfo,
                    navController = navController
                )
            }
        }
    }
}
@Composable
private fun ToeflIbtInfo(
    toeflIbtInfo: EnglishTestInfo.TOEFL,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable {
                    // 詳細画面に遷移
                    navController.navigate("toefl_ibt_detail/${toeflIbtInfo.id}")
                },
        ) {
            Column(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text(text = "受験日: ${toeflIbtInfo.date}")
                Text(text = "Overallスコア: ${toeflIbtInfo.overallScore}")
                Text(text = "Readingスコア: ${toeflIbtInfo.readingScore}")
                Text(text = "Listeningスコア: ${toeflIbtInfo.listeningScore}")
                Text(text = "Writingスコア: ${toeflIbtInfo.writingScore}")
                Text(text = "Speakingスコア: ${toeflIbtInfo.speakingScore}")
                Text(text = "メモ: ${toeflIbtInfo.memo}")
            }
        }
    }
}

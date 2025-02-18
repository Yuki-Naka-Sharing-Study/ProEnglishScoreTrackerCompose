package com.example.proenglishscoretracker.individual_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.example.proenglishscoretracker.data.EnglishInfoViewModel
import com.example.proenglishscoretracker.data.EnglishTestInfo
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ToeflIbtIndividualScreen(
    viewModel: EnglishInfoViewModel,
    navController: NavController
) {
    val toeflIbtInfoList = viewModel.toeflIbtInfo.collectAsState().value

    // 日付の降順でソート
    val sortedToeflIbtInfoList = remember(toeflIbtInfoList) {
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
                Row(

                ) {
                    Text(text = "受験日:")
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = toeflIbtInfo.date)
                }

                Row(

                ) {
                    Text(text = "Overallスコア:")
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = toeflIbtInfo.overallScore.toString())
                }

                Row(

                ) {
                    Text(text = "リーディングスコア:")
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = toeflIbtInfo.readingScore.toString())
                }

                Row(

                ) {
                    Text(text = "リスニングスコア:")
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = toeflIbtInfo.listeningScore.toString())
                }

                Row(

                ) {
                    Text(text = "ライティングスコア:")
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = toeflIbtInfo.writingScore.toString())
                }

                Row(

                ) {
                    Text(text = "スピーキングスコア:")
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = toeflIbtInfo.speakingScore.toString())
                }

                Row(

                ) {
                    Text(
                        text = "メモ: ${toeflIbtInfo.memo}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

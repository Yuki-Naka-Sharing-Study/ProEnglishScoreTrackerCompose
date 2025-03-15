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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.example.proenglishscoretracker.data.EnglishInfoViewModel
import com.example.proenglishscoretracker.data.EnglishTestInfo
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proenglishscoretracker.R

// TODO : りくとさんから頂いたFB通り、Circular Progress Indicatorを使ったデザインで修正
@Composable
fun IeltsIndividualScreen(
    viewModel: EnglishInfoViewModel,
    navController: NavController
) {
    val ieltsInfoList = viewModel.ieltsInfo.collectAsState().value

    // 日付の降順でソート
    val sortedIeltsInfoList = remember(ieltsInfoList) {
        ieltsInfoList.sortedByDescending { it.date }
    }

    if (sortedIeltsInfoList.isEmpty()) {
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
            items(items = sortedIeltsInfoList) { ieltsInfo ->
                IeltsInfo(
                    ieltsInfo = ieltsInfo,
                    navController = navController
                )
            }
        }
    }
}
@Composable
private fun IeltsInfo(
    ieltsInfo: EnglishTestInfo.IELTS,
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
                    navController.navigate("ielts_detail/${ieltsInfo.id}")
                },
        ) {
            Column(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "受験日:")
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = ieltsInfo.date)
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_8_dp)))
                Divider()
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_8_dp)))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Overallスコア:")
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = ieltsInfo.overallScore.toString())
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_8_dp)))
                Divider()
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_8_dp)))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Readingスコア:")
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = ieltsInfo.readingScore.toString())
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_8_dp)))
                Divider()
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_8_dp)))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Listeningスコア:")
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = ieltsInfo.listeningScore.toString())
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_8_dp)))
                Divider()
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_8_dp)))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Writingスコア:")
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = ieltsInfo.writingScore.toString())
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_8_dp)))
                Divider()
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_8_dp)))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Speakingスコア:")
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = ieltsInfo.speakingScore.toString())
                }

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_8_dp)))
                Divider()
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_8_dp)))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "メモ: ${ieltsInfo.memo}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

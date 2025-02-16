package com.example.proenglishscoretracker.detail_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proenglishscoretracker.R
import com.example.proenglishscoretracker.data.EnglishInfoViewModel

@Composable
fun ToeicDetailScreen(
    toeicId: String,
    viewModel: EnglishInfoViewModel,
    navController: NavController
) {
    // 画面表示時にデータを取得
    LaunchedEffect(toeicId) {
        viewModel.loadToeicInfoById(toeicId)
    }

    val toeicInfo by viewModel.selectedToeicInfo.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = "戻る",
                tint = androidx.compose.ui.graphics.Color.Gray
            )
        }

        IconButton(
            onClick = { /* 捨てる処理を追加 */ },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "削除する",
                tint = androidx.compose.ui.graphics.Color.Gray
            )
        }

        Column(modifier = Modifier.padding(start = 16.dp)) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_64_dp)))
            if (toeicInfo != null) {
                Text(
                    text = "受験日: ${toeicInfo!!.date}",
                    fontSize = 20.sp
                )
                Text(
                    text = "リーディングスコア: ${toeicInfo!!.readingScore}",
                    fontSize = 20.sp
                )
                Text(
                    text = "リスニングスコア: ${toeicInfo!!.listeningScore}",
                    fontSize = 20.sp
                )
                Text(
                    text = "メモ: ${toeicInfo!!.memo}",
                    fontSize = 20.sp
                )
            } else {
                Text(
                    text = "データが見つかりませんでした。",
                    color = androidx.compose.ui.graphics.Color.Red
                )
            }
        }
    }
}

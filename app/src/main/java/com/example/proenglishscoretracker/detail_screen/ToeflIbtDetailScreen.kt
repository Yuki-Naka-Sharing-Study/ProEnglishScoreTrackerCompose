package com.example.proenglishscoretracker.detail_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proenglishscoretracker.R
import com.example.proenglishscoretracker.data.EnglishInfoViewModel

@Composable
fun ToeflIbtDetailScreen(
    toeflIbtId: String,
    viewModel: EnglishInfoViewModel,
    navController: NavController
) {
    // 画面表示時にデータを取得
    LaunchedEffect(toeflIbtId) {
        viewModel.loadToeflIbtInfoById(toeflIbtId)
    }

    val toeflIbtInfo by viewModel.selectedToeflIbtInfo.collectAsState()
    var showAlertDialog by rememberSaveable { mutableStateOf(false) }

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

        // 右上の編集アイコンと削除アイコン
        Row(
            modifier = Modifier.align(Alignment.TopEnd),
        ) {
            IconButton(
                modifier = Modifier.padding(16.dp),
                onClick = { showAlertDialog = true }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.delete),
                    contentDescription = "削除する",
                    tint = androidx.compose.ui.graphics.Color.Red
                )
            }
            IconButton(
                modifier = Modifier.padding(16.dp),
                onClick = { /* 編集処理を追加 */ }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.edit),
                    contentDescription = "編集する",
                    tint = androidx.compose.ui.graphics.Color.Gray
                )
            }
        }

        // 削除確認ダイアログ
        if (showAlertDialog) {
            AlertDialog(
                onDismissRequest = { showAlertDialog = false },
                title = { Text(text = "削除の確認") },
                text = { Text(text = "このTOEFL iBT データを削除しますか？") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteToeflIbtInfo(toeflIbtId)
                            showAlertDialog = false
                            navController.popBackStack()
                        }
                    ) {
                        Text(text = "削除")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAlertDialog = false }) {
                        Text(text = "キャンセル")
                    }
                }
            )
        }

        Column(modifier = Modifier.padding(start = 16.dp)) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_64_dp)))
            if (toeflIbtInfo != null) {
                Text(
                    text = "受験日: ${toeflIbtInfo!!.date}",
                    fontSize = 20.sp
                )
                Text(
                    text = "Overallスコア: ${toeflIbtInfo!!.overallScore}",
                    fontSize = 20.sp
                )
                Text(
                    text = "リーディングスコア: ${toeflIbtInfo!!.readingScore}",
                    fontSize = 20.sp
                )
                Text(
                    text = "リスニングスコア: ${toeflIbtInfo!!.listeningScore}",
                    fontSize = 20.sp
                )
                Text(
                    text = "ライティングスコア: ${toeflIbtInfo!!.writingScore}",
                    fontSize = 20.sp
                )
                Text(
                    text = "スピーキングスコア: ${toeflIbtInfo!!.speakingScore}",
                    fontSize = 20.sp
                )
                Text(
                    text = "メモ: ${toeflIbtInfo!!.memo}",
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

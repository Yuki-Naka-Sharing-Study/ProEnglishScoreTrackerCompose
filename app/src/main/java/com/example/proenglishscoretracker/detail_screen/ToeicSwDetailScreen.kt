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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proenglishscoretracker.R
import com.example.proenglishscoretracker.data.EnglishInfoViewModel

@Composable
fun ToeicSwDetailScreen(
    toeicSwId: String,
    viewModel: EnglishInfoViewModel,
    navController: NavController
) {
    // 画面表示時にデータを取得
    LaunchedEffect(toeicSwId, viewModel) {
        viewModel.loadToeicSwInfoById(toeicSwId)
    }

    val toeicSwInfo by viewModel.selectedToeicSwInfo.collectAsState()
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
                tint = Color.Gray
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
                    tint = Color.Red
                )
            }
            IconButton(
                modifier = Modifier.padding(16.dp),
                onClick = {
                    toeicSwInfo?.let {
                        navController.navigate("toeic_sw_edit/${it.id}")
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.edit),
                    contentDescription = "編集する",
                    tint = Color.Gray
                )
            }
        }

        // 削除確認ダイアログ
        if (showAlertDialog) {
            AlertDialog(
                onDismissRequest = { showAlertDialog = false },
                title = { Text(text = "削除の確認") },
                text = { Text(text = "このTOEIC SW データを削除しますか？") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteToeicSwInfo(toeicSwId)
                            showAlertDialog = false
                            navController.popBackStack()
                        }
                    ) {
                        Text(
                            text = "削除",
                            color = Color.Red
                        )
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
            if (toeicSwInfo != null) {
                Text(
                    text = "受験日: ${toeicSwInfo!!.date}",
                    fontSize = 20.sp
                )
                Text(
                    text = "ライティングスコア: ${toeicSwInfo!!.writingScore}",
                    fontSize = 20.sp
                )
                Text(
                    text = "スピーキングスコア: ${toeicSwInfo!!.speakingScore}",
                    fontSize = 20.sp
                )
                Text(
                    text = "メモ: ${toeicSwInfo!!.memo}",
                    fontSize = 20.sp
                )
            } else {
                Text(
                    text = "データが見つかりませんでした。",
                    color = Color.Red
                )
            }
        }
    }
}

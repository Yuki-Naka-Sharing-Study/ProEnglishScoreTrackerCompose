package com.example.proenglishscoretracker.detail_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.proenglishscoretracker.R
import com.example.proenglishscoretracker.data.EnglishInfoViewModel

@Composable
fun ToeicSwDetailScreen(
    toeicSwId: String,
    viewModel: EnglishInfoViewModel,
    navHostController: NavHostController
) {
    // 画面表示時にデータを取得
    LaunchedEffect(toeicSwId, viewModel) {
        viewModel.loadToeicSwInfoById(toeicSwId)
    }
    val toeicSwInfo by viewModel.selectedToeicSwInfo.collectAsState()
    var showAlertDialog by remember { mutableStateOf(false) }

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
                        navHostController.popBackStack()
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
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "TOEIC SW 詳細") },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "戻る",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showAlertDialog = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.delete),
                            contentDescription = "削除する",
                            tint = Color.Red
                        )
                    }
                    IconButton(
                        onClick = {
                            toeicSwInfo?.let {
                                navHostController.navigate("toeic_sw_edit/${it.id}")
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.edit),
                            contentDescription = "編集する",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (toeicSwInfo != null) {
                Row {
                    Text(text = "[受験日]", fontSize = 20.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = toeicSwInfo!!.date, fontSize = 20.sp)
                }
                Spacer(Modifier.height(16.dp))
                Row {
                    Text(text = "[Writingスコア]", fontSize = 20.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "${toeicSwInfo!!.writingScore}", fontSize = 20.sp)
                }
                Spacer(Modifier.height(16.dp))
                Row {
                    Text(text = "[Speakingスコア]", fontSize = 20.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "${toeicSwInfo!!.speakingScore}", fontSize = 20.sp)
                }
                Spacer(Modifier.height(16.dp))
                Text(text = "[メモ]", fontSize = 20.sp)
                Spacer(Modifier.height(16.dp))
                Row {
                    Spacer(Modifier.width(16.dp))
                    Text(text = toeicSwInfo!!.memo, fontSize = 20.sp)
                }
            } else {
                Text(text = "データが見つかりませんでした。", color = Color.Red)
            }
        }
    }
}

package com.example.proenglishscoretracker.detail_screen

import androidx.compose.foundation.layout.Box
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
fun EikenDetailScreen(
    eikenId: String,
    viewModel: EnglishInfoViewModel,
    navController: NavController
) {
    // 画面表示時にデータを取得
    LaunchedEffect(eikenId, viewModel) {
        viewModel.loadEikenInfoById(eikenId)
    }

    val eikenInfo by viewModel.selectedEikenInfo.collectAsState()
    var showAlertDialog by rememberSaveable { mutableStateOf(false) }

    // 削除確認ダイアログ
    if (showAlertDialog) {
        AlertDialog(
            onDismissRequest = { showAlertDialog = false },
            title = { Text(text = "削除の確認") },
            text = { Text(text = "この英検データを削除しますか？") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteEikenInfo(eikenId)
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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
                    eikenInfo?.let {
                        navController.navigate("eiken_edit/${it.id}")
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
        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_120_dp)))
            if (eikenInfo != null) {
                Row(

                ) {
                    Text(
                        text = "[受験日]",
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = eikenInfo!!.date,
                        fontSize = 20.sp
                    )
                }
                Spacer(Modifier.height(16.dp))
                Row(

                ) {
                    Text(
                        text = "[受験級]",
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = eikenInfo!!.grade,
                        fontSize = 20.sp
                    )
                }
                Spacer(Modifier.height(16.dp))
                Row(

                ) {
                    Text(
                        text = "[CSEスコア]",
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${eikenInfo!!.cseScore}",
                        fontSize = 20.sp
                    )
                }
                Spacer(Modifier.height(16.dp))
                Row(

                ) {
                    Text(
                        text = "[Readingスコア]",
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${eikenInfo!!.readingScore}",
                        fontSize = 20.sp
                    )
                }
                Spacer(Modifier.height(16.dp))
                Row(

                ) {
                    Text(
                        text = "[Listeningスコア]",
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${eikenInfo!!.listeningScore}",
                        fontSize = 20.sp
                    )
                }
                if (
                    eikenInfo!!.grade == "3級" ||
                    eikenInfo!!.grade == "準2級" ||
                    eikenInfo!!.grade == "2級" ||
                    eikenInfo!!.grade == "準1級" ||
                    eikenInfo!!.grade == "1級"
                ) {
                    Spacer(Modifier.height(16.dp))
                    Row(

                    ) {
                        Text(
                            text = "[Writingスコア]",
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "${eikenInfo!!.writingScore}",
                            fontSize = 20.sp
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    Row(

                    ) {
                        Text(
                            text = "[Speakingスコア]",
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "${eikenInfo!!.speakingScore}",
                            fontSize = 20.sp
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "[メモ]",
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(16.dp))
                Row {
                    Spacer(Modifier.width(16.dp))
                    Text(
                        text = eikenInfo!!.memo,
                        fontSize = 20.sp
                    )
                }
            } else {
                Text(
                    text = "データが見つかりませんでした。",
                    color = Color.Red
                )
            }
        }
    }
}

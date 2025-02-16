package com.example.proenglishscoretracker.individual_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController

import com.example.proenglishscoretracker.data.EnglishInfoViewModel
import com.example.proenglishscoretracker.data.EnglishTestInfo

@Composable
fun EikenIndividualScreen(
    viewModel: EnglishInfoViewModel,
    navController: NavController
) {
    val eikenInfoList = viewModel.eikenSecondInfo.collectAsState().value

    // 日付の降順でソート
    val sortedEikenInfoList = rememberSaveable(eikenInfoList) {
        eikenInfoList.sortedByDescending { it.date }
    }

    if (sortedEikenInfoList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.material.Text(
                text = "まだスコアが登録されていません。",
                fontSize = 18.sp,
                color = Color.Gray
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(items = sortedEikenInfoList) { eikenInfo ->
                EikenItem(
                    eikenInfo = eikenInfo,
                    viewModel = viewModel,
                    navController = navController
                )
            }
        }
    }
}

@Composable
private fun EikenItem(
    eikenInfo: EnglishTestInfo.EIKEN_SECOND,
    viewModel: EnglishInfoViewModel,
    navController: NavController
) {
    var showEditDialog by remember { mutableStateOf(false) }

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
                    navController.navigate("eiken_detail/${eikenInfo.id}")
                },
        ) {
            Column(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                androidx.compose.material.Text(text = "受験日: ${eikenInfo.date}")
                androidx.compose.material.Text(text = "リーディングスコア: ${eikenInfo.readingScore}")
                androidx.compose.material.Text(text = "リスニングスコア: ${eikenInfo.listeningScore}")
                androidx.compose.material.Text(text = "ライティングスコア: ${eikenInfo.writingScore}")
                androidx.compose.material.Text(text = "スピーキングスコア: ${eikenInfo.speakingScore}")
                androidx.compose.material.Text(text = "CSEスコア: ${eikenInfo.cseScore}")
                androidx.compose.material.Text(text = "メモ: ${eikenInfo.memo}")
            }
            Menu(
                modifier = Modifier.align(Alignment.CenterEnd),
                viewModel = viewModel,
                eikenInfo = eikenInfo,
                onEdit = { showEditDialog = true }
            )
        }
    }
    if (showEditDialog) {
        EditEikenDialog(
            eikenInfo = eikenInfo,
            viewModel = viewModel,
            onDismiss = { showEditDialog = false }
        )
    }
}

@Composable
private fun Menu(
    modifier: Modifier = Modifier,
    viewModel: EnglishInfoViewModel,
    eikenInfo: EnglishTestInfo.EIKEN_SECOND,
    onEdit: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.End
    ) {
        IconButton(
            onClick = {
                expanded = true
            }
        ) {
            Icon(
                Icons.Filled.MoreVert,
                contentDescription = "",
                tint = Color(0xFF9C27B0)
            )
        }
        DropdownMenu(
            modifier = Modifier
                .background(MaterialTheme.colors.background)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colors.onSurface),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onEdit()
                }
            ) {
                androidx.compose.material.Text(
                    text = "編集",
                    fontSize = 24.sp,
                    color = Color.Gray,
                )
            }
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    showDialog = true
                }
            ) {
                androidx.compose.material.Text(
                    text = "削除",
                    fontSize = 24.sp,
                    color = Color.Gray,
                )
            }
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { androidx.compose.material.Text("削除確認") },
            text = { androidx.compose.material.Text("このデータを削除しますか？") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteEikenValues(eikenInfo)
                        showDialog = false
                    }
                ) {
                    androidx.compose.material.Text("削除", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    androidx.compose.material.Text("キャンセル")
                }
            }
        )
    }
}

@Composable
private fun EditEikenDialog(
    eikenInfo: EnglishTestInfo.EIKEN_SECOND,
    viewModel: EnglishInfoViewModel,
    onDismiss: () -> Unit
) {
    var newDate by remember { mutableStateOf(eikenInfo.date) }
    var newReadingScore by remember { mutableStateOf(eikenInfo.readingScore.toString()) }
    var newListeningScore by remember { mutableStateOf(eikenInfo.listeningScore.toString()) }
    var newWritingScore by remember { mutableStateOf(eikenInfo.writingScore.toString()) }
    var newSpeakingScore by remember { mutableStateOf(eikenInfo.speakingScore.toString()) }
    var newCseScore by remember { mutableStateOf(eikenInfo.cseScore.toString()) }
    var newMemo by remember { mutableStateOf(eikenInfo.memo) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { androidx.compose.material.Text("英検データを編集") },
        text = {
            Column {
                OutlinedTextField(
                    value = newDate,
                    onValueChange = { newDate = it },
                    label = { androidx.compose.material.Text("受験日") }
                )
                OutlinedTextField(
                    value = newReadingScore,
                    onValueChange = { newReadingScore = it },
                    label = { androidx.compose.material.Text("リーディングスコア") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = newListeningScore,
                    onValueChange = { newListeningScore = it },
                    label = { androidx.compose.material.Text("リスニングスコア") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = newWritingScore,
                    onValueChange = { newWritingScore = it },
                    label = { androidx.compose.material.Text("ライティングスコア") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = newSpeakingScore,
                    onValueChange = { newSpeakingScore = it },
                    label = { androidx.compose.material.Text("スピーキングスコア") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = newCseScore,
                    onValueChange = { newCseScore = it },
                    label = { androidx.compose.material.Text("CSEスコア") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = newMemo,
                    onValueChange = { newMemo = it },
                    label = { androidx.compose.material.Text("メモ") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.updateEikenValues(
                        eikenInfo.copy(
                            date = newDate,
                            readingScore = newReadingScore.toIntOrNull() ?: eikenInfo.readingScore,
                            listeningScore = newListeningScore.toIntOrNull() ?: eikenInfo.listeningScore,
                            writingScore = newWritingScore.toIntOrNull() ?: eikenInfo.writingScore,
                            speakingScore = newSpeakingScore.toIntOrNull() ?: eikenInfo.speakingScore,
                            memo = newMemo
                        )
                    )
                    onDismiss()
                }
            ) {
                androidx.compose.material.Text("保存")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                androidx.compose.material.Text("キャンセル")
            }
        }
    )
}

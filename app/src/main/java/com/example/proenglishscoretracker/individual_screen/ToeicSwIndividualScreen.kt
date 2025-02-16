package com.example.proenglishscoretracker.individual_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.example.proenglishscoretracker.data.EnglishInfoViewModel
import com.example.proenglishscoretracker.data.EnglishTestInfo
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ToeicSwIndividualScreen(
    viewModel: EnglishInfoViewModel,
    navController: NavController
) {
    val toeicSwInfoList = viewModel.toeicSwInfo.collectAsState().value

    // 日付の降順でソート
    val sortedToeicSwInfoList = rememberSaveable(toeicSwInfoList) {
        toeicSwInfoList.sortedByDescending { it.date }
    }

    if (sortedToeicSwInfoList.isEmpty()) {
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
            items(items = sortedToeicSwInfoList) { toeicSwInfo ->
                ToeicSwItem(
                    toeicSwInfo = toeicSwInfo,
                    viewModel = viewModel,
                    navController = navController
                )
            }
        }
    }
}
@Composable
private fun ToeicSwItem(
    toeicSwInfo: EnglishTestInfo.TOEIC_SW,
    viewModel: EnglishInfoViewModel,
    navController: NavController
) {
    var showEditDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                // 詳細画面に遷移
                navController.navigate("toeic_sw_detail/${toeicSwInfo.id}")
            },
        elevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text(text = "受験日: ${toeicSwInfo.date}")
                Text(text = "Writingスコア: ${toeicSwInfo.writingScore}")
                Text(text = "Speakingスコア: ${toeicSwInfo.speakingScore}")
                Text(text = "メモ: ${toeicSwInfo.memo}")
            }
            Menu(
                modifier = Modifier.align(Alignment.CenterEnd),
                viewModel = viewModel,
                toeicSwInfo = toeicSwInfo,
                onEdit = { showEditDialog = true }
            )
        }
    }
    if (showEditDialog) {
        EditToeicSwDialog(
            toeicSwInfo = toeicSwInfo,
            viewModel = viewModel,
            onDismiss = { showEditDialog = false }
        )
    }
}
@Composable
private fun Menu(
    modifier: Modifier = Modifier,
    viewModel: EnglishInfoViewModel,
    toeicSwInfo: EnglishTestInfo.TOEIC_SW,
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
                Text(
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
                Text(
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
            title = { Text("削除確認") },
            text = { Text("このデータを削除しますか？") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteToeicSwValues(toeicSwInfo)
                        showDialog = false
                    }
                ) {
                    Text("削除", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("キャンセル")
                }
            }
        )
    }
}

@Composable
private fun EditToeicSwDialog(
    toeicSwInfo: EnglishTestInfo.TOEIC_SW,
    viewModel: EnglishInfoViewModel,
    onDismiss: () -> Unit
) {
    var newDate by remember { mutableStateOf(toeicSwInfo.date) }
    var newWritingScore by remember { mutableStateOf(toeicSwInfo.writingScore.toString()) }
    var newSpeakingScore by remember { mutableStateOf(toeicSwInfo.speakingScore.toString()) }
    var newMemo by remember { mutableStateOf(toeicSwInfo.memo) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("TOEICデータを編集") },
        text = {
            Column {
                OutlinedTextField(
                    value = newDate,
                    onValueChange = { newDate = it },
                    label = { Text("受験日") }
                )
                OutlinedTextField(
                    value = newWritingScore,
                    onValueChange = { newWritingScore = it },
                    label = { Text("Writingスコア") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = newSpeakingScore,
                    onValueChange = { newSpeakingScore = it },
                    label = { Text("Speakingスコア") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = newMemo,
                    onValueChange = { newMemo = it },
                    label = { Text("メモ") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.updateToeicSwValues(
                        toeicSwInfo.copy(
                            date = newDate,
                            writingScore = newWritingScore.toIntOrNull() ?: toeicSwInfo.writingScore,
                            speakingScore = newSpeakingScore.toIntOrNull() ?: toeicSwInfo.speakingScore,
                            memo = newMemo
                        )
                    )
                    onDismiss()
                }
            ) {
                Text("保存")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("キャンセル")
            }
        }
    )
}

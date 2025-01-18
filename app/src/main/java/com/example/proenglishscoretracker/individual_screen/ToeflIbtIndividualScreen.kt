package com.example.proenglishscoretracker.individual_screen

import androidx.compose.foundation.background
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp

@Composable
fun ToeflIbtIndividualScreen(viewModel: EnglishInfoViewModel) {
    val toeflIbtInfoList = viewModel.toeflIbtInfo.collectAsState().value

    if (toeflIbtInfoList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "まだスコアが登録されていません。", fontSize = 18.sp, color = Color.Gray)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(items = toeflIbtInfoList) { toeflIbtInfo ->
                ToeflIbtInfo(toeflIbtInfo = toeflIbtInfo, viewModel)
            }
        }
    }
}
@Composable
private fun ToeflIbtInfo(
    toeflIbtInfo: EnglishTestInfo.TOEFL,
    viewModel: EnglishInfoViewModel
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
        ) {
            Column(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text(text = "受験日: ${toeflIbtInfo.date}")
                Text(text = "CSEスコア: ${toeflIbtInfo.overallScore}")
                Text(text = "Readingスコア: ${toeflIbtInfo.readingScore}")
                Text(text = "Listeningスコア: ${toeflIbtInfo.listeningScore}")
                Text(text = "Writingスコア: ${toeflIbtInfo.writingScore}")
                Text(text = "Speakingスコア: ${toeflIbtInfo.speakingScore}")
                Text(text = "メモ: ${toeflIbtInfo.memo}")
            }
            Menu(
                modifier = Modifier.align(Alignment.CenterEnd),
                viewModel = viewModel,
                toeflIbtInfo = toeflIbtInfo,
                onEdit = { showEditDialog = true }
            )
        }
    }
    if (showEditDialog) {
        EditToeflIbtDialog(
            toeflIbtInfo = toeflIbtInfo,
            viewModel = viewModel,
            onDismiss = { showEditDialog = false }
        )
    }
}
@Composable
private fun Menu(
    modifier: Modifier = Modifier,
    viewModel: EnglishInfoViewModel,
    toeflIbtInfo: EnglishTestInfo.TOEFL,
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
                        viewModel.deleteToeflIbtValues(toeflIbtInfo)
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
private fun EditToeflIbtDialog(
    toeflIbtInfo: EnglishTestInfo.TOEFL,
    viewModel: EnglishInfoViewModel,
    onDismiss: () -> Unit
) {
    var newDate by remember { mutableStateOf(toeflIbtInfo.date) }
    var newOverallScore by remember { mutableStateOf(toeflIbtInfo.overallScore.toString()) }
    var newReadingScore by remember { mutableStateOf(toeflIbtInfo.readingScore.toString()) }
    var newListeningScore by remember { mutableStateOf(toeflIbtInfo.listeningScore.toString()) }
    var newWritingScore by remember { mutableStateOf(toeflIbtInfo.writingScore.toString()) }
    var newSpeakingScore by remember { mutableStateOf(toeflIbtInfo.speakingScore.toString()) }
    var newMemo by remember { mutableStateOf(toeflIbtInfo.memo) }

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
                    value = newOverallScore,
                    onValueChange = { newOverallScore = it },
                    label = { Text("Overallスコア") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = newReadingScore,
                    onValueChange = { newReadingScore = it },
                    label = { Text("Readingスコア") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = newListeningScore,
                    onValueChange = { newListeningScore = it },
                    label = { Text("Listeningスコア") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
                    viewModel.updateToeflIbtValues(
                        toeflIbtInfo.copy(
                            date = newDate,
                            overallScore = newOverallScore.toIntOrNull() ?: toeflIbtInfo.overallScore,
                            readingScore = newReadingScore.toIntOrNull() ?: toeflIbtInfo.readingScore,
                            listeningScore = newListeningScore.toIntOrNull() ?: toeflIbtInfo.listeningScore,
                            writingScore = newWritingScore.toIntOrNull() ?: toeflIbtInfo.writingScore,
                            speakingScore = newSpeakingScore.toIntOrNull() ?: toeflIbtInfo.speakingScore,
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

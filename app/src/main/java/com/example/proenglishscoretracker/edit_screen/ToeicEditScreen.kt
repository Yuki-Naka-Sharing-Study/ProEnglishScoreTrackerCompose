package com.example.proenglishscoretracker.edit_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proenglishscoretracker.data.EnglishInfoViewModel
import com.example.proenglishscoretracker.data.EnglishTestInfo
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ToeicEditScreen(
    toeicInfo: EnglishTestInfo.TOEIC,
    viewModel: EnglishInfoViewModel,
    navController: NavController
) {
    var date by remember { mutableStateOf(toeicInfo.date) }
    var readingScore by remember { mutableStateOf(toeicInfo.readingScore.toString()) }
    var listeningScore by remember { mutableStateOf(toeicInfo.listeningScore.toString()) }
    var memo by remember { mutableStateOf(toeicInfo.memo) }

    // 日付の有効性とエラーメッセージの管理
    var errorMessage by remember { mutableStateOf<String?>(null) } // エラーメッセージを保持
    var isDateValid by remember { mutableStateOf(true) } // 日付の有効性を保持
    var isFocused by remember { mutableStateOf(true) } // フォーカスの有無を管理

    // FDate型チェックと日付が有効か確認する関数
    fun isValidDate(date: String): Boolean {
        return try {
            // "yyyy-MM-dd" フォーマットで日付を検証
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.JAPAN)
            dateFormat.isLenient = false
            dateFormat.parse(date) != null
        } catch (e: ParseException) {
            false
        }
    }

    // 日付が変更された場合の処理
    fun onDateChange(newDate: String) {
        date = newDate
        // 日付が有効かチェック
        isDateValid = isValidDate(newDate)
        errorMessage = if (isDateValid) null else "無効な日付です。"
    }

    // フォーカスが外れたときの処理
    fun onFocusChange(focused: Boolean) {
        if (!focused && !isDateValid) {
            errorMessage = "無効な日付です。"
        }
        isFocused = focused
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TOEIC データ編集") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "戻る"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (isDateValid) {
                                viewModel.updateToeicInfo(
                                    EnglishTestInfo.TOEIC(
                                        id = toeicInfo.id,
                                        date = date,
                                        readingScore = readingScore.toIntOrNull() ?: 0,
                                        listeningScore = listeningScore.toIntOrNull() ?: 0,
                                        memo = memo
                                    )
                                )
                                navController.popBackStack()
                            }
                        }
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "保存"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = date,
                onValueChange = { onDateChange(it) },
                label = { Text("受験日") },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { onFocusChange(it.isFocused) },
                isError = !isDateValid // 日付が無効な場合はエラーを表示
            )
            // 日付のエラーメッセージ
            if (!isDateValid) {
                Text(
                    text = errorMessage ?: "",
                    color = Color.Red,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            OutlinedTextField(
                value = readingScore,
                onValueChange = { readingScore = it },
                label = { Text("リーディングスコア") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = listeningScore,
                onValueChange = { listeningScore = it },
                label = { Text("リスニングスコア") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = memo,
                onValueChange = { memo = it },
                label = { Text("メモ") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

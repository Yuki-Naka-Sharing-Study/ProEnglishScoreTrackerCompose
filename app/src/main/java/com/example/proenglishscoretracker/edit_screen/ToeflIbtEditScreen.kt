package com.example.proenglishscoretracker.edit_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proenglishscoretracker.R
import com.example.proenglishscoretracker.data.EnglishInfoViewModel
import com.example.proenglishscoretracker.data.EnglishTestInfo
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ToeflIbtEditScreen(
    toeflIbtInfo: EnglishTestInfo.TOEFL,
    viewModel: EnglishInfoViewModel,
    navHostController: NavHostController
) {
    var date by remember { mutableStateOf(toeflIbtInfo.date) }
    var readingScore by remember { mutableStateOf(toeflIbtInfo.readingScore.toString()) }
    var listeningScore by remember { mutableStateOf(toeflIbtInfo.listeningScore.toString()) }
    var writingScore by remember { mutableStateOf(toeflIbtInfo.writingScore.toString()) }
    var speakingScore by remember { mutableStateOf(toeflIbtInfo.speakingScore.toString()) }
    var memo by remember { mutableStateOf(toeflIbtInfo.memo) }

    // 日付の有効性とエラーメッセージの管理
    var dateErrorMessage by remember { mutableStateOf<String?>(null) } // エラーメッセージを保持
    var isDateValid by remember { mutableStateOf(true) } // 日付の有効性を保持

    var readingErrorMessage by remember { mutableStateOf<String?>(null) }
    var listeningErrorMessage by remember { mutableStateOf<String?>(null) }
    var writingErrorMessage by remember { mutableStateOf<String?>(null) }
    var speakingErrorMessage by remember { mutableStateOf<String?>(null) }

    fun validateReadingScore(): Boolean {
        val score = readingScore.toIntOrNull()
        return when {
            score == null -> false
            score > 30 -> {
                readingErrorMessage = "Readingスコアの上限は30です。"
                false
            }
            else -> {
                readingErrorMessage = null
                true
            }
        }
    }

    fun validateListeningScore(): Boolean {
        val score = listeningScore.toIntOrNull()
        return when {
            score == null -> false
            score > 30 -> {
                listeningErrorMessage = "Listeningスコアの上限は30です。"
                false
            }
            else -> {
                listeningErrorMessage = null
                true
            }
        }
    }

    fun validateWritingScore(): Boolean {
        val score = writingScore.toIntOrNull()
        return when {
            score == null -> false
            score > 30 -> {
                writingErrorMessage = "Writingスコアの上限は30です。"
                false
            }
            else -> {
                writingErrorMessage = null
                true
            }
        }
    }

    fun validateSpeakingScore(): Boolean {
        val score = speakingScore.toIntOrNull()
        return when {
            score == null -> false
            score > 30 -> {
                speakingErrorMessage = "Speakingスコアの上限は30です。"
                false
            }
            else -> {
                speakingErrorMessage = null
                true
            }
        }
    }

    val isFormValid = isDateValid &&
            validateReadingScore() &&
            validateListeningScore() &&
            validateWritingScore() &&
            validateSpeakingScore()

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
        dateErrorMessage = if (isDateValid) null else "無効な日付です。"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TOEFL iBT データ編集") },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "戻る"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (isFormValid) {
                                viewModel.updateToeflIbtInfo(
                                    EnglishTestInfo.TOEFL(
                                        id = toeflIbtInfo.id,
                                        date = date,
                                        readingScore = readingScore.toIntOrNull() ?: 0,
                                        listeningScore = listeningScore.toIntOrNull() ?: 0,
                                        writingScore = writingScore.toIntOrNull() ?: 0,
                                        speakingScore = speakingScore.toIntOrNull() ?: 0,
                                        overallScore = readingScore.toInt() +
                                                listeningScore.toInt() +
                                                writingScore.toInt() +
                                                speakingScore.toInt(),
                                        memo = memo
                                    )
                                )
                                navHostController.popBackStack()
                            }
                        },
                        enabled = isFormValid
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
        val focusManager = LocalFocusManager.current
        val interactionSource = remember { MutableInteractionSource() }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .clickable(interactionSource = interactionSource, indication = null) {
                    focusManager.clearFocus()
                }
        ){
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
                    modifier = Modifier.fillMaxWidth(),
                    isError = !isDateValid
                )
                if (!isDateValid) {
                    Text(text = dateErrorMessage ?: "", color = Color.Red)
                }
                OutlinedTextField(
                    value = readingScore,
                    onValueChange = { readingScore = it },
                    label = { Text("Readingスコア") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    isError = readingErrorMessage != null
                )
                if (readingErrorMessage != null) {
                    Text(text = readingErrorMessage ?: "", color = Color.Red)
                }
                OutlinedTextField(
                    value = listeningScore,
                    onValueChange = { listeningScore = it },
                    label = { Text("Listeningスコア") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    isError = listeningErrorMessage != null
                )
                if (listeningErrorMessage != null) {
                    Text(text = listeningErrorMessage ?: "", color = Color.Red)
                }
                OutlinedTextField(
                    value = writingScore,
                    onValueChange = { writingScore = it },
                    label = { Text("Writingスコア") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    isError = writingErrorMessage != null
                )
                if (writingErrorMessage != null) {
                    Text(text = writingErrorMessage ?: "", color = Color.Red)
                }
                OutlinedTextField(
                    value = speakingScore,
                    onValueChange = { speakingScore = it },
                    label = { Text("Speakingスコア") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    isError = speakingErrorMessage != null
                )
                if (speakingErrorMessage != null) {
                    Text(text = speakingErrorMessage ?: "", color = Color.Red)
                }
                OutlinedTextField(
                    value = memo,
                    onValueChange = { memo = it },
                    label = { Text("メモ") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(id = R.dimen.memo_height_dp))
                )
            }
        }
    }
}

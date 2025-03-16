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
fun IeltsEditScreen(
    ieltsInfo: EnglishTestInfo.IELTS,
    viewModel: EnglishInfoViewModel,
    navHostController: NavHostController
) {
    var date by remember { mutableStateOf(ieltsInfo.date) }
    var readingScore by remember { mutableStateOf(ieltsInfo.readingScore.toString()) }
    var listeningScore by remember { mutableStateOf(ieltsInfo.listeningScore.toString()) }
    var writingScore by remember { mutableStateOf(ieltsInfo.writingScore.toString()) }
    var speakingScore by remember { mutableStateOf(ieltsInfo.speakingScore.toString()) }
    var memo by remember { mutableStateOf(ieltsInfo.memo) }

    // エラーメッセージを管理
    var dateErrorMessage by remember { mutableStateOf<String?>(null) }
    var isDateValid by remember { mutableStateOf(true) }

    var readingErrorMessage by remember { mutableStateOf<String?>(null) }
    var listeningErrorMessage by remember { mutableStateOf<String?>(null) }
    var writingErrorMessage by remember { mutableStateOf<String?>(null) }
    var speakingErrorMessage by remember { mutableStateOf<String?>(null) }

    // 0.5の倍数チェックを行う関数
    fun isHalfMultiple(score: Float?): Boolean {
        return score != null && (score * 2) % 1 == 0f
    }

    // スコアのバリデーションを行う関数（上限エラーと0.5倍数エラー）
    fun validateReadingScore(): Boolean {
        val score = readingScore.toFloatOrNull()
        return if (score == null) {
            false
        } else {
            readingErrorMessage = when {
                score > 9.0 -> "Readingスコアが上限を超えています。"
                !isHalfMultiple(score) -> "Readingスコアは0.5の倍数でなければなりません。"
                else -> null
            }
            readingErrorMessage == null
        }
    }

    fun validateListeningScore(): Boolean {
        val score = listeningScore.toFloatOrNull()
        return if (score == null) {
            false
        } else {
            listeningErrorMessage = when {
                score > 9.0 -> "Listeningスコアが上限を超えています。"
                !isHalfMultiple(score) -> "Listeningスコアは0.5の倍数でなければなりません。"
                else -> null
            }
            listeningErrorMessage == null
        }
    }

    fun validateWritingScore(): Boolean {
        val score = writingScore.toFloatOrNull()
        return if (score == null) {
            false
        } else {
            writingErrorMessage = when {
                score > 9.0 -> "Writingスコアが上限を超えています。"
                !isHalfMultiple(score) -> "Writingスコアは0.5の倍数でなければなりません。"
                else -> null
            }
            writingErrorMessage == null
        }
    }

    fun validateSpeakingScore(): Boolean {
        val score = speakingScore.toFloatOrNull()
        return if (score == null) {
            false
        } else {
            speakingErrorMessage = when {
                score > 9.0 -> "Speakingスコアが上限を超えています。"
                !isHalfMultiple(score) -> "Speakingスコアは0.5の倍数でなければなりません。"
                else -> null
            }
            speakingErrorMessage == null
        }
    }

    val isFormValid = isDateValid &&
            validateReadingScore() &&
            validateListeningScore() &&
            validateWritingScore() &&
            validateSpeakingScore()

    // 日付検証関数
    fun isValidDate(date: String): Boolean {
        return try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.JAPAN)
            dateFormat.isLenient = false
            dateFormat.parse(date) != null
        } catch (e: ParseException) {
            false
        }
    }

    fun onDateChange(newDate: String) {
        date = newDate
        isDateValid = isValidDate(newDate)
        dateErrorMessage = if (isDateValid) null else "無効な日付です。"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("IELTS データ編集") },
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
                                viewModel.updateIeltsInfo(
                                    EnglishTestInfo.IELTS(
                                        id = ieltsInfo.id,
                                        date = date,
                                        readingScore = readingScore.toFloatOrNull() ?: 0.0F,
                                        listeningScore = listeningScore.toFloatOrNull() ?: 0.0F,
                                        writingScore = writingScore.toFloatOrNull() ?: 0.0F,
                                        speakingScore = speakingScore.toFloatOrNull() ?: 0.0F,
                                        overallScore = readingScore.toFloat() +
                                                listeningScore.toFloat() +
                                                writingScore.toFloat() +
                                                speakingScore.toFloat(),
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

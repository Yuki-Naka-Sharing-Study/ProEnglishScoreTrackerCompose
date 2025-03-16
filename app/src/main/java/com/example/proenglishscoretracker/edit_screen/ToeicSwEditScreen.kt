package com.example.proenglishscoretracker.edit_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proenglishscoretracker.data.EnglishInfoViewModel
import com.example.proenglishscoretracker.data.EnglishTestInfo
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ToeicSwEditScreen(
    toeicSwInfo: EnglishTestInfo.TOEIC_SW,
    viewModel: EnglishInfoViewModel,
    navHostController: NavHostController
) {
    var date by remember { mutableStateOf(toeicSwInfo.date) }
    var writingScore by remember { mutableStateOf(toeicSwInfo.writingScore.toString()) }
    var speakingScore by remember { mutableStateOf(toeicSwInfo.speakingScore.toString()) }
    var memo by remember { mutableStateOf(toeicSwInfo.memo) }

    var dateErrorMessage by remember { mutableStateOf<String?>(null) }
    var isDateValid by remember { mutableStateOf(true) }

    var writingErrorMessage by remember { mutableStateOf<String?>(null) }
    var speakingErrorMessage by remember { mutableStateOf<String?>(null) }

    var isWritingFocused by remember { mutableStateOf(false) }
    var isSpeakingFocused by remember { mutableStateOf(false) }

    fun validateWritingScore(): Boolean {
        val score = writingScore.toIntOrNull()
        return when {
            score == null -> false
            score > 200 -> {
                writingErrorMessage = "Writingスコアが上限を超えています。"
                false
            }
            !isWritingFocused && score % 5 != 0 -> {
                writingErrorMessage = "Writingスコアは5の倍数のみでしか入力できません。"
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
            score > 200 -> {
                speakingErrorMessage = "Speakingスコアが上限を超えています。"
                false
            }
            !isSpeakingFocused && score % 5 != 0 -> {
                speakingErrorMessage = "Speakingスコアは5の倍数のみでしか入力できません。"
                false
            }
            else -> {
                speakingErrorMessage = null
                true
            }
        }
    }

    val isFormValid = isDateValid && validateWritingScore() && validateSpeakingScore()

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
                title = { Text("TOEIC SW データ編集") },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (isFormValid) {
                                viewModel.updateToeicSwInfo(
                                    EnglishTestInfo.TOEIC_SW(
                                        id = toeicSwInfo.id,
                                        date = date,
                                        writingScore = writingScore.toIntOrNull() ?: 0,
                                        speakingScore = speakingScore.toIntOrNull() ?: 0,
                                        memo = memo
                                    )
                                )
                                navHostController.popBackStack()
                            }
                        },
                        enabled = isFormValid
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "保存")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize()
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
                value = writingScore,
                onValueChange = { writingScore = it },
                label = { Text("Writingスコア") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
                    .onFocusChanged { isWritingFocused = it.isFocused },
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
                modifier = Modifier.fillMaxWidth()
                    .onFocusChanged { isSpeakingFocused = it.isFocused },
                isError = speakingErrorMessage != null
            )
            if (speakingErrorMessage != null) {
                Text(text = speakingErrorMessage ?: "", color = Color.Red)
            }
            OutlinedTextField(
                value = memo,
                onValueChange = { memo = it },
                label = { Text("メモ") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

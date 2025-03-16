package com.example.proenglishscoretracker.edit_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
fun ToeicEditScreen(
    toeicInfo: EnglishTestInfo.TOEIC,
    viewModel: EnglishInfoViewModel,
    navHostController: NavHostController
) {
    var date by remember { mutableStateOf(toeicInfo.date) }
    var readingScore by remember { mutableStateOf(toeicInfo.readingScore.toString()) }
    var listeningScore by remember { mutableStateOf(toeicInfo.listeningScore.toString()) }
    var memo by remember { mutableStateOf(toeicInfo.memo) }

    var dateErrorMessage by remember { mutableStateOf<String?>(null) }
    var isDateValid by remember { mutableStateOf(true) }

    var readingErrorMessage by remember { mutableStateOf<String?>(null) }
    var listeningErrorMessage by remember { mutableStateOf<String?>(null) }

    var isReadingFocused by remember { mutableStateOf(false) }
    var isListeningFocused by remember { mutableStateOf(false) }

    fun validateReadingScore(): Boolean {
        val score = readingScore.toIntOrNull()
        return when {
            score == null -> false
            score > 495 -> {
                readingErrorMessage = "Readingスコアが上限を超えています。"
                false
            }
            !isReadingFocused && score % 5 != 0 -> {
                readingErrorMessage = "Readingスコアは5の倍数のみでしか入力できません。"
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
            score > 495 -> {
                listeningErrorMessage = "Listeningスコアが上限を超えています。"
                false
            }
            !isListeningFocused && score % 5 != 0 -> {
                listeningErrorMessage = "Listeningスコアは5の倍数のみでしか入力できません。"
                false
            }
            else -> {
                listeningErrorMessage = null
                true
            }
        }
    }

    val isFormValid = isDateValid && validateReadingScore() && validateListeningScore()

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
                title = { Text("TOEIC データ編集") },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (isFormValid) {
                                viewModel.updateToeicInfo(
                                    EnglishTestInfo.TOEIC(
                                        id = toeicInfo.id,
                                        date = date,
                                        readingScore = readingScore.toIntOrNull() ?: 0,
                                        listeningScore = listeningScore.toIntOrNull() ?: 0,
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
                value = readingScore,
                onValueChange = { readingScore = it },
                label = { Text("Readingスコア") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
                    .onFocusChanged { isReadingFocused = it.isFocused },
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
                modifier = Modifier.fillMaxWidth()
                    .onFocusChanged { isListeningFocused = it.isFocused },
                isError = listeningErrorMessage != null
            )
            if (listeningErrorMessage != null) {
                Text(text = listeningErrorMessage ?: "", color = Color.Red)
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

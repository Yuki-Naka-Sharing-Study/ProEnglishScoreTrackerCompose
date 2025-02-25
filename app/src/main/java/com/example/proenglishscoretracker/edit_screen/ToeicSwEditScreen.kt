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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.proenglishscoretracker.data.EnglishInfoViewModel
import com.example.proenglishscoretracker.data.EnglishTestInfo

@Composable
fun ToeicSwEditScreen(
    toeicSwInfo: EnglishTestInfo.TOEIC_SW,
    viewModel: EnglishInfoViewModel,
    navController: NavController
) {
    var date by remember { mutableStateOf(toeicSwInfo.date) }
    var writingScore by remember { mutableStateOf(toeicSwInfo.writingScore.toString()) }
    var speakingScore by remember { mutableStateOf(toeicSwInfo.speakingScore.toString()) }
    var memo by remember { mutableStateOf(toeicSwInfo.memo) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TOEIC SW 情報編集") },
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
                            viewModel.updateToeicSwInfo(
                                EnglishTestInfo.TOEIC_SW(
                                    id = toeicSwInfo.id,
                                    date = date,
                                    writingScore = writingScore.toIntOrNull() ?: 0,
                                    speakingScore = speakingScore.toIntOrNull() ?: 0,
                                    memo = memo
                                )
                            )
                            navController.popBackStack()
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
                onValueChange = { date = it },
                label = { Text("受験日") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = writingScore,
                onValueChange = { writingScore = it },
                label = { Text("ライティングスコア") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = speakingScore,
                onValueChange = { speakingScore = it },
                label = { Text("スピーキングスコア") },
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

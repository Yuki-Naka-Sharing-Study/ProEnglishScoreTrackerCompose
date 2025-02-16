package com.example.proenglishscoretracker.detail_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.proenglishscoretracker.data.EnglishInfoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToeicDetailScreen(
    toeicId: String,
    viewModel: EnglishInfoViewModel,
    navController: NavController
) {
    // 画面表示時にデータを取得
    LaunchedEffect(toeicId) {
        viewModel.loadToeicInfoById(toeicId)
    }

    // selectedToeicInfoを監視
    val toeicInfo by viewModel.selectedToeicInfo.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text = "戻る",
                    maxLines = 1,
                    color = androidx.compose.ui.graphics.Color.White
                ) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "戻る")
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            if (toeicInfo != null) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "受験日: ${toeicInfo!!.date}", fontSize = 20.sp)
                    Text(text = "リーディングスコア: ${toeicInfo!!.readingScore}", fontSize = 20.sp)
                    Text(text = "リスニングスコア: ${toeicInfo!!.listeningScore}", fontSize = 20.sp)
                    Text(text = "メモ: ${toeicInfo!!.memo}", fontSize = 20.sp)
                }
            } else {
                Text(
                    text = "データが見つかりませんでした。",
                    color = androidx.compose.ui.graphics.Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

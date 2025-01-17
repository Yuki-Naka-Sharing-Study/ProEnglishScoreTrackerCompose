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
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import androidx.compose.ui.unit.sp

@Composable
fun ToeicIndividualScreen(viewModel: EnglishInfoViewModel) {
    val toeicInfoList = viewModel.toeicInfo.collectAsState().value
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(items = toeicInfoList) { toeicInfo ->
            ToeicItem(toeicInfo = toeicInfo)
        }
    }
}

@Composable
fun ToeicItem(toeicInfo: EnglishTestInfo.TOEIC) {
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
                Text(text = "受験日: ${toeicInfo.date}")
                Text(text = "Readingスコア: ${toeicInfo.readingScore}")
                Text(text = "Listeningスコア: ${toeicInfo.listeningScore}")
                Text(text = "メモ: ${toeicInfo.memo}")
            }
            Menu(
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}

@Composable
private fun Menu(modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }

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
                    // データを編集する
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
                    // データを削除する
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
}

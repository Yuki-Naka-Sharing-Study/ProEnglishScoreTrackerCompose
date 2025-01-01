package com.example.proenglishscoretracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

typealias ComposableFun = @Composable () -> Unit

sealed class TabItem(var icon: Int,var title: String, var screen: ComposableFun){
    object TOEIC : TabItem(R.drawable.reading, "TOEIC", { KariToeicScreen() })
    object TOEIC_SW : TabItem(R.drawable.listening, "TOEIC SW", { KariToeicSwScreen() })
    object EIKEN : TabItem(R.drawable.speaking, "英検", { KariEikenScreen() })
    object TOEFL_IBT : TabItem(R.drawable.speaking, "TOEFL iBT", { KariToeflIbtScreen() })
    object IELTS : TabItem(R.drawable.speaking, "IELTS", { KariIeltsScreen() })
}

@Composable
private fun KariToeicScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "TOEIC",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 30.sp
        )
    }
}

@Composable
private fun KariToeicSwScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "TOEIC SW",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 30.sp
        )
    }
}

@Composable
private fun KariEikenScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "英検",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 30.sp
        )
    }
}

@Composable
private fun KariToeflIbtScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "TOEFL iBT",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 30.sp
        )
    }
}

@Composable
private fun KariIeltsScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "IELTS",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 30.sp
        )
    }
}

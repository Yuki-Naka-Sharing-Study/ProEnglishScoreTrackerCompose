package com.example.proenglishscoretracker.select_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proenglishscoretracker.TapView

@Composable
fun SelectToeicSwScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        ToeicSwIndividualView(
            navController,
            modifier = Modifier.weight(1f),
            buttonColor = Color.Red
        )
        ToeicSwChartView(
            navController,
            modifier = Modifier.weight(1f),
            buttonColor = Color.Blue
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectToeicSwScreenPreview() {
    SelectToeicSwScreen(navController = rememberNavController())
}

@Composable
private fun ToeicSwIndividualView(navController: NavController, modifier: Modifier = Modifier, buttonColor: Color) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TapView("TOEIC SW　個別記録画面", buttonColor) { navController.navigate("toeicSwIndividualScreen") }
    }
}

@Composable
private fun ToeicSwChartView(navController: NavController, modifier: Modifier = Modifier, buttonColor: Color) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TapView("TOEIC SW　チャート記録画面", buttonColor) { navController.navigate("toeicSwChartScreen") }
    }
}

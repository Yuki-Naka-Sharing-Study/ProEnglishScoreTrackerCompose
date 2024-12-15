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
fun SelectEikenIchijiScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        EikenIchijiIndividualView(
            navController,
            modifier = Modifier.weight(1f),
            buttonColor = Color.Red
        )
        EikenIchijiChartView(
            navController,
            modifier = Modifier.weight(1f),
            buttonColor = Color.Blue
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectEikenIchijiScreenPreview() {
    SelectEikenIchijiScreen(navController = rememberNavController())
}

@Composable
private fun EikenIchijiIndividualView(navController: NavController, modifier: Modifier = Modifier, buttonColor: Color) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TapView("英検一次　個別記録画面", buttonColor) { navController.navigate("eikenIchijiIndividualScreen") }
    }
}

@Composable
private fun EikenIchijiChartView(navController: NavController, modifier: Modifier = Modifier, buttonColor: Color) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TapView("英検一次　チャート記録画面", buttonColor) { navController.navigate("eikenIchijiChartScreen") }
    }
}

package com.example.proenglishscoretracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun SelectEikenNijiScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        EikenNijiIndividualView(
            navController,
            modifier = Modifier.weight(1f),
            buttonColor = Color.Red
        )
        EikenNijiChartView(
            navController,
            modifier = Modifier.weight(1f),
            buttonColor = Color.Blue
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectEikenNijiScreenPreview() {
    SelectEikenNijiScreen(navController = rememberNavController())
}

@Composable
private fun EikenNijiIndividualView(navController: NavController, modifier: Modifier = Modifier, buttonColor: Color) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TapView("英検二次　個別記録画面", buttonColor) { navController.navigate("eikenNijiIndividualScreen") }
    }
}

@Composable
private fun EikenNijiChartView(navController: NavController, modifier: Modifier = Modifier, buttonColor: Color) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TapView("英検二次　チャート記録画面", buttonColor) { navController.navigate("eikenNijiChartScreen") }
    }
}

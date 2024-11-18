package com.example.proenglishscoretracker

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.ui.platform.ComposeView
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

class SelectIeltsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val composeView = ComposeView(requireContext()).apply {
            setContent {
                SelectIeltsScreen(
                    navController = TODO()
                )
            }
        }
        return composeView
    }
}

@Composable
fun SelectIeltsScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        IeltsIndividualView(
            navController,
            modifier = Modifier.weight(1f),
            buttonColor = Color.Red
        )
        IeltsChartView(
            navController,
            modifier = Modifier.weight(1f),
            buttonColor = Color.Blue
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectIeltsScreenPreview() {
    SelectIeltsScreen(navController = rememberNavController())
}

@Composable
private fun IeltsIndividualView(navController: NavController, modifier: Modifier = Modifier, buttonColor: Color) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TapView("IELTS　個別記録画面", buttonColor) { navController.navigate("toeflIbtIndividualScreen") }
    }
}

@Composable
private fun IeltsChartView(navController: NavController, modifier: Modifier = Modifier, buttonColor: Color) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TapView("IELTS　チャート記録画面", buttonColor) { navController.navigate("toeflIbtChartScreen") }
    }
}

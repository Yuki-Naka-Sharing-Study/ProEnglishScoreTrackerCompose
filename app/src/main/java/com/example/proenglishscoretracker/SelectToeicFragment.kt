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

class SelectToeicFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val composeView = ComposeView(requireContext()).apply {
            setContent {
                SelectToeicScreen(
                    navController = TODO()
                )
            }
        }
        return composeView
    }
}

@Composable
fun SelectToeicScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        ToeicIndividualView(
            navController,
            modifier = Modifier.weight(1f),
            buttonColor = Color.Red
        )
        ToeicChartView(
            navController,
            modifier = Modifier.weight(1f),
            buttonColor = Color.Blue
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectToeicScreenPreview() {
    SelectToeicScreen(navController = rememberNavController())
}

@Composable
private fun ToeicIndividualView(navController: NavController, modifier: Modifier = Modifier, buttonColor: Color) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TapView("TOEIC　個別記録画面", buttonColor) { navController.navigate("toeicIndividualScreen") }
    }
}

@Composable
private fun ToeicChartView(navController: NavController, modifier: Modifier = Modifier, buttonColor: Color) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TapView("TOEIC　チャート記録画面", buttonColor) { navController.navigate("toeicChartScreen") }
    }
}

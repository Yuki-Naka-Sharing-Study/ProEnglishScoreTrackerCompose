package com.example.proenglishscoretracker

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

class SelectConfirmFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val composeView = ComposeView(requireContext()).apply {
            setContent {
                SelectConfirmScreen(
                    navController = TODO()
                )
            }
        }
        return composeView
    }
}

@Composable
fun SelectConfirmScreen(navController: NavController) {
    Row(modifier = Modifier.fillMaxSize()) {
        LeftSideViews(navController, modifier = Modifier.weight(1f))
        RightSideViews(navController, modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectConfirmScreenPreview() {
    SelectConfirmScreen(navController = rememberNavController())
}

@Composable
private fun LeftSideViews(navController: NavController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        EikenIchijiView(navController, Color.Red)
        ToeicView(navController, Color.Yellow)
        ToeflIbtView(navController, Color.Blue)
    }
}

@Composable
private fun RightSideViews(navController: NavController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        EikenNijiView(navController, Color(0xFFFF5722))
        ToeicSwView(navController, Color.Green)
        IeltsView(navController, Color(0xFF9C27B0))
    }
}

@Composable
private fun EikenIchijiView(navController: NavController, buttonColor: Color) {
    TapView("英検一次", buttonColor) { navController.navigate("selectEikenIchijiScreen") }
}

@Composable
private fun EikenNijiView(navController: NavController, buttonColor: Color) {
    TapView("英検二次", buttonColor) { navController.navigate("eikenNijiScreen") }
}

@Composable
private fun ToeicView(navController: NavController, buttonColor: Color) {
    TapView("TOEIC", buttonColor) { navController.navigate("toeicScreen") }
}

@Composable
private fun ToeicSwView(navController: NavController, buttonColor: Color) {
    TapView("TOEIC SW", buttonColor) { navController.navigate("toeicSwScreen") }
}

@Composable
private fun ToeflIbtView(navController: NavController, buttonColor: Color) {
    TapView("TOEFL iBT", buttonColor) { navController.navigate("toeflIbtScreen") }
}

@Composable
private fun IeltsView(navController: NavController, buttonColor: Color) {
    TapView("IELTS", buttonColor) { navController.navigate("ieltsScreen") }
}

@Composable
private fun Screen(screenName: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "$screenName Content", style = MaterialTheme.typography.headlineLarge)
    }
}

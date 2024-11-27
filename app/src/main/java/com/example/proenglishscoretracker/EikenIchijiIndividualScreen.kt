package com.example.proenglishscoretracker

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun EikenIchijiIndividualScreen() {
    Text(text = "英検個別データ保存画面")
}

@Preview(showBackground = true)
@Composable
private fun EikenIchijiIndividualScreenPreview() {
    EikenIchijiIndividualScreen()
}
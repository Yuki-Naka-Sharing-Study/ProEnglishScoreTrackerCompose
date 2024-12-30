package com.example.proenglishscoretracker.individual_screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ToeicIndividualScreen() {
    Text(text = "TOEIC記録確認画面(個別)")
}

@Preview(showBackground = true)
@Composable
private fun ToeicIndividualScreennPreview() {
    ToeicIndividualScreen()
}

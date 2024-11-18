package com.example.proenglishscoretracker

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.Text
import androidx.compose.ui.platform.ComposeView
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class EikenIchijiIndividualFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val composeView = ComposeView(requireContext()).apply {
            setContent {
                EikenIchijiIndividualScreen()
            }
        }
        return composeView
    }
}

@Composable
fun EikenIchijiIndividualScreen() {
    Text(text = "英検個別データ保存画面")
}

@Preview(showBackground = true)
@Composable
private fun EikenIchijiIndividualScreenPreview() {
    EikenIchijiIndividualScreen()
}
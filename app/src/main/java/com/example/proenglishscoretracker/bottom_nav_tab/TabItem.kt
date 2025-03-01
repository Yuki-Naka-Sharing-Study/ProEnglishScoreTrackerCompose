package com.example.proenglishscoretracker.bottom_nav_tab

import com.example.proenglishscoretracker.R
import androidx.compose.runtime.Composable

typealias ComposableFun = @Composable () -> Unit

sealed class TabItem(var icon: Int,var title: String, var screen: ComposableFun){
    object TOEIC : TabItem(R.drawable.toeic, "TOEIC", {})
    object TOEIC_SW : TabItem(R.drawable.toeic, "TOEIC \n SW", {})
    object EIKEN : TabItem(R.drawable.eiken, "英検", {})
    object TOEFL_IBT : TabItem(R.drawable.toefl, "TOEFL \n iBT", {})
    object IELTS : TabItem(R.drawable.ielts, "IELTS", {})
}

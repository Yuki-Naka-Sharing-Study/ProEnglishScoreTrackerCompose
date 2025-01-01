package com.example.proenglishscoretracker.bottom_navigation_and_tab_row

import com.example.proenglishscoretracker.R
import androidx.compose.runtime.Composable

typealias ComposableFun = @Composable () -> Unit

sealed class TabItem(var icon: Int,var title: String, var screen: ComposableFun){
    object IELTS : TabItem(R.drawable.ielts, "IELTS", {})
    object TOEFL_IBT : TabItem(R.drawable.toefl, "TOEFL iBT", {})
    object EIKEN : TabItem(R.drawable.eiken, "英検", {})
    object TOEIC_SW : TabItem(R.drawable.toeic, "TOEIC SW", {})
    object TOEIC : TabItem(R.drawable.toeic, "TOEIC", {})
}

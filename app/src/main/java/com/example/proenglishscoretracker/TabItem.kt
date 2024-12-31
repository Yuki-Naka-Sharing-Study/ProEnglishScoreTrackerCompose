package com.example.proenglishscoretracker

import androidx.compose.runtime.Composable
import com.example.proenglishscoretracker.data_screen.HomeScreen
import com.example.proenglishscoretracker.data_screen.ProfileScreen
import com.example.proenglishscoretracker.data_screen.SettingsScreen


typealias ComposableFun = @Composable () -> Unit

sealed class TabItem(var icon: Int,var title: String, var screen: ComposableFun){
    object Home : TabItem(R.drawable.reading, "Home", { HomeScreen() })
    object Profile : TabItem(R.drawable.listening, "Profile", { ProfileScreen() })
    object Settings : TabItem(R.drawable.speaking, "Settings", { SettingsScreen() })
}

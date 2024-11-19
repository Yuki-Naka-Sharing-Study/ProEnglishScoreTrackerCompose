package com.example.proenglishscoretracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room

class MainActivity : ComponentActivity() {
    private val repository: EnglishInfoRepository = EnglishInfoRepository()
    private lateinit var englishInfoDao: EnglishInfoDao
    private val viewModel: EnglishInfoViewModel by viewModels {
        EnglishInfoViewModelFactory(repository, englishInfoDao)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        englishInfoDao = Room.databaseBuilder(
            application,
            EnglishInfoDatabase::class.java, "english_info_database"
        ).build().englishInfoDao()
        setContent {
            EnglishScoreTracker(viewModel = viewModel)
        }
    }
}

@Composable
fun EnglishScoreTracker(viewModel: EnglishInfoViewModel) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "selectConfirm",
            Modifier.padding(innerPadding)
        ) {
            // 以下、BottomNavigationBar
            composable("selectConfirm") { SelectConfirmScreen(navController) }
            composable("selectRecord") { SelectRecordScreen(navController) }
            composable("setting") { SettingScreen() }

            // 以下、「SelectConfirmFragment」


            // 以下、「SelectXxxScreen」
            composable("selectEikenIchijiScreen") {
                SelectEikenIchijiScreen(navController)
            }
            composable("selectEikenNijiScreen") {
                SelectEikenNijiScreen(navController)
            }
            composable("selectToeicScreen") {
                SelectToeicScreen(navController)
            }
            composable("selectToeicSwScreen") {
                SelectToeicSwScreen(navController)
            }
            composable("selectToeflIbtScreen") {
                SelectToeflIbtScreen(navController)
            }
            composable("selectIeltsScreen") {
                SelectIeltsScreen(navController)
            }


            // 以下、「SelectEikenIchijiFragment」
            composable("eikenIchijiIndividualScreen") {
                EikenIchijiIndividualScreen()
            }
            composable("eikenIchijiChartScreen") {
                EikenIchijiChartScreen()
            }


            // 以下、「SelectEikenNijiFragment」
            composable("eikenNijiIndividualScreen") {
//                EikenNijiIndividualScreen()
            }
            composable("eikenIchijiChartScreen") {
                EikenNijiChartScreen()
            }


            // 以下、「SelectToeicFragment」
            composable("toeicIndividualScreen") {
//                ToeicIndividualScreen()
            }
            composable("toeicChartScreen") {
                ToeicChartScreen()
            }


            // 以下、「SelectToeicSwFragment」
            composable("toeicSwIndividualScreen") {
//                ToeicSwIndividualScreen()
            }
            composable("toeicSwChartScreen") {
                ToeicSwChartScreen()
            }


            // 以下、「SelectToeflIbtFragment」
            composable("toeflIbtIndividualScreen") {
//                ToeflIbtIndividualScreen()
            }
            composable("toeflIbtChartScreen") {
                ToeflIbtChartScreen()
            }


            // 以下、「SelectIeltsFragment」
            composable("ieltsIndividualScreen") {
//                IeltsIndividualScreen()
            }
            composable("ieltsIbtChartScreen") {
                IeltsChartScreen()
            }


            // 以下、「XxxRecordScreen」
            composable("eikenIchijiRecordScreen") {
                EikenIchijiRecordScreen(viewModel = viewModel)
            }
            composable("eikenNijiRecordScreen") {
                EikenNijiRecordScreen(viewModel = viewModel)
            }
            composable("toeicRecordScreen") {
                ToeicRecordScreen(viewModel = viewModel)
            }
            composable("toeicSwRecordScreen") {
                ToeicSwRecordScreen(viewModel = viewModel)
            }
            composable("toeflIbtRecordScreen") {
                ToeflIbtRecordScreen(viewModel = viewModel)
            }
            composable("ieltsRecordScreen") {
                IeltsRecordScreen(viewModel = viewModel)
            }

        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    BottomNavigation {
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Check, contentDescription = "Confirm") },
            label = { Text("Confirm") },
            selected = currentDestination?.route == "selectConfirm",
            onClick = {
                navController.navigate("selectConfirm") {
//                    popUpTo("confirm") { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Edit, contentDescription = "Record") },
            label = { Text("Record") },
            selected = currentDestination?.route == "selectRecord",
            onClick = {
                navController.navigate("selectRecord") {
//                    popUpTo("record") { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = currentDestination?.route == "setting",
            onClick = {
                navController.navigate("setting") {
//                    popUpTo("setting") { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}

@Composable
fun TapView(text: String, buttonColor: Color,  onTap: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(buttonColor)
            .clickable { onTap()},
        contentAlignment = Alignment.Center,
        content = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                fontSize = 24.sp,
            )
        }
    )
}

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
import com.example.proenglishscoretracker.chart_screen.EikenChartScreen
import com.example.proenglishscoretracker.chart_screen.EikenNijiChartScreen
import com.example.proenglishscoretracker.chart_screen.IeltsChartScreen
import com.example.proenglishscoretracker.chart_screen.ToeflIbtChartScreen
import com.example.proenglishscoretracker.chart_screen.ToeicChartScreen
import com.example.proenglishscoretracker.chart_screen.ToeicSwChartScreen
import com.example.proenglishscoretracker.data.EnglishInfoDao
import com.example.proenglishscoretracker.data.EnglishInfoDatabase
import com.example.proenglishscoretracker.data.EnglishInfoRepository
import com.example.proenglishscoretracker.data.EnglishInfoViewModel
import com.example.proenglishscoretracker.data.EnglishInfoViewModelFactory
import com.example.proenglishscoretracker.data_screen.ExamDataScreen
import com.example.proenglishscoretracker.record_screen.EikenIchijiRecordScreen
import com.example.proenglishscoretracker.record_screen.EikenNijiRecordScreen
import com.example.proenglishscoretracker.record_screen.IeltsRecordScreen
import com.example.proenglishscoretracker.record_screen.ToeflIbtRecordScreen
import com.example.proenglishscoretracker.record_screen.ToeicRecordScreen
import com.example.proenglishscoretracker.record_screen.ToeicSwRecordScreen
import com.example.proenglishscoretracker.individual_screen.EikenIndividualScreen
import com.example.proenglishscoretracker.individual_screen.ToeicIndividualScreen
import com.example.proenglishscoretracker.select_screen.SelectEikenIchijiScreen
import com.example.proenglishscoretracker.select_screen.SelectEikenNijiScreen
import com.example.proenglishscoretracker.select_screen.SelectIeltsScreen
import com.example.proenglishscoretracker.tab_screen.SelectRecordScreen
import com.example.proenglishscoretracker.select_screen.SelectToeflIbtScreen
import com.example.proenglishscoretracker.select_screen.SelectToeicScreen
import com.example.proenglishscoretracker.select_screen.SelectToeicSwScreen
import com.example.proenglishscoretracker.tab_screen.SettingScreen

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
        bottomBar = { BottomNavigationBar(
            navController
        ) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "examDataScreen",
            Modifier.padding(innerPadding)
        ) {
            // 以下、BottomNavigationBar
            composable("examDataScreen") { ExamDataScreen() }
            composable("selectRecord") { SelectRecordScreen(navController) }
            composable("setting") { SettingScreen() }

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
                EikenIndividualScreen()
            }
            composable("eikenIchijiChartScreen") {
                EikenChartScreen()
            }


            // 以下、「SelectEikenNijiFragment」
            composable("eikenNijiIndividualScreen") {
//                EikenNijiIndividualScreen()
            }
            composable("eikenIchijiChartScreen") {
                EikenNijiChartScreen()
            }


            // 以下、「SelectToeicFragment」
            composable("toeicIndividualScreen") { ToeicIndividualScreen() }
            composable("toeicChartScreen") { ToeicChartScreen() }


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
    BottomNavigation(
        backgroundColor = Color(0xFFE0F7FA), // ナビゲーションバーの背景色
        contentColor = Color(0xFF00796B) // デフォルトのコンテンツカラー（アイコン・テキスト）
    ) {
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Check, contentDescription = "examDataScreen") },
            label = { Text("Confirm") },
            selected = currentDestination?.route == "examDataScreen",
            onClick = {
                navController.navigate("examDataScreen") {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            selectedContentColor = Color(0xFF004D40), // 選択中のタブの色
            unselectedContentColor = Color(0xFFB2DFDB) // 非選択中のタブの色
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Edit, contentDescription = "Record") },
            label = { Text("Record") },
            selected = currentDestination?.route == "selectRecord",
            onClick = {
                navController.navigate("selectRecord") {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            selectedContentColor = Color(0xFF004D40),
            unselectedContentColor = Color(0xFFB2DFDB)
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = currentDestination?.route == "setting",
            onClick = {
                navController.navigate("setting") {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            selectedContentColor = Color(0xFF004D40),
            unselectedContentColor = Color(0xFFB2DFDB)
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

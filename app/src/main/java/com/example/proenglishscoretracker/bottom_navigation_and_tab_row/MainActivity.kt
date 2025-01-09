package com.example.proenglishscoretracker.bottom_navigation_and_tab_row

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.proenglishscoretracker.R
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
import com.example.proenglishscoretracker.record_screen.EikenIchijiRecordScreen
import com.example.proenglishscoretracker.record_screen.EikenNijiRecordScreen
import com.example.proenglishscoretracker.record_screen.IeltsRecordScreen
import com.example.proenglishscoretracker.record_screen.ToeflIbtRecordScreen
import com.example.proenglishscoretracker.record_screen.ToeicRecordScreen
import com.example.proenglishscoretracker.record_screen.ToeicSwRecordScreen
import com.example.proenglishscoretracker.individual_screen.EikenIndividualScreen
import com.example.proenglishscoretracker.individual_screen.ToeicIndividualScreen

class MainActivity : ComponentActivity() {
    private val repository: EnglishInfoRepository = EnglishInfoRepository()
    private lateinit var englishInfoDao: EnglishInfoDao
    private val dataStore by preferencesDataStore(name = "examDataScreen")
    private val viewModel: EnglishInfoViewModel by viewModels {
        EnglishInfoViewModelFactory(repository, englishInfoDao, dataStore)
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
            // 以下、「BottomNavigationBar」
            composable("examDataScreen") { ExamDataScreen() }
            composable("examRecordScreen") { ExamRecordScreen(viewModel) }
            composable("setting") { SettingScreen() }


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
        backgroundColor = Color(0xFFCE93D8), // ナビゲーションバーの背景色
        contentColor = Color(0xFF00796B) // デフォルトのコンテンツカラー（アイコン・テキスト）
    ) {
        BottomNavigationItem(
//            icon = { Icon(Icons.Default.StackedLineChart, contentDescription = "examDataScreen") },
            icon = {
                // 自作のアイコンを表示
                Icon(
                    painter = painterResource(id = R.drawable.chart), // ここを自作のアイコンに変更
                    contentDescription = "examDataScreen"
                )
            },
            label = { Text("記録確認") },
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
            label = { Text("記録") },
            selected = currentDestination?.route == "examRecordScreen",
            onClick = {
                navController.navigate("examRecordScreen") {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            selectedContentColor = Color(0xFF004D40),
            unselectedContentColor = Color(0xFFB2DFDB)
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("設定") },
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

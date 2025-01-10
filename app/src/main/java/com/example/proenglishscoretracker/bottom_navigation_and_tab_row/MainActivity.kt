package com.example.proenglishscoretracker.bottom_navigation_and_tab_row

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.proenglishscoretracker.R
import com.example.proenglishscoretracker.chart_screen.EikenChartScreen
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
import com.example.proenglishscoretracker.individual_screen.IeltsIndividualScreen
import com.example.proenglishscoretracker.individual_screen.ToeflIbtIndividualScreen
import com.example.proenglishscoretracker.individual_screen.ToeicIndividualScreen
import com.example.proenglishscoretracker.individual_screen.ToeicSwIndividualScreen
import com.example.proenglishscoretracker.onboard.OnboardingScreen

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
    val isFirstLaunchState = viewModel.isFirstLaunch.collectAsState(initial = null)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (isFirstLaunchState.value == false) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (isFirstLaunchState.value == true) "onboarding" else "examDataScreen",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("onboarding") {
                OnboardingScreen(
                    onFinish = {
                        viewModel.completeOnboarding()
                        navController.navigate("examDataScreen") {
                            popUpTo("onboarding") { inclusive = true }
                        }
                    }
                )
            }
            composable("examDataScreen") { ExamDataScreen() }
            composable("examRecordScreen") { ExamRecordScreen(viewModel) }
            composable("setting") { SettingScreen() }

            // XxxIndividualScreen
            composable("toeicIndividualScreen") { ToeicIndividualScreen() }
            composable("toeicSwIndividualScreen") { ToeicSwIndividualScreen() }
            composable("eikenIchijiIndividualScreen") { EikenIndividualScreen() }
            composable("toeflIbtIndividualScreen") { ToeflIbtIndividualScreen() }
            composable("ieltsIndividualScreen") { IeltsIndividualScreen() }

            // XxxChartScreen
            composable("toeicChartScreen") { ToeicChartScreen() }
            composable("toeicSwChartScreen") { ToeicSwChartScreen() }
            composable("eikenIchijiChartScreen") { EikenChartScreen() }
            composable("toeflIbtChartScreen") { ToeflIbtChartScreen() }
            composable("ieltsIbtChartScreen") { IeltsChartScreen() }

            // XxxRecordScreen
            composable("toeicRecordScreen") { ToeicRecordScreen(viewModel = viewModel) }
            composable("toeicSwRecordScreen") { ToeicSwRecordScreen(viewModel = viewModel) }
            composable("eikenIchijiRecordScreen") { EikenIchijiRecordScreen(viewModel = viewModel) }
            composable("eikenNijiRecordScreen") { EikenNijiRecordScreen(viewModel = viewModel) }
            composable("toeflIbtRecordScreen") { ToeflIbtRecordScreen(viewModel = viewModel) }
            composable("ieltsRecordScreen") { IeltsRecordScreen(viewModel = viewModel) }
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Loading...", fontSize = 20.sp)
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination
    BottomNavigation(
        backgroundColor = Color(0xFFCE93D8),
        contentColor = Color(0xFF00796B)
    ) {
        BottomNavigationItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.chart),
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
            selectedContentColor = Color(0xFF004D40),
            unselectedContentColor = Color(0xFFB2DFDB)
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

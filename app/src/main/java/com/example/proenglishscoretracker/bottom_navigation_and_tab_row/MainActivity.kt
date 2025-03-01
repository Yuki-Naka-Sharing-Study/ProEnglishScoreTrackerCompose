package com.example.proenglishscoretracker.bottom_navigation_and_tab_row

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
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
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
import com.example.proenglishscoretracker.detail_screen.EikenDetailScreen
import com.example.proenglishscoretracker.detail_screen.ToeflIbtDetailScreen
import com.example.proenglishscoretracker.detail_screen.ToeicDetailScreen
import com.example.proenglishscoretracker.detail_screen.ToeicSwDetailScreen
import com.example.proenglishscoretracker.edit_screen.EikenEditScreen
import com.example.proenglishscoretracker.edit_screen.ToeflIbtEditScreen
import com.example.proenglishscoretracker.edit_screen.ToeicEditScreen
import com.example.proenglishscoretracker.edit_screen.ToeicSwEditScreen
import com.example.proenglishscoretracker.record_screen.EikenRecordScreen
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
import com.example.proenglishscoretracker.settings.CountdownSettingsScreen

class MainActivity : ComponentActivity() {
    private lateinit var englishInfoDao: EnglishInfoDao
    private lateinit var repository: EnglishInfoRepository
    private val dataStore by preferencesDataStore(name = "examDataScreen")
    private val viewModel: EnglishInfoViewModel by lazy {
        EnglishInfoViewModelFactory(repository, englishInfoDao, dataStore).create(EnglishInfoViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 画面の回転を無効にする
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Roomデータベースの初期化
        val database = Room.databaseBuilder(
            application,
            EnglishInfoDatabase::class.java, "english_info_database"
        ).build()
        // DaoとRepositoryの初期化
        englishInfoDao = database.englishInfoDao()
        repository = EnglishInfoRepository(englishInfoDao)
        setContent {
            EnglishScoreTracker(
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun EnglishScoreTracker(
    viewModel: EnglishInfoViewModel
) {
    val navController = rememberNavController()
    val isFirstLaunchState = viewModel.isFirstLaunch.collectAsState(initial = null)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (isFirstLaunchState.value == false) {
                BottomNavigationBar(navController = navController, viewModel)
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
            composable("examDataScreen") { ExamDataScreen(viewModel, navController) }
            composable("examRecordScreen") { ExamRecordScreen(viewModel) }
            composable("setting") { SettingsScreen(navController) }

            composable("examCountdown") { CountdownSettingsScreen(navController)}

            // XxxIndividualScreen
            composable("toeicIndividualScreen") { ToeicIndividualScreen(viewModel, navController) }
            composable("toeicSwIndividualScreen") { ToeicSwIndividualScreen(viewModel, navController) }
            composable("eikenIchijiIndividualScreen") { EikenIndividualScreen(viewModel, navController) }
            composable("toeflIbtIndividualScreen") { ToeflIbtIndividualScreen(viewModel, navController) }
            composable("ieltsIndividualScreen") { IeltsIndividualScreen() }

            // XxxDetailScreen
            composable("toeic_detail/{toeicId}") { backStackEntry ->
                val toeicId = backStackEntry.arguments?.getString("toeicId")
                // toeicIdを使用して詳細情報を取得し、ToeicDetailScreenに渡す
                if (toeicId != null) {
                    ToeicDetailScreen(
                        toeicId = toeicId,
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }
            composable("toeic_sw_detail/{toeicSwId}") { backStackEntry ->
                val toeicSwId = backStackEntry.arguments?.getString("toeicSwId")
                if (toeicSwId != null) {
                    ToeicSwDetailScreen(
                        toeicSwId = toeicSwId,
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }
            composable("eiken_detail/{eikenId}") { backStackEntry ->
                val eikenId = backStackEntry.arguments?.getString("eikenId")
                if (eikenId != null) {
                    EikenDetailScreen(
                        eikenId = eikenId,
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }
            composable("toefl_ibt_detail/{toeflIbtId}") { backStackEntry ->
                val toeflIbtId = backStackEntry.arguments?.getString("toeflIbtId")
                if (toeflIbtId != null) {
                    ToeflIbtDetailScreen(
                        toeflIbtId = toeflIbtId,
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }

            // XxxEditScreen
            composable("toeic_edit/{toeicId}") { backStackEntry ->
                val toeicId = backStackEntry.arguments?.getString("toeicId")
                val toeicInfo = viewModel.selectedToeicInfo.collectAsState().value

                if (toeicId != null && toeicInfo != null) {
                    ToeicEditScreen(
                        toeicInfo = toeicInfo,
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }
            composable("toeic_sw_edit/{toeicSwId}") { backStackEntry ->
                val toeicSwId = backStackEntry.arguments?.getString("toeicSwId")
                val toeicSwInfo = viewModel.selectedToeicSwInfo.collectAsState().value

                if (toeicSwId != null && toeicSwInfo != null) {
                    ToeicSwEditScreen(
                        toeicSwInfo = toeicSwInfo,
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }
            composable("eiken_edit/{eikenId}") { backStackEntry ->
                val eikenId = backStackEntry.arguments?.getString("eikenId")
                val eikenInfo = viewModel.selectedEikenInfo.collectAsState().value

                if (eikenId != null && eikenInfo != null) {
                    EikenEditScreen(
                        eikenInfo = eikenInfo,
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }
            composable("toefl_ibt_edit/{toeflIbtId}") { backStackEntry ->
                val toeflIbtId = backStackEntry.arguments?.getString("toeflIbtId")
                val toeflIbtInfo = viewModel.selectedToeflIbtInfo.collectAsState().value

                if (toeflIbtId != null && toeflIbtInfo != null) {
                    ToeflIbtEditScreen(
                        toeflIbtInfo = toeflIbtInfo,
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }


            // XxxChartScreen
            composable("toeicChartScreen") { ToeicChartScreen(viewModel) }
            composable("toeicSwChartScreen") { ToeicSwChartScreen(viewModel) }
            composable("eikenIchijiChartScreen") { EikenChartScreen(viewModel) }
            composable("toeflIbtChartScreen") { ToeflIbtChartScreen(viewModel) }
            composable("ieltsIbtChartScreen") { IeltsChartScreen() }

            // XxxRecordScreen
            composable("toeicRecordScreen") { ToeicRecordScreen(viewModel = viewModel) }
            composable("toeicSwRecordScreen") { ToeicSwRecordScreen(viewModel = viewModel) }
            composable("eikenRecordScreen") { EikenRecordScreen(viewModel = viewModel) }
            composable("toeflIbtRecordScreen") { ToeflIbtRecordScreen(viewModel = viewModel) }
            composable("ieltsRecordScreen") { IeltsRecordScreen(viewModel = viewModel) }
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    viewModel: EnglishInfoViewModel
) {
    val eikenUnsavedChanges by viewModel.eikenUnsavedChanges.collectAsState()
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var targetRoute by rememberSaveable { mutableStateOf<String?>(null) }
    val eikenGrade by viewModel.eikenGrade.collectAsState()
    val eikenReadingScore by viewModel.eikenReadingScore.collectAsState()
    val eikenListeningScore by viewModel.eikenListeningScore.collectAsState()
    val eikenWritingScore by viewModel.eikenWritingScore.collectAsState()
    val eikenSpeakingScore by viewModel.eikenSpeakingScore.collectAsState()
    val eikenMemoText by viewModel.eikenMemoText.collectAsState()

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "確認") },
            text = { Text("入力途中で画面を切り替えると情報が失われますが大丈夫でしょうか？") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    targetRoute?.let {
                        // TODO : TOEICの情報喪失アラートの実装

                        viewModel.setEikenGrade("")
                        viewModel.setEikenReadingScore(0)
                        viewModel.setEikenListeningScore(0)
                        viewModel.setEikenWritingScore(0)
                        viewModel.setEikenSpeakingScore(0)
                        viewModel.setEikenMemoText("")
                        navController.navigate(it) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }) {
                    Text(text = "OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(text = "キャンセル")
                }
            }
        )
    }

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
            selected = navController.currentBackStackEntry?.destination?.route == "examDataScreen",
            onClick = {
                if (
                // TODO : TOEICの情報喪失アラートの実装

                    eikenUnsavedChanges &&
                    eikenGrade.isNotEmpty() ||
                    eikenReadingScore > 0 ||
                    eikenListeningScore > 0 ||
                    eikenWritingScore > 0 ||
                    eikenSpeakingScore > 0 ||
                    eikenMemoText.isNotEmpty()
                    )
                {
                    targetRoute = "examDataScreen"
                    showDialog = true
                } else {
                    // TODO : TOEICの情報喪失アラートの実装

                    viewModel.setEikenGrade("")
                    viewModel.setEikenReadingScore(0)
                    viewModel.setEikenListeningScore(0)
                    viewModel.setEikenWritingScore(0)
                    viewModel.setEikenSpeakingScore(0)
                    viewModel.setEikenMemoText("")
                    navController.navigate("examDataScreen") {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            selectedContentColor = Color(0xFF004D40),
            unselectedContentColor = Color(0xFFB2DFDB)
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Edit, contentDescription = "Record") },
            label = { Text("記録") },
            selected = navController.currentBackStackEntry?.destination?.route == "examRecordScreen",
            onClick = {
                if (
                // TODO : TOEICの情報喪失アラートの実装

                    eikenUnsavedChanges &&
                    eikenGrade.isNotEmpty() ||
                    eikenReadingScore > 0 ||
                    eikenListeningScore > 0 ||
                    eikenWritingScore > 0 ||
                    eikenSpeakingScore > 0 ||
                    eikenMemoText.isNotEmpty())
                {
                    targetRoute = "examRecordScreen"
                    showDialog = true
                } else {
                    // TODO : TOEICの情報喪失アラートの実装

                    viewModel.setEikenGrade("")
                    viewModel.setEikenReadingScore(0)
                    viewModel.setEikenListeningScore(0)
                    viewModel.setEikenWritingScore(0)
                    viewModel.setEikenSpeakingScore(0)
                    viewModel.setEikenMemoText("")
                    navController.navigate("examRecordScreen") {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            selectedContentColor = Color(0xFF004D40),
            unselectedContentColor = Color(0xFFB2DFDB)
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("設定") },
            selected = navController.currentBackStackEntry?.destination?.route == "setting",
            onClick = {
                if (
                // TODO : TOEICの情報喪失アラートの実装

                    eikenUnsavedChanges &&
                    eikenGrade.isNotEmpty() ||
                    eikenReadingScore > 0 ||
                    eikenListeningScore > 0 ||
                    eikenWritingScore > 0 ||
                    eikenSpeakingScore > 0 ||
                    eikenMemoText.isNotEmpty())
                {
                    targetRoute = "setting"
                    showDialog = true
                } else {
                    // TODO : TOEICの情報喪失アラートの実装

                    viewModel.setEikenGrade("")
                    viewModel.setEikenReadingScore(0)
                    viewModel.setEikenListeningScore(0)
                    viewModel.setEikenWritingScore(0)
                    viewModel.setEikenSpeakingScore(0)
                    viewModel.setEikenMemoText("")
                    navController.navigate("setting") {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            selectedContentColor = Color(0xFF004D40),
            unselectedContentColor = Color(0xFFB2DFDB)
        )
    }
}

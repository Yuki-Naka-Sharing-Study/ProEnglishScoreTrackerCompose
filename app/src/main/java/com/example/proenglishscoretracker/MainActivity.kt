package com.example.proenglishscoretracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proenglishscoretracker.ui.theme.ProEnglishScoreTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EnglishScoreTracker()
            ProEnglishScoreTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EnglishScoreTracker()
                }
            }
        }
    }
}

@Composable
fun EnglishScoreTracker() {
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


            // 以下、「SelectEikenIchijiFragment」
            composable("eikenIchijiIndividualScreen") {
                EikenIchijiIndividualScreen()
            }
            composable("eikenIchijiChartScreen") {
                EikenIchijiChartScreen()
            }


            // 以下、「SelectRecordFragment」
            // 以下の三行はテストで書いたコード
//            composable("selectEikenIchijiScreenDesu") {
//                SelectEikenIchijiScreenDesu()
//            }

            composable("selectEikenIchijiScreen") {
                SelectEikenIchijiScreen(navController)
            }
//            composable("eikenNijiScreen") {
//                Screen("英検二次")
//            }
//            composable("toeicScreen") {
//                Screen("TOEIC")
//            }
//            composable("toeicSwScreen") {
//                Screen("TOEIC SW")
//            }
//            composable("toeflIbtScreen") {
//                Screen("TOEFL iBT")
//            }
//            composable("ieltsScreen") {
//                Screen("IELTS")
//            }

            composable("eikenIchijiRecordScreen") {
                EikenIchijiRecordScreen()
            }
            composable("eikenNijiRecordScreen") {
                EikenIchijiRecordScreen()
            }
            composable("toeicRecordScreen") {
                EikenIchijiRecordScreen()
            }
            composable("toeicSwRecordScreen") {
                EikenIchijiRecordScreen()
            }
            composable("toeflIbtRecordScreen") {
                EikenIchijiRecordScreen()
            }
            composable("ieltsRecordScreen") {
                EikenIchijiRecordScreen()
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

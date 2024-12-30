package com.example.proenglishscoretracker.data_screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun ToeicDataScreen(navController: NavController) {
    SingleChoiceSegmentedButton(navController = navController)
}

@Composable
private fun SingleChoiceSegmentedButton(navController: NavController, modifier: Modifier = Modifier) {
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val options = listOf("個別", "グラフ")

    SingleChoiceSegmentedButtonRow(
        modifier = modifier.fillMaxWidth()
    ) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                modifier = Modifier
                    .weight(1f),
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = {
                    selectedIndex = index
                    // 「個別」または「グラフ」のタップで画面遷移
                    if (index == 0) {
                        navController.navigate("toeicIndividualScreen")
                    } else {
                        navController.navigate("toeicChartScreen")
                    }
                },
                selected = index == selectedIndex,
                label = { Text(label) }
            )
        }
    }
}

package com.example.proenglishscoretracker.data_screen

import androidx.compose.foundation.layout.Column
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
import com.example.proenglishscoretracker.chart_screen.ToeicChartScreen
import com.example.proenglishscoretracker.individual_screen.ToeicIndividualScreen

@Composable
fun ToeicDataScreen() {
    Column {
        //「ToeicSegmentedButton()」の上にTabLayoutを配置。
        ToeicSegmentedButton()
    }
}

@Composable
private fun ToeicSegmentedButton() {
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val options = listOf("個別", "グラフ")

    Column(modifier = Modifier.fillMaxWidth()) {
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEachIndexed { index, label ->
                SegmentedButton(
                    modifier = Modifier
                        .weight(1f),
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    ),
                    onClick = { selectedIndex = index },
                    selected = index == selectedIndex,
                    label = { Text(label) }
                )
            }
        }
        when (selectedIndex) {
            0 -> ToeicIndividualScreen()
            1 -> ToeicChartScreen()
        }
    }
}

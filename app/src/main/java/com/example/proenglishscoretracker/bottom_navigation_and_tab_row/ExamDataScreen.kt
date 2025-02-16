package com.example.proenglishscoretracker.bottom_navigation_and_tab_row

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
import androidx.compose.material.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.proenglishscoretracker.chart_screen.EikenChartScreen
import com.example.proenglishscoretracker.chart_screen.IeltsChartScreen
import com.example.proenglishscoretracker.chart_screen.ToeflIbtChartScreen
import com.example.proenglishscoretracker.chart_screen.ToeicSwChartScreen
import com.example.proenglishscoretracker.data.EnglishInfoViewModel
import com.example.proenglishscoretracker.individual_screen.EikenIndividualScreen
import com.example.proenglishscoretracker.individual_screen.IeltsIndividualScreen
import com.example.proenglishscoretracker.individual_screen.ToeflIbtIndividualScreen
import com.example.proenglishscoretracker.individual_screen.ToeicSwIndividualScreen
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.pagerTabIndicatorOffset
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun ExamDataScreen(
    viewModel: EnglishInfoViewModel,
    navHostController: NavHostController
) {
    val tabs = listOf(
        TabItem.IELTS,
        TabItem.TOEFL_IBT,
        TabItem.EIKEN,
        TabItem.TOEIC_SW,
        TabItem.TOEIC,
    )
    val pagerState = com.google.accompanist.pager.rememberPagerState(tabs.size)

    Column {
        Tabs(tabs = tabs, pagerState = pagerState)
        when (pagerState.currentPage) {
            0 -> IeltsSegmentedButton()
            1 -> ToeflIbtSegmentedButton(viewModel)
            2 -> EikenSegmentedButton(viewModel)
            3 -> ToeicSwSegmentedButton(viewModel, navHostController)
            4 -> ToeicSegmentedButton(viewModel, navHostController)
        }
        TabsContent(tabs = tabs, pagerState = pagerState)
    }
}

@OptIn(ExperimentalPagerApi::class)
@ExperimentalMaterialApi
@Composable
private fun Tabs(
    tabs: List<TabItem>,
    pagerState: com.google.accompanist.pager.PagerState
) {
    val scope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = Color(0xFF6A1B9A),
        contentColor = Color.Yellow,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        }
    ) {
        tabs.forEachIndexed { index, tab ->
            LeadingIconTab(
                icon = { /* アイコンを表示しない */ },
                text = { Text(text = tab.title, color = Color.White) },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
            )
        }
    }
}

@Composable
private fun ToeicSegmentedButton(
    viewModel: EnglishInfoViewModel,
    navHostController: NavHostController
) {
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
                    label = {
                        Text(
                            text = label,
                            color = if (index == selectedIndex) Color.White else Color.Black
                        )
                    },
                    colors = SegmentedButtonDefaults.colors(Color(0xFFCE93D8)),
                )
            }
        }
        when (selectedIndex) {
            0 -> ToeicIndividualScreen(viewModel, navHostController)
            1 -> ToeicChartScreen(viewModel)
        }
    }
}

@Composable
private fun ToeicSwSegmentedButton(
    viewModel: EnglishInfoViewModel,
    navController: NavController
) {
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
                    label = {
                        Text(
                            text = label,
                            color = if (index == selectedIndex) Color.White else Color.Black
                        )
                    },
                    colors = SegmentedButtonDefaults.colors(Color(0xFFCE93D8)),
                )
            }
        }
        when (selectedIndex) {
            0 -> ToeicSwIndividualScreen(viewModel, navController)
            1 -> ToeicSwChartScreen(viewModel)
        }
    }
}

@Composable
private fun EikenSegmentedButton(viewModel: EnglishInfoViewModel) {
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
                    label = {
                        Text(
                            text = label,
                            color = if (index == selectedIndex) Color.White else Color.Black
                        )
                    },
                    colors = SegmentedButtonDefaults.colors(Color(0xFFCE93D8)),
                )
            }
        }
        when (selectedIndex) {
            0 -> EikenIndividualScreen(viewModel)
            1 -> EikenChartScreen(viewModel)
        }
    }
}

@Composable
private fun ToeflIbtSegmentedButton(viewModel: EnglishInfoViewModel) {
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
                    label = {
                        Text(
                            text = label,
                            color = if (index == selectedIndex) Color.White else Color.Black
                        )
                    },
                    colors = SegmentedButtonDefaults.colors(Color(0xFFCE93D8)),
                )
            }
        }
        when (selectedIndex) {
            0 -> ToeflIbtIndividualScreen(viewModel)
            1 -> ToeflIbtChartScreen(viewModel)
        }
    }
}

@Composable
private fun IeltsSegmentedButton() {
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
                    label = {
                        Text(
                            text = label,
                            color = if (index == selectedIndex) Color.White else Color.Black
                        )
                    },
                    colors = SegmentedButtonDefaults.colors(Color(0xFFCE93D8)),
                )
            }
        }
        when (selectedIndex) {
            0 -> IeltsIndividualScreen()
            1 -> IeltsChartScreen()
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun TabsContent(
    tabs: List<TabItem>,
    pagerState: com.google.accompanist.pager.PagerState
) {
    com.google.accompanist.pager.HorizontalPager(
        state = pagerState,
        count = 5,
    )
    { page ->
        tabs[page].screen()
    }
}

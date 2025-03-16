package com.example.proenglishscoretracker.bottom_nav_tab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.example.proenglishscoretracker.list_screen.ToeicListScreen
import androidx.compose.material.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proenglishscoretracker.chart_screen.EikenChartScreen
import com.example.proenglishscoretracker.chart_screen.IeltsChartScreen
import com.example.proenglishscoretracker.chart_screen.ToeflIbtChartScreen
import com.example.proenglishscoretracker.chart_screen.ToeicSwChartScreen
import com.example.proenglishscoretracker.data.EnglishInfoViewModel
import com.example.proenglishscoretracker.list_screen.EikenListScreen
import com.example.proenglishscoretracker.list_screen.IeltsListScreen
import com.example.proenglishscoretracker.list_screen.ToeflIbtListScreen
import com.example.proenglishscoretracker.list_screen.ToeicSwListScreen
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
        TabItem.TOEIC,
        TabItem.TOEIC_SW,
        TabItem.EIKEN,
        TabItem.TOEFL_IBT,
        TabItem.IELTS,
    )
    val pagerState = com.google.accompanist.pager.rememberPagerState(initialPage = 0)

    Column {
        Tabs(tabs = tabs, pagerState = pagerState)
        when (pagerState.currentPage) {
            0 -> ToeicSegmentedButton(viewModel, navHostController)
            1 -> ToeicSwSegmentedButton(viewModel, navHostController)
            2 -> EikenSegmentedButton(viewModel, navHostController)
            3 -> ToeflIbtSegmentedButton(viewModel, navHostController)
            4 -> IeltsSegmentedButton(viewModel, navHostController)
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
    val options = listOf("グラフ", "一覧")

    Column(modifier = Modifier.fillMaxWidth()) {
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
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
            0 -> ToeicChartScreen(viewModel)
            1 -> ToeicListScreen(viewModel, navHostController)
        }
    }
}

@Composable
private fun ToeicSwSegmentedButton(
    viewModel: EnglishInfoViewModel,
    navHostController: NavHostController
) {
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val options = listOf("グラフ", "一覧")

    Column(modifier = Modifier.fillMaxWidth()) {
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
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
            0 -> ToeicSwChartScreen(viewModel)
            1 -> ToeicSwListScreen(viewModel, navHostController)
        }
    }
}

@Composable
private fun EikenSegmentedButton(
    viewModel: EnglishInfoViewModel,
    navHostController: NavHostController
) {
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val options = listOf("グラフ", "一覧")

    Column(modifier = Modifier.fillMaxWidth()) {
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
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
            0 -> EikenChartScreen(viewModel)
            1 -> EikenListScreen(viewModel, navHostController)
        }
    }
}

@Composable
private fun ToeflIbtSegmentedButton(
    viewModel: EnglishInfoViewModel,
    navHostController: NavHostController
) {
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val options = listOf("グラフ", "一覧")

    Column(modifier = Modifier.fillMaxWidth()) {
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
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
            0 -> ToeflIbtChartScreen(viewModel)
            1 -> ToeflIbtListScreen(viewModel, navHostController)
        }
    }
}

@Composable
private fun IeltsSegmentedButton(
    viewModel: EnglishInfoViewModel,
    navHostController: NavHostController
) {
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val options = listOf("グラフ", "一覧")

    Column(modifier = Modifier.fillMaxWidth()) {
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
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
            0 -> IeltsChartScreen(viewModel)
            1 -> IeltsListScreen(viewModel, navHostController)
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

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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.proenglishscoretracker.TabItem
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.pagerTabIndicatorOffset
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ToeicDataScreen() {
    Column {
        //「ToeicSegmentedButton()」の上にTabLayoutを配置。
        MainScreen()
        ToeicSegmentedButton()
    }
}

@OptIn(ExperimentalPagerApi::class)
@ExperimentalMaterialApi
@Composable
fun MainScreen() {
    val tabs = listOf(
        TabItem.Home,
        TabItem.Profile,
        TabItem.Settings,
    )
    val pagerState = com.google.accompanist.pager.rememberPagerState(tabs.size)

    Column() {
        Tabs(tabs = tabs, pagerState = pagerState)
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
        backgroundColor = Color.LightGray,
        contentColor = Color.Gray,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        }
    ) {
        tabs.forEachIndexed { index, tab ->
            LeadingIconTab(
                icon = { Icon(painter = painterResource(id = tab.icon), contentDescription = "") },
                text = { Text(text = tab.title) },
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

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun TabsContent(
    tabs: List<TabItem>,
    pagerState: com.google.accompanist.pager.PagerState
) {
    com.google.accompanist.pager.HorizontalPager(
        state = pagerState,
        count = 3,
    )
    { page ->
        tabs[page].screen()
    }
}

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Home Screen",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 30.sp
        )
    }
}

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Profile Screen",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 30.sp
        )
    }
}

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Settings Screen",
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            fontSize = 30.sp
        )
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

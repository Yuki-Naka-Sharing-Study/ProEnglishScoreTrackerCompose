package com.example.proenglishscoretracker.bottom_navigation_and_tab_row

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import com.example.proenglishscoretracker.data.EnglishInfoViewModel
import com.example.proenglishscoretracker.record_screen.EikenRecordScreen
import com.example.proenglishscoretracker.record_screen.IeltsRecordScreen
import com.example.proenglishscoretracker.record_screen.ToeflIbtRecordScreen
import com.example.proenglishscoretracker.record_screen.ToeicRecordScreen
import com.example.proenglishscoretracker.record_screen.ToeicSwRecordScreen
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.pagerTabIndicatorOffset
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun ExamRecordScreen(viewModel: EnglishInfoViewModel) {
    val tabs = listOf(
        TabItem.TOEIC,
        TabItem.TOEIC_SW,
        TabItem.EIKEN,
        TabItem.TOEFL_IBT,
        TabItem.IELTS,
    )
    val pagerState = com.google.accompanist.pager.rememberPagerState(initialPage = 0)
    val scope = rememberCoroutineScope()
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var pendingPage by rememberSaveable { mutableIntStateOf(0) }

    Column {
        Tabs(
            tabs = tabs,
            pagerState = pagerState,
            onTabClick = { index ->
                if (
                    // TODO : TOEICの情報喪失アラートの実装

                    viewModel.eikenGrade.value.isNotEmpty() ||
                    viewModel.eikenReadingScore.value > 0 ||
                    viewModel.eikenListeningScore.value > 0 ||
                    viewModel.eikenWritingScore.value > 0 ||
                    viewModel.eikenSpeakingScore.value > 0 ||
                    viewModel.eikenMemoText.value.isNotEmpty()
                )
                {
                    showDialog = true
                    pendingPage = index
                } else {
                    scope.launch { pagerState.animateScrollToPage(index) }
                }
            }
        )
        when (pagerState.currentPage) {
            0 -> ToeicRecordScreen(viewModel = viewModel)
            1 -> ToeicSwRecordScreen(viewModel = viewModel)
            2 -> EikenRecordScreen(viewModel = viewModel)
            3 -> ToeflIbtRecordScreen(viewModel = viewModel)
            4 -> IeltsRecordScreen(viewModel = viewModel)
        }
        TabsContent(tabs = tabs, pagerState = pagerState)
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("データ喪失の可能性") },
            text = { Text("入力途中で画面遷移するとデータが喪失しますが宜しいでしょうか？") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.setEikenMemoText("")
                    scope.launch { pagerState.animateScrollToPage(pendingPage) }
                    showDialog = false
                }) {
                    Text("はい")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("いいえ")
                }
            }
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@ExperimentalMaterialApi
@Composable
private fun Tabs(
    tabs: List<TabItem>,
    pagerState: com.google.accompanist.pager.PagerState,
    onTabClick: (Int) -> Unit
) {
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
                onClick = { onTabClick(index) }
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
        count = 5,
    )
    { page ->
        tabs[page].screen()
    }
}

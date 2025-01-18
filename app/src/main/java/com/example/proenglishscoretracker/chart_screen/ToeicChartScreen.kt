package com.example.proenglishscoretracker.chart_screen

import android.view.ViewGroup
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.proenglishscoretracker.R
import com.example.proenglishscoretracker.data.EnglishInfoViewModel
import com.example.proenglishscoretracker.wheel_picker.FVerticalWheelPicker
import com.example.proenglishscoretracker.wheel_picker.FWheelPickerFocusVertical
import com.example.proenglishscoretracker.wheel_picker.rememberFWheelPickerState

@Composable
fun ToeicChartScreen(viewModel: EnglishInfoViewModel) {
    var examYear by rememberSaveable { mutableIntStateOf(0) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ToeicScoreChart(viewModel)

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_64_dp)))

        Text(text = "受験年を選択", fontSize = 20.sp)

        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_32_dp)))
        
        ExamYearPicker(
            Modifier,
            examYear,
        ) {
            examYear = it
            viewModel.setSumScore(it)
        }
    }
}

@Composable
private fun ToeicScoreChart(viewModel: EnglishInfoViewModel) {
    val toeicInfoList by viewModel.toeicInfo.collectAsState()

    if (toeicInfoList.isNotEmpty()) { // データがある場合のみグラフを表示
        val examDates = toeicInfoList.map { it.date }
        val readingScores = toeicInfoList.map { it.readingScore.toFloat() }
        val listeningScores = toeicInfoList.map { it.listeningScore.toFloat() }

        val entriesReading = readingScores.mapIndexed { index, score ->
            Entry(index.toFloat(), score)
        }
        val entriesListening = listeningScores.mapIndexed { index, score ->
            Entry(index.toFloat(), score)
        }

        AndroidView(
            factory = { context ->
                LineChart(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    val dataSetReading = LineDataSet(
                        entriesReading,
                        "Readingスコア").apply {
                        color = android.graphics.Color.RED
                        valueTextColor = android.graphics.Color.BLACK
                        valueTextSize = 15f // スコアのテキストサイズを設定
                    }
                    val dataSetListening = LineDataSet(
                        entriesListening,
                        "Listeningスコア").apply {
                        color = android.graphics.Color.BLUE
                        valueTextColor = android.graphics.Color.BLACK
                        valueTextSize = 15f // スコアのテキストサイズを設定
                    }

                    val lineData = LineData(
                        dataSetReading,
                        dataSetListening)
                    this.data = lineData

                    // X軸ラベル設定
                    xAxis.textSize = 15f
                    xAxis.valueFormatter = IndexAxisValueFormatter(examDates)
                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.granularity = 1f
                    xAxis.setDrawGridLines(false)

                    // Y軸設定
                    axisLeft.textSize = 15f
                    axisLeft.axisMinimum = 0f
                    axisRight.isEnabled = false
                    description.isEnabled = false
                    legend.isEnabled = true

                    // グラフの余白設定
                    setViewPortOffsets(
                        100f,
                        0f,
                        100f,
                        0f
                    )

                    Handler(Looper.getMainLooper()).postDelayed({
                        invalidate()
                    }, 100)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
    } else {
        // データがない場合のUI（何も表示しない or メッセージを表示）
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.material.Text(
                text = "まだスコアが登録されていません。",
                fontSize = 18.sp,
                color = androidx.compose.ui.graphics.Color.Gray
            )
        }
    }
}

@Composable
private fun ExamYearPicker(
    modifier: Modifier = Modifier,
    selectedExamYear: Int,
    onScoreConfirm: (Int) -> Unit,
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var examYear by rememberSaveable { mutableIntStateOf(selectedExamYear) }

    Box(modifier = modifier) {
        Button(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(width = 80.dp, height = 40.dp),
            onClick = { showDialog = true },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFFf5f5f5)),
        ) {
            androidx.compose.material.Text(
                text = "$selectedExamYear",
                color = androidx.compose.ui.graphics.Color.Black
            )
        }
    }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFd3d3d3)),
                modifier = Modifier
                    .size(width = 240.dp, height = 320.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    ExamYearPickerView(
                        score = examYear,
                        onScoreChange = { examYear = it }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            onScoreConfirm(examYear)
                            showDialog = false
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFF9C27B0)),
                    ) {
                        androidx.compose.material.Text(
                            text = "確定",
                            color = androidx.compose.ui.graphics.Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun ExamYearPickerView(
    score: Int,
    onScoreChange: (Int) -> Unit
) {
    val thousand = score / 1000
    val hundred = (score % 1000) / 100
    val ten = (score % 100) / 10
    val one = score % 10

    val thousandState = rememberSaveable { mutableIntStateOf(thousand) }
    val hundredState = rememberSaveable { mutableIntStateOf(hundred) }
    val tenState = rememberSaveable { mutableIntStateOf(ten) }
    val oneState = rememberSaveable { mutableIntStateOf(one) }

    LaunchedEffect(
        thousandState.intValue,
        hundredState.intValue,
        tenState.intValue,
        oneState.intValue
    ) {
        onScoreChange(
            thousandState.intValue * 1000 +
                    hundredState.intValue * 100 +
                    tenState.intValue * 10 +
                    oneState.intValue
        )
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ExamYearFourDigits(thousandState)
        ExamYearThreeDigits(hundredState)
        ExamYearTwoDigits(tenState)
        ExamYearOneDigit(oneState)
    }
}

@Composable
private fun ExamYearFourDigits(state: MutableIntState) {
    val items = listOf(2)

    // FWheelPickerStateを利用してスクロール状態を管理
    val listState = rememberFWheelPickerState()
    // currentIndex の変化を監視
    LaunchedEffect(listState.currentIndex) {
        // currentIndex が items のサイズを超えないことを確認
        val currentItemIndex = listState.currentIndex.coerceIn(0, items.size - 1)
        // listState.currentIndex が変わったときに state.intValue を更新
        state.intValue = items[currentItemIndex]
    }
    FVerticalWheelPicker(
        modifier = Modifier.width(48.dp),
        count = items.size,
        itemHeight = 48.dp,
        unfocusedCount = 2,
        state = listState,
        focus = {
            FWheelPickerFocusVertical(dividerColor = Color.White, dividerSize = 2.dp)
        },
    ) { index ->
        Text(
            items[index].toString(),
            color = Color.Black
        )
    }
}

@Composable
private fun ExamYearThreeDigits(state: MutableIntState) {
    val listState = rememberFWheelPickerState()

    LaunchedEffect(listState.currentIndex) {
        state.intValue = listState.currentIndex
    }
    FVerticalWheelPicker(
        modifier = Modifier.width(48.dp),
        count = 1,
        itemHeight = 48.dp,
        unfocusedCount = 2,
        state = listState,
        focus = {
            FWheelPickerFocusVertical(dividerColor = androidx.compose.ui.graphics.Color.White, dividerSize = 2.dp)
        },
    ) { index ->
        androidx.compose.material.Text(
            index.toString(),
            color = androidx.compose.ui.graphics.Color.Black
        )
    }
}

@Composable
private fun ExamYearTwoDigits(state: MutableIntState) {
    val listState = rememberFWheelPickerState()

    LaunchedEffect(listState.currentIndex) {
        state.intValue = listState.currentIndex
    }
    FVerticalWheelPicker(
        modifier = Modifier.width(48.dp),
        count = 10,
        itemHeight = 48.dp,
        unfocusedCount = 2,
        state = listState,
        focus = {
            FWheelPickerFocusVertical(dividerColor = androidx.compose.ui.graphics.Color.White, dividerSize = 2.dp)
        },
    ) { index ->
        androidx.compose.material.Text(
            index.toString(),
            color = androidx.compose.ui.graphics.Color.Black
        )
    }
}

@Composable
private fun ExamYearOneDigit(state: MutableIntState) {
    val listState = rememberFWheelPickerState()

    LaunchedEffect(listState.currentIndex) {
        state.intValue = listState.currentIndex
    }
    FVerticalWheelPicker(
        modifier = Modifier.width(48.dp),
        count = 10,
        itemHeight = 48.dp,
        unfocusedCount = 2,
        state = listState,
        focus = {
            FWheelPickerFocusVertical(dividerColor = androidx.compose.ui.graphics.Color.White, dividerSize = 2.dp)
        },
    ) { index ->
        androidx.compose.material.Text(
            index.toString(),
            color = androidx.compose.ui.graphics.Color.Black
        )
    }
}

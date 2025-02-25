package com.example.proenglishscoretracker.chart_screen

import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Minimize
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.proenglishscoretracker.R
import com.example.proenglishscoretracker.data.EnglishInfoViewModel
import com.example.proenglishscoretracker.wheel_picker.FVerticalWheelPicker
import com.example.proenglishscoretracker.wheel_picker.FWheelPickerFocusVertical
import com.example.proenglishscoretracker.wheel_picker.rememberFWheelPickerState
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener

@Composable
fun ToeicChartScreen(viewModel: EnglishInfoViewModel) {
    val toeicInfoList by viewModel.toeicInfo.collectAsState()
    val latestExamYear = toeicInfoList
        .mapNotNull { it.date.substring(0, 4).toIntOrNull() }
        .maxOrNull()
    var examYear by rememberSaveable { mutableStateOf<Int?>(null) }

    LaunchedEffect(latestExamYear) {
        if (latestExamYear != null && examYear == null) {
            examYear = latestExamYear
        }
    }
    if (examYear != null) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "受験年を選択", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))
            ExamYearPicker(
                modifier = Modifier,
                selectedExamYear = examYear!!,
                onScoreConfirm = { selectedYear ->
                    examYear = selectedYear
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
            ToeicScoreChart(
                viewModel,
                examYear!!,
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "まだスコアが登録されていません。",
                fontSize = 18.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun ToeicScoreChart(
    viewModel: EnglishInfoViewModel,
    examYear: Int
) {
    val toeicInfoList by viewModel.toeicInfo.collectAsState()
    var isGraphTapped by rememberSaveable { mutableStateOf(false) }

    if (toeicInfoList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "まだスコアが登録されていません。",
                fontSize = 18.sp,
                color = Color.Gray
            )
        }
    } else {
        val filteredToeicInfo = toeicInfoList.filter {
            it.date.substring(0, 4).toInt() == examYear
        }

        if (filteredToeicInfo.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "選択した年のデータがありません。",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
        } else {
            val sortedToeicInfo = filteredToeicInfo.sortedBy { it.date }
            val examDates = sortedToeicInfo.map { it.date }
            val readingScores = sortedToeicInfo.map { it.readingScore.toFloat() }
            val listeningScores = sortedToeicInfo.map { it.listeningScore.toFloat() }

            val entriesReading = readingScores.mapIndexed { index, score ->
                Entry(index.toFloat(), score)
            }
            val entriesListening = listeningScores.mapIndexed { index, score ->
                Entry(index.toFloat(), score)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                AndroidView(
                    update = {
                        it.data = updateData(entriesReading, entriesListening)
                        it.notifyDataSetChanged()
                        it.invalidate()
                    },
                    factory = { context ->
                        LineChart(context).apply {
                            val dataSetReading = LineDataSet(
                                entriesReading,
                                "Readingスコア"
                            ).apply {
                                color = android.graphics.Color.RED
                                valueTextColor = android.graphics.Color.BLACK
                                valueTextSize = 15f
                            }
                            val dataSetListening = LineDataSet(
                                entriesListening,
                                "Listeningスコア"
                            ).apply {
                                color = android.graphics.Color.BLUE
                                valueTextColor = android.graphics.Color.BLACK
                                valueTextSize = 15f
                            }

                            val lineData = LineData(
                                dataSetReading,
                                dataSetListening
                            )
                            this.data = lineData

                            xAxis.textSize = 15f
                            xAxis.valueFormatter = IndexAxisValueFormatter(examDates)
                            xAxis.position = XAxis.XAxisPosition.BOTTOM
                            xAxis.granularity = 1f
                            xAxis.setDrawGridLines(false)

                            axisLeft.textSize = 15f
                            axisLeft.axisMinimum = 0f
                            axisRight.isEnabled = false
                            description.isEnabled = false
                            legend.isEnabled = true

                            setViewPortOffsets(
                                120f,
                                0f,
                                120f,
                                0f
                            )

                            animateX(250, com.github.mikephil.charting.animation.Easing.Linear)

                            Handler(Looper.getMainLooper()).postDelayed({
                                setVisibleXRangeMaximum(2f)
                                moveViewToX(data.entryCount.toFloat())
                                invalidate()
                            }, 100)

                            setOnChartGestureListener(object : OnChartGestureListener {
                                override fun onChartGestureStart(
                                    me: MotionEvent?,
                                    lastPerformedGesture: ChartTouchListener.ChartGesture?
                                ) {
                                    isGraphTapped = true
                                }

                                override fun onChartGestureEnd(
                                    me: MotionEvent?,
                                    lastPerformedGesture: ChartTouchListener.ChartGesture?
                                ) {
                                    isGraphTapped = false
                                }

                                override fun onChartLongPressed(me: MotionEvent?) {}
                                override fun onChartDoubleTapped(me: MotionEvent?) {}
                                override fun onChartSingleTapped(me: MotionEvent?) {}
                                override fun onChartFling(
                                    me1: MotionEvent?,
                                    me2: MotionEvent?,
                                    velocityX: Float,
                                    velocityY: Float
                                ) {
                                }

                                override fun onChartScale(
                                    me: MotionEvent?,
                                    scaleX: Float,
                                    scaleY: Float
                                ) {
                                }

                                override fun onChartTranslate(
                                    me: MotionEvent?,
                                    dX: Float,
                                    dY: Float
                                ) {
                                }
                            })
                        }
                    },
                    modifier = Modifier
                        .matchParentSize()
                )
                if (isGraphTapped) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(8.dp)
                            .background(
                                Color.White.copy(
                                    alpha = 0.8f
                                ),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "Scroll",
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                        Image(
                            painter = painterResource(id = R.drawable.right_arrow),
                            contentDescription = "",
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = "Pinch In",
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                        Image(
                            painter = painterResource(id = R.drawable.pinch_in),
                            contentDescription = "",
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = "Pinch Out",
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                        Image(
                            painter = painterResource(id = R.drawable.pinch_out),
                            contentDescription = "",
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = "Scroll",
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                        Image(
                            painter = painterResource(id = R.drawable.left_arrow),
                            contentDescription = "",
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(64.dp))

            val currentReadingScore = readingScores.last().toInt()
            val previousReadingScore =
                readingScores.dropLast(1).lastOrNull()?.toInt() ?: currentReadingScore
            val currentListeningScore = listeningScores.last().toInt()
            val previousListeningScore =
                listeningScores.dropLast(1).lastOrNull()?.toInt() ?: currentListeningScore

            ConstraintLayout(
                modifier = Modifier.fillMaxWidth()
            ) {
                val guideline = createGuidelineFromStart(0.4f)
                val (readingLabel, readingScore, listeningLabel, listeningScore) = createRefs()

                Text(
                    text = "Readingスコア:",
                    modifier = Modifier.constrainAs(readingLabel) {
                        start.linkTo(parent.start, margin = 16.dp)
                        top.linkTo(parent.top, margin = 16.dp)
                    }
                )
                ComparePreviousScore(
                    modifier = Modifier.constrainAs(readingScore) {
                        start.linkTo(guideline)
                        top.linkTo(readingLabel.top)
                    },
                    currentScore = currentReadingScore,
                    previousScore = previousReadingScore
                )
                Text(
                    text = "Listeningスコア:",
                    modifier = Modifier.constrainAs(listeningLabel) {
                        start.linkTo(parent.start, margin = 16.dp)
                        top.linkTo(readingLabel.bottom, margin = 16.dp)
                    }
                )
                ComparePreviousScore(
                    modifier = Modifier.constrainAs(listeningScore) {
                        start.linkTo(guideline)
                        top.linkTo(listeningLabel.top)
                    },
                    currentScore = currentListeningScore,
                    previousScore = previousListeningScore
                )
            }
        }
    }
}

private fun updateData(
    entriesReading: List<Entry>,
    entriesListening: List<Entry>
): LineData {
    val dataSetReading = LineDataSet(
        entriesReading, "Readingスコア"
    ).apply {
        color = android.graphics.Color.RED
        valueTextColor = android.graphics.Color.BLACK
        valueTextSize = 15f
    }
    val dataSetListening = LineDataSet(
        entriesListening, "Listeningスコア"
    ).apply {
        color = android.graphics.Color.BLUE
        valueTextColor = android.graphics.Color.BLACK
        valueTextSize = 15f
    }
    return LineData(dataSetReading, dataSetListening)
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
                .size(
                    width = 100.dp,
                    height = 60.dp
                ),
            onClick = { showDialog = true },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFFf5f5f5)),
        ) {
            Text(
                text = "$selectedExamYear",
                fontSize = 20.sp,
                color = Color.Black
            )
        }
    }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFffffff)),
                modifier = Modifier
                    .size(
                        width = 240.dp,
                        height = 320.dp
                    )
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
                        Text(
                            text = "確定",
                            color = Color.White
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

    val thousandState = remember { mutableIntStateOf(thousand) }
    val hundredState = remember { mutableIntStateOf(hundred) }
    val tenState = remember { mutableIntStateOf(ten) }
    val oneState = remember { mutableIntStateOf(one) }

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
    val listState = rememberFWheelPickerState()

    LaunchedEffect(listState.currentIndex) {
        val currentItemIndex = listState.currentIndex.coerceIn(0, items.size - 1)
        state.intValue = items[currentItemIndex]
    }
    FVerticalWheelPicker(
        modifier = Modifier.width(48.dp),
        count = items.size,
        itemHeight = 48.dp,
        unfocusedCount = 2,
        state = listState,
        focus = {
            FWheelPickerFocusVertical(
                dividerColor = Color.LightGray,
                dividerSize = 2.dp
            )
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
            FWheelPickerFocusVertical(
                dividerColor = Color.LightGray,
                dividerSize = 2.dp
            )
        },
    ) { index ->
        Text(
            index.toString(),
            color = Color.Black
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
            FWheelPickerFocusVertical(
                dividerColor = Color.LightGray,
                dividerSize = 2.dp
            )
        },
    ) { index ->
        Text(
            index.toString(),
            color = Color.Black
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
            FWheelPickerFocusVertical(
                dividerColor = Color.LightGray,
                dividerSize = 2.dp
            )
        },
    ) { index ->
        Text(
            index.toString(),
            color = Color.Black
        )
    }
}

@Composable
private fun ComparePreviousScore(
    modifier: Modifier,
    currentScore: Int,
    previousScore: Int
) {
    val scoreDifference = currentScore - previousScore
    val (icon, color, message) = when {
        scoreDifference > 0 -> {
            Triple(
                Icons.Default.KeyboardArrowUp,
                Color.Blue,
                "前回より${scoreDifference}点上がっています。"
            )
        }
        scoreDifference < 0 -> {
            Triple(
                Icons.Default.KeyboardArrowDown,
                Color.Red,
                "前回より${-scoreDifference}点下がっています。"
            )
        }
        else -> {
            Triple(
                Icons.Default.Minimize,
                Color.Gray,
                "前回と同じスコアです。"
            )
        }
    }
    ConstraintLayout(
        modifier = modifier
    ) {
        val (iconRef, textRef) = createRefs()
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.constrainAs(iconRef) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
            }
        )
        Text(
            text = message,
            color = color,
            modifier = Modifier.constrainAs(textRef) {
                start.linkTo(iconRef.end, margin = 8.dp)
                top.linkTo(parent.top)
                end.linkTo(parent.end)
            }
        )
    }
}

package com.example.proenglishscoretracker.chart_screen

import android.view.ViewGroup
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.proenglishscoretracker.R
import com.example.proenglishscoretracker.data.EnglishInfoViewModel
import com.example.proenglishscoretracker.wheel_picker.FVerticalWheelPicker
import com.example.proenglishscoretracker.wheel_picker.FWheelPickerFocusVertical
import com.example.proenglishscoretracker.wheel_picker.rememberFWheelPickerState
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener

@Composable
fun ToeicSwChartScreen(viewModel: EnglishInfoViewModel) {
    val toeicSwInfoList by viewModel.toeicSwInfo.collectAsState()

    // 最新の受験年を取得
    val latestExamYear = toeicSwInfoList
        .mapNotNull { it.date.substring(0, 4).toIntOrNull() }
        .maxOrNull() ?: 0 // データがない場合は0を設定

    // examYearの初期値を最新の受験年に設定
    var examYear by rememberSaveable { mutableIntStateOf(latestExamYear) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "受験年を選択", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))
        ExamYearPicker(
            modifier = Modifier,
            selectedExamYear = examYear,
            onScoreConfirm = { selectedYear ->
                examYear = selectedYear
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        ToeicSwScoreChart(viewModel, examYear)
    }
}

@Composable
private fun ToeicSwScoreChart(
    viewModel: EnglishInfoViewModel,
    examYear: Int
) {
    val toeicSwInfoList by viewModel.toeicSwInfo.collectAsState()
    var isGraphTapped by rememberSaveable { mutableStateOf(false) }

    // データが存在しない場合のメッセージ
    if (toeicSwInfoList.isEmpty()) {
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
        // examYear に基づいてデータをフィルタリング
        val filteredToeicSwInfo = toeicSwInfoList.filter {
            it.date.substring(0, 4).toInt() == examYear
        }

        if (filteredToeicSwInfo.isEmpty()) {
            // 選択した年のデータがない場合のメッセージ
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "選択した年のデータがありません。",
                    fontSize = 18.sp,
                    color = androidx.compose.ui.graphics.Color.Gray
                )
            }
        } else {
            // 受験日を昇順（古い順）にソート
            val sortedToeicSwInfo = filteredToeicSwInfo.sortedBy { it.date }

            // ソートされたデータから必要な情報を抽出
            val examDates = sortedToeicSwInfo.map { it.date }
            val writingScores = sortedToeicSwInfo.map { it.writingScore.toFloat() }
            val speakingScores = sortedToeicSwInfo.map { it.speakingScore.toFloat() }

            val entriesWriting = writingScores.mapIndexed { index, score ->
                Entry(index.toFloat(), score)
            }
            val entriesSpeaking = speakingScores.mapIndexed { index, score ->
                Entry(index.toFloat(), score)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ){
                AndroidView(
                    update = {
                        it.data = updateData(entriesWriting, entriesSpeaking)
                        it.notifyDataSetChanged()
                        it.invalidate()
                    },
                    factory = { context ->
                        LineChart(context).apply {
//                            layoutParams = ViewGroup.LayoutParams(
//                                ViewGroup.LayoutParams.MATCH_PARENT,
//                                ViewGroup.LayoutParams.MATCH_PARENT
//                            )

                            val dataSetWriting = LineDataSet(
                                entriesWriting, "Writingスコア"
                            ).apply {
                                color = android.graphics.Color.RED
                                valueTextColor = android.graphics.Color.BLACK
                                valueTextSize = 15f // スコアのテキストサイズを設定
                                // mode = LineDataSet.Mode.CUBIC_BEZIER // 曲線
                            }
                            val dataSetSpeaking = LineDataSet(
                                entriesSpeaking, "Speakingスコア"
                            ).apply {
                                color = android.graphics.Color.BLUE
                                valueTextColor = android.graphics.Color.BLACK
                                valueTextSize = 15f // スコアのテキストサイズを設定
                                // mode = LineDataSet.Mode.CUBIC_BEZIER // 曲線
                            }

                            val lineData = LineData(dataSetWriting, dataSetSpeaking)
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
                                120f,
                                0f,
                                120f,
                                0f
                            )

                            // 左から右に表示するアニメーションを追加。
                            this.animateX(250, com.github.mikephil.charting.animation.Easing.Linear)

                            Handler(Looper.getMainLooper()).postDelayed({
                                setVisibleXRangeMaximum(2f)
                                invalidate()
                            }, 100)

                            // グラフのタップイベントをリスナーで検知
                            setOnChartGestureListener(object : OnChartGestureListener {
                                override fun onChartGestureStart(
                                    me: MotionEvent?,
                                    lastPerformedGesture: ChartTouchListener.ChartGesture?
                                ) {
                                    isGraphTapped = true // タップ開始時に表示
                                }

                                override fun onChartGestureEnd(
                                    me: MotionEvent?,
                                    lastPerformedGesture: ChartTouchListener.ChartGesture?
                                ) {
                                    isGraphTapped = false // タップ終了時に非表示
                                }

                                override fun onChartLongPressed(me: MotionEvent?) {}
                                override fun onChartDoubleTapped(me: MotionEvent?) {}
                                override fun onChartSingleTapped(me: MotionEvent?) {}
                                override fun onChartFling(
                                    me1: MotionEvent?,
                                    me2: MotionEvent?,
                                    velocityX: Float,
                                    velocityY: Float
                                ) {}

                                override fun onChartScale(
                                    me: MotionEvent?,
                                    scaleX: Float,
                                    scaleY: Float
                                ) {}

                                override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {}
                            })
                        }
                    },
                    modifier = Modifier
                        .matchParentSize()
                )
                // タップ時に表示する UI（グラフの上に重ねる）
                if (isGraphTapped) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(8.dp)
                            .background(Color.White.copy(alpha = 0.8f), shape = RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Text(text = "Scroll", fontSize = 12.sp, color = Color.Black)
                        Image(
                            painter = painterResource(id = R.drawable.right_arrow),
                            contentDescription = "",
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_40_dp)))
                        Text(text = "Pinch In", fontSize = 12.sp, color = Color.Black)
                        Image(
                            painter = painterResource(id = R.drawable.pinch_in),
                            contentDescription = "",
                        )
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.space_40_dp)))
                        Text(text = "Pinch Out", fontSize = 12.sp, color = Color.Black)
                        Image(
                            painter = painterResource(id = R.drawable.pinch_out),
                            contentDescription = "",
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(64.dp))

            // 前回のスコアとの比較を表示
            val currentWritingScore =
                writingScores.last().toInt()
            val previousWritingScore =
                writingScores.dropLast(1).lastOrNull()?.toInt() ?: currentWritingScore
            val currentSpeakingScore =
                speakingScores.last().toInt()
            val previousSpeakingScore =
                speakingScores.dropLast(1).lastOrNull()?.toInt() ?: currentSpeakingScore

            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Writingスコア:")
                    ComparePreviousScore(
                        currentScore = currentWritingScore,
                        previousScore = previousWritingScore
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Speakingスコア:")
                    ComparePreviousScore(
                        currentScore = currentSpeakingScore,
                        previousScore = previousSpeakingScore
                    )
                }
            }
        }
    }
}

private fun updateData(
    entriesWriting: List<Entry>,
    entriesSpeaking: List<Entry>
): LineData {
    val dataSetWriting = LineDataSet(
        entriesWriting, "Writingスコア"
    ).apply {
        color = android.graphics.Color.RED
        valueTextColor = android.graphics.Color.BLACK
        valueTextSize = 15f // スコアのテキストサイズを設定
    }
    val dataSetSpeaking = LineDataSet(
        entriesSpeaking, "Speakingスコア"
    ).apply {
        color = android.graphics.Color.BLUE
        valueTextColor = android.graphics.Color.BLACK
        valueTextSize = 15f // スコアのテキストサイズを設定
    }
    return LineData(dataSetWriting, dataSetSpeaking)
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
                .size(width = 100.dp, height = 60.dp),
            onClick = { showDialog = true },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFFf5f5f5)),
        ) {
            Text(
                text = "$selectedExamYear",
                fontSize = 20.sp,
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
                        Text(
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
            FWheelPickerFocusVertical(dividerColor = androidx.compose.ui.graphics.Color.White, dividerSize = 2.dp)
        },
    ) { index ->
        Text(
            items[index].toString(),
            color = androidx.compose.ui.graphics.Color.Black
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
        Text(
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
        Text(
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
        Text(
            index.toString(),
            color = androidx.compose.ui.graphics.Color.Black
        )
    }
}

@Composable
private fun ComparePreviousScore(
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

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = message, color = color)
    }
}

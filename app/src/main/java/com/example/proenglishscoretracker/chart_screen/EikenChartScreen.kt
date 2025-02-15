package com.example.proenglishscoretracker.chart_screen

import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.proenglishscoretracker.data.EnglishInfoViewModel
import com.example.proenglishscoretracker.wheel_picker.FVerticalWheelPicker
import com.example.proenglishscoretracker.wheel_picker.FWheelPickerFocusVertical
import com.example.proenglishscoretracker.wheel_picker.rememberFWheelPickerState
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

@Composable
fun EikenChartScreen(viewModel: EnglishInfoViewModel) {
    val eikenInfoList by viewModel.eikenSecondInfo.collectAsState()

    // 最新の受験年を取得
    val latestExamYear = eikenInfoList
        .mapNotNull { it.date.substring(0, 4).toIntOrNull() }
        .maxOrNull() ?: 0 // データがない場合は0を設定

    // examYearの初期値を最新の受験年に設定
    var examYear by rememberSaveable { mutableIntStateOf(latestExamYear) }
    val grades = listOf("5級", "4級", "3級", "準2級", "2級", "準1級", "1級")
    var selectedGrade by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val guideline = createGuidelineFromStart(0.3f)
            val (yearText, yearPicker, gradeText, gradeDropdown) = createRefs()

            Text(
                text = "受験年を選択",
                fontSize = 20.sp,
                modifier = Modifier.constrainAs(yearText) {
                    start.linkTo(guideline)
                    top.linkTo(parent.top, margin = 28.dp)
                }
            )

            ExamYearPicker(
                selectedExamYear = examYear,
                onScoreConfirm = { selectedYear ->
                    examYear = selectedYear
                },
                modifier = Modifier.constrainAs(yearPicker) {
                    start.linkTo(yearText.end, margin = 20.dp)
                    top.linkTo(yearText.top)
                    bottom.linkTo(yearText.bottom)
                }
            )

            Text(
                text = "受験級を選択",
                fontSize = 20.sp,
                modifier = Modifier.constrainAs(gradeText) {
                    start.linkTo(guideline)
                    top.linkTo(yearText.bottom, margin = 28.dp)
                }
            )

            // TODO : 以下の「DropdownMenuWithIcon」をPiano Appのコードから拝借して修正予定。
            DropdownMenuWithIcon(
                modifier = Modifier.constrainAs(gradeDropdown) {
                    start.linkTo(gradeText.end, margin = 20.dp)
                    top.linkTo(gradeText.top)
                    bottom.linkTo(gradeText.bottom)
                },
                grades = grades,
                onGradeSelected = { grade ->
                    selectedGrade = grade
                },
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        EikenScoreChart(viewModel, examYear)
    }
}

@Composable
private fun DropdownMenuWithIcon(
    modifier: Modifier = Modifier,
    grades: List<String>,
    onGradeSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }

    Box(
        modifier = modifier
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = modifier
                .wrapContentSize(Alignment.Center)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .clickable { expanded = !expanded }
                    .padding(8.dp)
            ) {
                Text(
                    text = grades[selectedIndex],
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = modifier.width(8.dp))
                Icon(
                    imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    tint = Color.Black,
                    modifier = modifier.size(24.dp)
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                offset = DpOffset(0.dp, 8.dp)
            ) {
                grades.forEachIndexed { index, item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            selectedIndex = index
                            onGradeSelected(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun EikenScoreChart(
    viewModel: EnglishInfoViewModel,
    examYear: Int
) {
    val eikenInfoList by viewModel.eikenSecondInfo.collectAsState()

    // データが存在しない場合のメッセージ
    if (eikenInfoList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "まだスコアが登録されていません。",
                fontSize = 18.sp,
                color = androidx.compose.ui.graphics.Color.Gray
            )
        }
    } else {
        // examYear に基づいてデータをフィルタリング
        val filteredEikenInfo = eikenInfoList.filter {
            it.date.substring(0, 4).toInt() == examYear }

        if (filteredEikenInfo.isEmpty()) {
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
            val sortedEikenInfo = filteredEikenInfo.sortedBy { it.date }

            // ソートされたデータから必要な情報を抽出
            val examDates = sortedEikenInfo.map { it.date }
            val cseScores = sortedEikenInfo.map { it.cseScore.toFloat() }
            val readingScores = sortedEikenInfo.map { it.readingScore.toFloat() }
            val listeningScores = sortedEikenInfo.map { it.listeningScore.toFloat() }
            val writingScores = sortedEikenInfo.map { it.writingScore.toFloat() }
            val speakingScores = sortedEikenInfo.map { it.speakingScore.toFloat() }

            val entriesCse = cseScores.mapIndexed { index, score ->
                Entry(index.toFloat(), score)
            }
            val entriesReading = readingScores.mapIndexed { index, score ->
                Entry(index.toFloat(), score)
            }
            val entriesListening = listeningScores.mapIndexed { index, score ->
                Entry(index.toFloat(), score)
            }
            val entriesWriting = writingScores.mapIndexed { index, score ->
                Entry(index.toFloat(), score)
            }
            val entriesSpeaking = speakingScores.mapIndexed { index, score ->
                Entry(index.toFloat(), score)
            }

            // CSEスコアのグラフ
            AndroidView(
                update = {
                    it.data = updateCseData(
                        entriesCse
                    )
                    it.notifyDataSetChanged()
                    it.invalidate()
                },
                factory = { context ->
                    LineChart(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )

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
                        this.animateX(250, com.github.mikephil.charting.animation.Easing.Linear)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            Spacer(modifier = Modifier.height(64.dp))

            val currentCseScore =
                cseScores.last().toInt()
            val previousCSEScore =
                cseScores.dropLast(1).lastOrNull()?.toInt() ?: currentCseScore

            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "CSEスコア:")
                    ComparePreviousScore(
                        currentScore = currentCseScore,
                        previousScore = previousCSEScore
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 4技能のグラフ
            AndroidView(
                update = {
                    it.data = updateRLWSData(
                        entriesReading,
                        entriesListening,
                        entriesWriting,
                        entriesSpeaking
                    )
                    it.notifyDataSetChanged()
                    it.invalidate()
                },
                factory = { context ->
                    LineChart(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )

                        val dataSetReading = LineDataSet(
                            entriesReading, "Readingスコア"
                        ).apply {
                            color = android.graphics.Color.RED
                            valueTextColor = android.graphics.Color.BLACK
                            valueTextSize = 15f // スコアのテキストサイズを設定
//                            mode = LineDataSet.Mode.CUBIC_BEZIER // 曲線
                        }
                        val dataSetListening = LineDataSet(
                            entriesListening, "Listeningスコア"
                        ).apply {
                            color = android.graphics.Color.BLUE
                            valueTextColor = android.graphics.Color.BLACK
                            valueTextSize = 15f // スコアのテキストサイズを設定
//                            mode = LineDataSet.Mode.CUBIC_BEZIER // 曲線
                        }
                        val dataSetWriting = LineDataSet(
                            entriesWriting, "Writingスコア"
                        ).apply {
                            color = android.graphics.Color.GREEN
                            valueTextColor = android.graphics.Color.BLACK
                            valueTextSize = 15f // スコアのテキストサイズを設定
//                            mode = LineDataSet.Mode.CUBIC_BEZIER // 曲線
                        }
                        val dataSetSpeaking = LineDataSet(
                            entriesSpeaking, "Speakingスコア"
                        ).apply {
                            color = android.graphics.Color.MAGENTA
                            valueTextColor = android.graphics.Color.BLACK
                            valueTextSize = 15f // スコアのテキストサイズを設定
//                            mode = LineDataSet.Mode.CUBIC_BEZIER // 曲線
                        }

                        val lineData = LineData(
                            dataSetReading,
                            dataSetListening,
                            dataSetWriting,
                            dataSetSpeaking
                        )
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
                            invalidate()
                        }, 100)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            Spacer(modifier = Modifier.height(64.dp))

            // 前回のスコアとの比較を表示
            val currentReadingScore =
                readingScores.last().toInt()
            val previousReadingScore =
                readingScores.dropLast(1).lastOrNull()?.toInt() ?: currentReadingScore
            val currentListeningScore =
                listeningScores.last().toInt()
            val previousListeningScore =
                listeningScores.dropLast(1).lastOrNull()?.toInt() ?: currentListeningScore
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
                    Text(text = "Readingスコア:")
                    ComparePreviousScore(
                        currentScore = currentReadingScore,
                        previousScore = previousReadingScore
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Listeningスコア:")
                    ComparePreviousScore(
                        currentScore = currentListeningScore,
                        previousScore = previousListeningScore
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

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

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

private fun updateCseData(
    entriesOverall: List<Entry>,
): LineData {
    val dataSetOverall = LineDataSet(
        entriesOverall, "Overallスコア"
    ).apply {
        color = android.graphics.Color.CYAN
        valueTextColor = android.graphics.Color.BLACK
        valueTextSize = 15f // スコアのテキストサイズを設定
    }
    return LineData(
        dataSetOverall,
    )
}

private fun updateRLWSData(
    entriesReading: List<Entry>,
    entriesListening: List<Entry>,
    entriesWriting: List<Entry>,
    entriesSpeaking: List<Entry>
): LineData {
    val dataSetReading = LineDataSet(
        entriesReading, "Readingスコア"
    ).apply {
        color = android.graphics.Color.RED
        valueTextColor = android.graphics.Color.BLACK
        valueTextSize = 15f // スコアのテキストサイズを設定
    }
    val dataSetListening = LineDataSet(
        entriesListening, "Listeningスコア"
    ).apply {
        color = android.graphics.Color.BLUE
        valueTextColor = android.graphics.Color.BLACK
        valueTextSize = 15f // スコアのテキストサイズを設定
    }
    val dataSetWriting = LineDataSet(
        entriesWriting, "Writingスコア"
    ).apply {
        color = android.graphics.Color.GREEN
        valueTextColor = android.graphics.Color.BLACK
        valueTextSize = 15f // スコアのテキストサイズを設定
    }
    val dataSetSpeaking = LineDataSet(
        entriesSpeaking, "Speakingスコア"
    ).apply {
        color = android.graphics.Color.MAGENTA
        valueTextColor = android.graphics.Color.BLACK
        valueTextSize = 15f // スコアのテキストサイズを設定
    }
    return LineData(
        dataSetReading,
        dataSetListening,
        dataSetWriting,
        dataSetSpeaking
    )
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

package com.example.proenglishscoretracker.chart_screen

import android.view.ViewGroup
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.proenglishscoretracker.data.EnglishInfoViewModel
import com.example.proenglishscoretracker.data.EnglishTestInfo
import com.example.proenglishscoretracker.ui.theme.ProEnglishScoreTrackerTheme
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ToeicChartScreen(viewModel: EnglishInfoViewModel) {
    ToeicScoreChart(viewModel)
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
                        color = Color.RED
                        valueTextColor = Color.BLACK
                    }
                    val dataSetListening = LineDataSet(
                        entriesListening,
                        "Listeningスコア").apply {
                        color = Color.BLUE
                        valueTextColor = Color.BLACK
                    }

                    val lineData = LineData(
                        dataSetReading,
                        dataSetListening)
                    this.data = lineData

                    // X軸ラベル設定
                    xAxis.valueFormatter = IndexAxisValueFormatter(examDates)
                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.granularity = 1f
                    xAxis.setDrawGridLines(false)

                    // Y軸設定
                    axisLeft.axisMinimum = 0f
                    axisRight.isEnabled = false
                    description.isEnabled = false
                    legend.isEnabled = true

                    // グラフの余白設定
                    setViewPortOffsets(75f, 0f, 75f, 0f)

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

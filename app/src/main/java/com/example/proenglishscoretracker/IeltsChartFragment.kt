package com.example.proenglishscoretracker

import android.view.ViewGroup
import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.tooling.preview.Preview
import com.example.proenglishscoretracker.ui.theme.ProEnglishScoreTrackerTheme

@Composable
fun IeltsChartScreen() {
    IeltsScoreChart()
}

@Preview(showBackground = true)
@Composable
private fun IeltsChartScreenPreview() {
    IeltsChartScreen()
}

@Composable
private fun IeltsScoreChart() {
    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                // データのセットアップ
                val readingScores = listOf(8.5f, 8.0f, 9.0f)
                val listeningScores = listOf(8.0f, 9.0f, 8.5f)
                val writingScores = listOf(6.0f, 6.5f, 7.0f)
                val speakingScores = listOf(7.0f, 6.0f, 6.5f)

                val examDates = listOf("2023-09-01", "2023-10-01", "2023-11-01")

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

                val dataSetReading = LineDataSet(entriesReading, "リーディングスコア").apply {
                    color = Color.RED
                    valueTextColor = Color.BLACK
                }
                val dataSetListening = LineDataSet(entriesListening, "リスニングスコア").apply {
                    color = Color.BLUE
                    valueTextColor = Color.BLACK
                }
                val dataSetWriting = LineDataSet(entriesWriting, "ライティングスコア").apply {
                    color = Color.YELLOW
                    valueTextColor = Color.BLACK
                }
                val dataSetSpeaking = LineDataSet(entriesSpeaking, "スピーキングスコア").apply {
                    color = Color.GREEN
                    valueTextColor = Color.BLACK
                }

                val lineData = LineData(dataSetReading, dataSetListening, dataSetWriting, dataSetSpeaking)
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
                invalidate() // グラフの再描画
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun ScoreChartPreview() {
    ProEnglishScoreTrackerTheme {
        IeltsScoreChart()
    }
}

package com.example.proenglishscoretracker.chart_screen

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
fun EikenIchijiChartScreen() {
    EikenIchijiScoreChart()
}

@Preview(showBackground = true)
@Composable
private fun EikenIchijiChartScreenPreview() {
    EikenIchijiChartScreen()
}

@Composable
private fun EikenIchijiScoreChart() {
    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                // データのセットアップ
                val readingScores = listOf(690f, 710f, 800f)
                val listeningScores = listOf(700f, 650f, 850f)
                val writingScores = listOf(750f, 730f, 790f)

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

                val lineData = LineData(dataSetReading, dataSetListening, dataSetWriting)
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
        EikenIchijiScoreChart()
    }
}

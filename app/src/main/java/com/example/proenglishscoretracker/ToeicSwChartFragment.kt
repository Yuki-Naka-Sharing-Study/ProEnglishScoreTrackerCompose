package com.example.proenglishscoretracker

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.proenglishscoretracker.ui.theme.ProEnglishScoreTrackerTheme

class ToeicSwChartFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val composeView = ComposeView(requireContext()).apply {
            setContent {
                ToeicSwChartScreen()
            }
        }
        return composeView
    }
}

@Composable
fun ToeicSwChartScreen() {
    ToeicSwScoreChart()
}

@Preview(showBackground = true)
@Composable
private fun ToeicSwChartScreenPreview() {
    ToeicSwChartScreen()
}

@Composable
private fun ToeicSwScoreChart() {
    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                // データのセットアップ
                val writingScores = listOf(150f, 180f, 200f)
                val speakingScores = listOf(180f, 170f, 190f)

                val examDates = listOf("2023-09-01", "2023-10-01", "2023-11-01")

                val entriesWriting = writingScores.mapIndexed { index, score ->
                    Entry(index.toFloat(), score)
                }
                val entriesSpeaking = speakingScores.mapIndexed { index, score ->
                    Entry(index.toFloat(), score)
                }


                val dataSetWriting = LineDataSet(entriesWriting, "ライティングスコア").apply {
                    color = Color.YELLOW
                    valueTextColor = Color.BLACK
                }
                val dataSetSpeaking = LineDataSet(entriesSpeaking, "スピーキングスコア").apply {
                    color = Color.BLUE
                    valueTextColor = Color.BLACK
                }

                val lineData = LineData(dataSetWriting, dataSetSpeaking)
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
        ToeicSwScoreChart()
    }
}

package com.example.proenglishscoretracker.settings

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import java.util.Calendar
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import java.util.Date
import java.util.Locale
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.navigation.NavController
import androidx.work.*
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

// 各試験の設定情報
data class ExamSetting(val name: String, val prefKey: String)

@Composable
fun CountdownSettingsScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("exam_prefs", Context.MODE_PRIVATE)
    val workManager = WorkManager.getInstance(context)

    // 設定対象の試験一覧
    val examSettings = listOf(
        ExamSetting("TOEIC", "exam_toeic"),
        ExamSetting("TOEIC SW", "exam_toeicsw"),
        ExamSetting("英検", "exam_eiken"),
        ExamSetting("TOEFL iBT", "exam_toeflibt"),
        ExamSetting("IELTS", "exam_ielts")
    )

    // 初回表示時に通知用のWorkerをスケジュール
    LaunchedEffect(Unit) {
        scheduleExamCountdown(workManager)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "受験日カウントダウン設定",
            fontSize = 24.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        examSettings.forEach { setting ->
            ExamCountdownSettingItem(
                setting = setting,
                sharedPreferences = sharedPreferences,
                workManager = workManager
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ExamCountdownSettingItem(
    setting: ExamSetting,
    sharedPreferences: SharedPreferences,
    workManager: WorkManager
) {
    val context = LocalContext.current
    // SharedPreferences に保存されている受験日（ミリ秒）
    val savedExamDate = sharedPreferences.getLong(setting.prefKey, 0L)
    // Switch の状態は、受験日が設定されていれば ON とする
    var isEnabled by remember { mutableStateOf(savedExamDate > 0L) }
    var examDate by remember { mutableStateOf(savedExamDate) }

    val dateFormatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    val formattedDate =
        if (examDate > 0L) dateFormatter.format(Date(examDate))
        else "未設定"

    // 公式HPのURLを設定
    val examUrls = mapOf(
        "TOEIC" to "https://www.toeic.or.jp/",
        "TOEIC SW" to "https://www.iibc-global.org/toeic/test/sw.html",
        "英検" to "https://www.eiken.or.jp/",
        "TOEFL iBT" to "https://www.ets.org/toefl",
        "IELTS" to "https://www.ielts.org/"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = setting.name, modifier = Modifier.weight(1f))
            Switch(
                checked = isEnabled,
                onCheckedChange = { checked ->
                    isEnabled = checked
                    if (!checked) {
                        examDate = 0L
                        sharedPreferences.edit().remove(setting.prefKey).apply()
                        scheduleExamCountdown(workManager)
                    }
                }
            )
        }
        if (isEnabled) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "受験日: $formattedDate",
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = {
                    // DatePickerDialog を表示するため、context を Activity にキャスト
                    val activity = context as? Activity
                    val calendar = Calendar.getInstance()
                    activity?.let {
                        DatePickerDialog(
                            it,
                            { _, year, month, dayOfMonth ->
                                val selectedCalendar = Calendar.getInstance().apply {
                                    set(year, month, dayOfMonth, 0, 0, 0)
                                }
                                examDate = selectedCalendar.timeInMillis
                                sharedPreferences.edit().putLong(setting.prefKey, examDate).apply()
                                scheduleExamCountdown(workManager)
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }
                }) {
                    Text(text = "受験日を設定")
                }

                // 公式HPに遷移するボタン
                Button(onClick = {
                    val url = examUrls[setting.name] ?: return@Button
                    // 公式HPを表示するためのWebViewActivityに遷移
                    val intent = Intent(context, WebViewActivity::class.java).apply {
                        putExtra("url", url)
                    }
                    intent.putExtra("URL", url) // URLを渡す
                    context.startActivity(intent)
                }) {
                    Text(text = "公式HPを開く")
                }
            }
        }
    }
}

// WorkManager の定期実行をスケジュール（1日ごとに実行）
fun scheduleExamCountdown(workManager: WorkManager) {
    val request = PeriodicWorkRequestBuilder<MultiExamCountdownWorker>(1, TimeUnit.DAYS)
        .setInitialDelay(1, TimeUnit.DAYS)
        .build()
    workManager.enqueueUniquePeriodicWork(
        "multi_exam_countdown",
        ExistingPeriodicWorkPolicy.REPLACE,
        request
    )
}

// 複数試験の受験日カウントダウンをチェックし、通知を送信する Worker
class MultiExamCountdownWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    // 各試験の設定情報（UI側と同じキーを使用）
    private val examSettings = listOf(
        ExamSetting("TOEIC", "exam_toeic"),
        ExamSetting("TOEIC SW", "exam_toeicsw"),
        ExamSetting("英検", "exam_eiken"),
        ExamSetting("TOEFL iBT", "exam_toeflibt"),
        ExamSetting("IELTS", "exam_ielts")
    )

    override fun doWork(): Result {
        val sharedPreferences = applicationContext.getSharedPreferences("exam_prefs", Context.MODE_PRIVATE)
        val now = Calendar.getInstance().timeInMillis
        examSettings.forEach { exam ->
            val examDate = sharedPreferences.getLong(exam.prefKey, 0L)
            if (examDate > 0) {
                val daysLeft = ((examDate - now) / (1000 * 60 * 60 * 24)).toInt()
                if (daysLeft >= 0) {
                    sendNotification(exam.name, daysLeft)
                }
            }
        }
        return Result.success()
    }

    private fun sendNotification(examName: String, daysLeft: Int) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "multi_exam_countdown_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "試験カウントダウン通知",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("$examName 試験まであと $daysLeft 日")
            .setContentText("受験日カウントダウン中です")
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(examName.hashCode(), notification)
    }
}

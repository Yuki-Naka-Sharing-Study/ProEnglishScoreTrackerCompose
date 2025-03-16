package com.example.proenglishscoretracker.settings

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
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun ExpireCountdownScreen(
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("expire_prefs", Context.MODE_PRIVATE)
    val workManager = WorkManager.getInstance(context)

    val examSettings = listOf(
        ExamSetting("TOEIC", "exam_toeic"),
        ExamSetting("TOEIC SW", "exam_toeicsw"),
        ExamSetting("TOEFL iBT", "exam_toeflibt"),
        ExamSetting("IELTS", "exam_ielts")
    )

    LaunchedEffect(Unit) {
        scheduleExpireCountdown(workManager)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("有効期限カウントダウン", fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "戻る",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
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
}

@Composable
private fun ExamCountdownSettingItem(
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

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = setting.name, modifier = Modifier.weight(1f))
            Switch(
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF8A2BE2),
                    uncheckedThumbColor = Color.Gray,
                    checkedTrackColor = Color(0xFF8A2BE2),
                    uncheckedTrackColor = Color.LightGray
                ),
                checked = isEnabled,
                onCheckedChange = { checked ->
                    isEnabled = checked
                    if (!checked) {
                        examDate = 0L
                        sharedPreferences.edit().remove(setting.prefKey).apply()
                        scheduleExpireCountdown(workManager)
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
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = "有効期限失効日:\n$formattedDate",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = {
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
                                scheduleExpireCountdown(workManager)
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }
                }) {
                    Text(text = "有効期限日カウントダウン通知を有効")
                }
            }
        }
    }
}

// WorkManager の定期実行をスケジュール（1日ごとに実行）
private fun scheduleExpireCountdown(workManager: WorkManager) {
    val request = PeriodicWorkRequestBuilder<MultiExamCountdownWorker>(1, TimeUnit.DAYS)
        .setInitialDelay(1, TimeUnit.DAYS)
        .build()
    workManager.enqueueUniquePeriodicWork(
        "multi_exam_countdown",
        ExistingPeriodicWorkPolicy.REPLACE,
        request
    )
}

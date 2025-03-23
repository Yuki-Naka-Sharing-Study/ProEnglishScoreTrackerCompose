package com.example.proenglishscoretracker.settings

import android.content.Context
import android.os.Build
import android.app.Activity
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavHostController
import androidx.work.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

data class ExamSetting(val name: String, val prefKey: String)

// DataStore のインスタンスを取得
val Context.dataStore by preferencesDataStore(name = "exam_prefs")

@Composable
fun ExamDayCountdownScreen(
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val workManager = WorkManager.getInstance(context)

    val examSettings = listOf(
        ExamSetting("TOEIC", "exam_toeic"),
        ExamSetting("TOEIC SW", "exam_toeicsw"),
        ExamSetting("英検", "exam_eiken"),
        ExamSetting("TOEFL iBT", "exam_toeflibt"),
        ExamSetting("IELTS", "exam_ielts")
    )

    LaunchedEffect(Unit) {
        scheduleExamCountdown(workManager)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("受験日カウントダウン設定", fontSize = 20.sp) },
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
    workManager: WorkManager
) {
    val context = LocalContext.current
    val dataStore = context.dataStore
    val scope = rememberCoroutineScope()

    var examDate by remember { mutableStateOf(0L) }
    var isEnabled by remember { mutableStateOf(false) }

    // DataStoreからデータを読み込み
    LaunchedEffect(Unit) {
        dataStore.data
            .collect { preferences ->
                examDate = preferences[longPreferencesKey(setting.prefKey)] ?: 0L
                isEnabled = preferences[booleanPreferencesKey("${setting.prefKey}_notify")] ?: false
            }
    }

    val dateFormatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    val formattedDate = if (examDate > 0L) dateFormatter.format(Date(examDate)) else "未設定"

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
                    // DataStoreに保存
                    scope.launch(Dispatchers.IO) {
                        dataStore.edit { preferences ->
                            preferences[booleanPreferencesKey("${setting.prefKey}_notify")] = checked
                        }
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = "受験日:\n$formattedDate",
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
                            // DataStoreに保存
                            scope.launch(Dispatchers.IO) {
                                dataStore.edit { preferences ->
                                    preferences[longPreferencesKey(setting.prefKey)] = examDate
                                }
                            }
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
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                val url = examUrls[setting.name] ?: return@Button
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                    setPackage("com.android.chrome")
                }
                try {
                    context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    intent.setPackage(null)
                    context.startActivity(intent)
                }
            }) {
                Text(text = "公式HPを開く")
            }
        }
    }
}

// WorkManager の定期実行をスケジュール（1日ごとに実行）
private fun scheduleExamCountdown(workManager: WorkManager) {
//    val request = PeriodicWorkRequestBuilder<MultiExamCountdownWorker>(1, TimeUnit.DAYS)
    val request = PeriodicWorkRequestBuilder<MultiExamCountdownWorker>(1, TimeUnit.MINUTES)
//        .setInitialDelay(1, TimeUnit.DAYS)
        .setInitialDelay(1, TimeUnit.MINUTES)
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
        val dataStore = applicationContext.dataStore
        val now = Calendar.getInstance().timeInMillis
        examSettings.forEach { exam ->
            val examDate = runBlocking {
                dataStore.data
                    .first()[longPreferencesKey(exam.prefKey)] ?: 0L
            }
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
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
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

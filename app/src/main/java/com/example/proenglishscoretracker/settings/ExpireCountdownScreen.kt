package com.example.proenglishscoretracker.settings

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.navigation.NavHostController
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.proenglishscoretracker.R
import com.example.proenglishscoretracker.data.EnglishInfoDatabase
import com.example.proenglishscoretracker.data.EnglishTestInfo
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import kotlin.random.Random

@Composable
fun ExpireCountdownScreen(
    navHostController: NavHostController
) {
    val context = LocalContext.current
    val expireSoonPreferences =
        context.getSharedPreferences("expire_soon_prefs", Context.MODE_PRIVATE)
    val expiredPreferences = context.getSharedPreferences("expire_soon_prefs", Context.MODE_PRIVATE)
    val workManager = WorkManager.getInstance(context)

    // 各試験ごとの設定状態を管理
    val expireSoonEnabledToeic = remember { mutableStateOf(false) }
    val expiredEnabledToeic = remember { mutableStateOf(false) }

    val expireSoonEnabledToeicSw = remember { mutableStateOf(false) }
    val expiredEnabledToeicSw = remember { mutableStateOf(false) }

    val expireSoonEnabledToefl = remember { mutableStateOf(false) }
    val expiredEnabledToefl = remember { mutableStateOf(false) }

    val expireSoonEnabledIelts = remember { mutableStateOf(false) }
    val expiredEnabledIelts = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        scheduleExpireCountdown(workManager)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("資格有効期限通知設定", fontSize = 20.sp) },
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
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // TOEIC
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("TOEIC - 有効期限が近づいている通知設定")
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = expireSoonEnabledToeic.value,
                    onCheckedChange = { expireSoonEnabledToeic.value = it }
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("TOEIC - 有効期限が切れた通知設定")
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = expiredEnabledToeic.value,
                    onCheckedChange = { expiredEnabledToeic.value = it }
                )
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            // TOEIC SW
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("TOEIC SW - 有効期限が近づいている通知設定")
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = expireSoonEnabledToeicSw.value,
                    onCheckedChange = { expireSoonEnabledToeicSw.value = it }
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("TOEIC SW - 有効期限が切れた通知設定")
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = expiredEnabledToeicSw.value,
                    onCheckedChange = { expiredEnabledToeicSw.value = it }
                )
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            // TOEFL
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("TOEFL - 有効期限が近づいている通知設定")
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = expireSoonEnabledToefl.value,
                    onCheckedChange = { expireSoonEnabledToefl.value = it }
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("TOEFL - 有効期限が切れた通知設定")
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = expiredEnabledToefl.value,
                    onCheckedChange = { expiredEnabledToefl.value = it }
                )
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            // IELTS
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("IELTS - 有効期限が近づいている通知設定")
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = expireSoonEnabledIelts.value,
                    onCheckedChange = { expireSoonEnabledIelts.value = it }
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("IELTS - 有効期限が切れた通知設定")
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = expiredEnabledIelts.value,
                    onCheckedChange = { expiredEnabledIelts.value = it }
                )
            }
        }
    }
}

// WorkManager の定期実行をスケジュール（1日ごとに実行）
private fun scheduleExpireCountdown(workManager: WorkManager) {
    val request = PeriodicWorkRequestBuilder<ExpireNotificationWorker>(1, TimeUnit.DAYS)
        .setInitialDelay(1, TimeUnit.DAYS)
        .build()
    workManager.enqueueUniquePeriodicWork(
        "multi_expire_countdown",
        ExistingPeriodicWorkPolicy.REPLACE,
        request
    )
}

class ExpireNotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val dao = EnglishInfoDatabase.getEnglishInfoDatabase(applicationContext).englishInfoDao()
        val today = LocalDate.now()
        val notifications = mutableListOf<String>()

        // TOEIC の有効期限チェック
        val toeicList = dao.getAllToeicInfo()
        notifications.addAll(notifyExpirySoon(toeicList, "TOEIC", today))
        notifications.addAll(notifyExpired(toeicList, "TOEIC", today))

        // TOEIC SW の有効期限チェック
        val toeicSwList = dao.getAllToeicSwInfo()
        notifications.addAll(notifyExpirySoon(toeicSwList, "TOEIC SW", today))
        notifications.addAll(notifyExpired(toeicSwList, "TOEIC SW", today))

        // TOEFL の有効期限チェック
        val toeflList = dao.getAllToeflIbtInfo()
        notifications.addAll(notifyExpirySoon(toeflList, "TOEFL iBT", today))
        notifications.addAll(notifyExpired(toeflList, "TOEFL iBT", today))

        // IELTS の有効期限チェック
        val ieltsList = dao.getAllIeltsInfo()
        notifications.addAll(notifyExpirySoon(ieltsList, "IELTS", today))
        notifications.addAll(notifyExpired(ieltsList, "IELTS", today))

        // 通知を送信
        notifications.forEach { message ->
            sendNotification(applicationContext, "資格の有効期限", message)
        }

        return Result.success()
    }

    private fun notifyExpirySoon(
        testList: List<EnglishTestInfo>,
        testName: String,
        today: LocalDate
    ): List<String> {
        val notifications = mutableListOf<String>()
        for (info in testList) {
            val dateString: String? = when (info) {
                is EnglishTestInfo.TOEIC -> info.date
                is EnglishTestInfo.TOEIC_SW -> info.date
                is EnglishTestInfo.TOEFL -> info.date
                is EnglishTestInfo.IELTS -> info.date
                is EnglishTestInfo.EIKEN -> null
            }

            if (dateString != null) {
                val expiryDate = LocalDate.parse(dateString).plusYears(2)
                val remainingDays = ChronoUnit.DAYS.between(today, expiryDate)

                if (remainingDays in 1..60) {
                    notifications.add("$testName ($dateString) の有効期限が近づいています。(残り${remainingDays}日)")
                }
            }
        }
        return notifications
    }

    private fun notifyExpired(
        testList: List<EnglishTestInfo>,
        testName: String,
        today: LocalDate
    ): List<String> {
        val notifications = mutableListOf<String>()
        for (info in testList) {
            val dateString: String? = when (info) {
                is EnglishTestInfo.TOEIC -> info.date
                is EnglishTestInfo.TOEIC_SW -> info.date
                is EnglishTestInfo.TOEFL -> info.date
                is EnglishTestInfo.IELTS -> info.date
                is EnglishTestInfo.EIKEN -> null
            }

            if (dateString != null) {
                val expiryDate = LocalDate.parse(dateString).plusYears(2)
                val remainingDays = ChronoUnit.DAYS.between(today, expiryDate)

                if (remainingDays < 0) {
                    notifications.add("$testName ($dateString) の有効期限が切れました。")
                }
            }
        }
        return notifications
    }

    private fun sendNotification(context: Context, title: String, message: String) {
        val channelId = "expire_notification_channel"
        val notificationManager = context.getSystemService(NotificationManager::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "資格の有効期限",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.calendar)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(Random.nextInt(), notification)
    }
}

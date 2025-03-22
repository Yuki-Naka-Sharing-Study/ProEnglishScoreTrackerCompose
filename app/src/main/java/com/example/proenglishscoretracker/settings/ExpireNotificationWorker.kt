package com.example.proenglishscoretracker.settings

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.proenglishscoretracker.R
import com.example.proenglishscoretracker.data.EnglishInfoDatabase
import com.example.proenglishscoretracker.data.EnglishTestInfo
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.random.Random

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

    private fun notifyExpirySoon(testList: List<EnglishTestInfo>, testName: String, today: LocalDate): List<String> {
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

    private fun notifyExpired(testList: List<EnglishTestInfo>, testName: String, today: LocalDate): List<String> {
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
            val channel = NotificationChannel(channelId, "資格の有効期限", NotificationManager.IMPORTANCE_DEFAULT)
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

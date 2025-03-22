package com.example.proenglishscoretracker.settings

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
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
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.proenglishscoretracker.R
import com.example.proenglishscoretracker.data.EnglishInfoDatabase
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
    val expiredPreferences = context.getSharedPreferences("expired_prefs", Context.MODE_PRIVATE)
    val workManager = WorkManager.getInstance(context)

    // 各試験ごとの設定状態を管理
    val expireSoonEnabledToeic = remember {
        mutableStateOf(
            expireSoonPreferences.getBoolean(
                "expireSoonEnabledToeic",
                false
            )
        )
    }
    val expiredEnabledToeic =
        remember { mutableStateOf(expiredPreferences.getBoolean("expiredEnabledToeic", false)) }

    val expireSoonEnabledToeicSw = remember {
        mutableStateOf(
            expireSoonPreferences.getBoolean(
                "expireSoonEnabledToeicSw",
                false
            )
        )
    }
    val expiredEnabledToeicSw =
        remember { mutableStateOf(expiredPreferences.getBoolean("expiredEnabledToeicSw", false)) }

    val expireSoonEnabledToefl = remember {
        mutableStateOf(
            expireSoonPreferences.getBoolean(
                "expireSoonEnabledToefl",
                false
            )
        )
    }
    val expiredEnabledToefl =
        remember { mutableStateOf(expiredPreferences.getBoolean("expiredEnabledToefl", false)) }

    val expireSoonEnabledIelts = remember {
        mutableStateOf(
            expireSoonPreferences.getBoolean(
                "expireSoonEnabledIelts",
                false
            )
        )
    }
    val expiredEnabledIelts =
        remember { mutableStateOf(expiredPreferences.getBoolean("expiredEnabledIelts", false)) }

    LaunchedEffect(Unit) {
        notifyToeicExpireSoon(workManager)
        notifyToeicExpired(workManager)
        notifyToeicSwExpireSoon(workManager)
        notifyToeicSwExpired(workManager)
        notifyToeflIbtExpireSoon(workManager)
        notifyToeflIbtExpired(workManager)
        notifyIeltsExpireSoon(workManager)
        notifyIeltsExpired(workManager)
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
                    onCheckedChange = {
                        expireSoonEnabledToeic.value = it
                        expireSoonPreferences.edit().putBoolean("expireSoonEnabledToeic", it)
                            .apply()
                        notifyToeicExpireSoon(workManager)
                    }
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("TOEIC - 有効期限が切れた通知設定")
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = expiredEnabledToeic.value,
                    onCheckedChange = {
                        expiredEnabledToeic.value = it
                        expiredPreferences.edit().putBoolean("expiredEnabledToeic", it).apply()
                        notifyToeicExpired(workManager)
                    }
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
                    onCheckedChange = {
                        expireSoonEnabledToeicSw.value = it
                        expireSoonPreferences.edit().putBoolean("expireSoonEnabledToeicSw", it)
                            .apply()
                        notifyToeicSwExpireSoon(workManager)
                    }
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("TOEIC SW - 有効期限が切れた通知設定")
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = expiredEnabledToeicSw.value,
                    onCheckedChange = {
                        expiredEnabledToeicSw.value = it
                        expiredPreferences.edit().putBoolean("expiredEnabledToeicSw", it).apply()
                        notifyToeicSwExpired(workManager)
                    }
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
                    onCheckedChange = {
                        expireSoonEnabledToefl.value = it
                        expireSoonPreferences.edit().putBoolean("expireSoonEnabledToefl", it)
                            .apply()
                        notifyToeflIbtExpireSoon(workManager)
                    }
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("TOEFL - 有効期限が切れた通知設定")
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = expiredEnabledToefl.value,
                    onCheckedChange = {
                        expiredEnabledToefl.value = it
                        expiredPreferences.edit().putBoolean("expiredEnabledToefl", it).apply()
                        notifyToeflIbtExpired(workManager)
                    }
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
                    onCheckedChange = {
                        expireSoonEnabledIelts.value = it
                        expireSoonPreferences.edit().putBoolean("expireSoonEnabledIelts", it)
                            .apply()
                        notifyIeltsExpireSoon(workManager)
                    }
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("IELTS - 有効期限が切れた通知設定")
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = expiredEnabledIelts.value,
                    onCheckedChange = {
                        expiredEnabledIelts.value = it
                        expiredPreferences.edit().putBoolean("expiredEnabledIelts", it).apply()
                        notifyIeltsExpired(workManager)
                    }
                )
            }
        }
    }
}

// TOEICの有効期限通知(もうすぐ切れる)
private fun notifyToeicExpireSoon(workManager: WorkManager) {
    val request = PeriodicWorkRequestBuilder<ToeicExpireSoonWorker>(1, TimeUnit.DAYS)
        .setInitialDelay(0, TimeUnit.MINUTES)
        .build()
    workManager.enqueueUniquePeriodicWork(
        "notify_toeic_expire_soon",
        ExistingPeriodicWorkPolicy.REPLACE,
        request
    )
}

// TOEIC SWの有効期限通知(もうすぐ切れる)
private fun notifyToeicSwExpireSoon(workManager: WorkManager) {
    val request = PeriodicWorkRequestBuilder<ToeicSwExpireSoonWorker>(1, TimeUnit.DAYS)
        .setInitialDelay(0, TimeUnit.MINUTES)
        .build()
    workManager.enqueueUniquePeriodicWork(
        "notify_toeic_sw_expire_soon",
        ExistingPeriodicWorkPolicy.REPLACE,
        request
    )
}

// TOEFL iBTの有効期限通知(もうすぐ切れる)
private fun notifyToeflIbtExpireSoon(workManager: WorkManager) {
    val request = PeriodicWorkRequestBuilder<ToeflIbtExpireSoonWorker>(1, TimeUnit.DAYS)
        .setInitialDelay(0, TimeUnit.MINUTES)
        .build()
    workManager.enqueueUniquePeriodicWork(
        "notify_toefl_ibt_expire_soon",
        ExistingPeriodicWorkPolicy.REPLACE,
        request
    )
}

// IELTSの有効期限通知(もうすぐ切れる)
private fun notifyIeltsExpireSoon(workManager: WorkManager) {
    val request = PeriodicWorkRequestBuilder<IeltsExpireSoonWorker>(1, TimeUnit.DAYS)
        .setInitialDelay(0, TimeUnit.MINUTES)
        .build()
    workManager.enqueueUniquePeriodicWork(
        "notify_ielts_expire_soon",
        ExistingPeriodicWorkPolicy.REPLACE,
        request
    )
}

// TOEICの有効期限通知(有効期限切れ)
private fun notifyToeicExpired(workManager: WorkManager) {
    val request = OneTimeWorkRequestBuilder<ToeicExpiredWorker>()
        .setInitialDelay(1, TimeUnit.DAYS)
        .build()
    workManager.enqueueUniqueWork(
        "notify_toeic_expired",
        ExistingWorkPolicy.REPLACE,
        request
    )
}

// TOEIC SWの有効期限通知(有効期限切れ)
private fun notifyToeicSwExpired(workManager: WorkManager) {
    val request = OneTimeWorkRequestBuilder<ToeicSwExpiredWorker>()
        .setInitialDelay(1, TimeUnit.DAYS)
        .build()
    workManager.enqueueUniqueWork(
        "notify_toeic_sw_expired",
        ExistingWorkPolicy.REPLACE,
        request
    )
}

// TOEFL iBTの有効期限通知(有効期限切れ)
private fun notifyToeflIbtExpired(workManager: WorkManager) {
    val request = OneTimeWorkRequestBuilder<ToeflIbtExpiredWorker>()
        .setInitialDelay(1, TimeUnit.DAYS)
        .build()
    workManager.enqueueUniqueWork(
        "notify_toefl_ibt_expired",
        ExistingWorkPolicy.REPLACE,
        request
    )
}

// IELTSの有効期限通知(有効期限切れ)
private fun notifyIeltsExpired(workManager: WorkManager) {
    val request = OneTimeWorkRequestBuilder<IeltsExpiredWorker>()
        .setInitialDelay(1, TimeUnit.DAYS)
        .build()
    workManager.enqueueUniqueWork(
        "notify_ielts_expired",
        ExistingWorkPolicy.REPLACE,
        request
    )
}

// TOEICの有効期限が近づいている場合のWorker
class ToeicExpireSoonWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val dao = EnglishInfoDatabase.getEnglishInfoDatabase(applicationContext).englishInfoDao()
        val today = LocalDate.now()
        val notifications = mutableListOf<String>()

        // TOEICの有効期限チェック
        val toeicList = dao.getAllToeicInfo() // TOEICに関するデータだけ取得
        toeicList.forEach { toeic ->
            val expiryDate = LocalDate.parse(toeic.date).plusYears(2) // 2年後が有効期限
            val remainingDays = ChronoUnit.DAYS.between(today, expiryDate)

            // 残り60日以内であれば通知
            if (remainingDays in 1..60) {
                notifications.add("TOEIC の有効期限が近づいています。(残り${remainingDays}日)")
            }
        }

        // 通知を送信
        notifications.forEach { message ->
            sendNotification(applicationContext, "資格の有効期限", message)
        }
        return Result.success()
    }
    private fun sendNotification(
        context: Context,
        title: String,
        message: String
    ) {
        val channelId = "toeic_expire_soon_notification_channel"
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

class ToeicSwExpireSoonWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val dao = EnglishInfoDatabase.getEnglishInfoDatabase(applicationContext).englishInfoDao()
        val today = LocalDate.now()
        val notifications = mutableListOf<String>()
        val toeicSwList = dao.getAllToeicSwInfo()

        toeicSwList.forEach { toeicSw ->
            val expiryDate = LocalDate.parse(toeicSw.date).plusYears(2)
            val remainingDays = ChronoUnit.DAYS.between(today, expiryDate)

            if (remainingDays in 1..60) {
                notifications.add("TOEIC SW の有効期限が近づいています。(残り${remainingDays}日)")
            }
        }
        notifications.forEach { message ->
            sendNotification(applicationContext, "資格の有効期限", message)
        }
        return Result.success()
    }
    private fun sendNotification(
        context: Context,
        title: String,
        message: String
    ) {
        val channelId = "toeic_sw_expire_soon_notification_channel"
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

class ToeflIbtExpireSoonWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val dao = EnglishInfoDatabase.getEnglishInfoDatabase(applicationContext).englishInfoDao()
        val today = LocalDate.now()
        val notifications = mutableListOf<String>()
        val toeflIbtList = dao.getAllToeflIbtInfo()

        toeflIbtList.forEach { toefl ->
            val expiryDate = LocalDate.parse(toefl.date).plusYears(2)
            val remainingDays = ChronoUnit.DAYS.between(today, expiryDate)

            if (remainingDays in 1..60) {
                notifications.add("TOEFL iBT の有効期限が近づいています。(残り${remainingDays}日)")
            }
        }
        notifications.forEach { message ->
            sendNotification(applicationContext, "資格の有効期限", message)
        }
        return Result.success()
    }
    private fun sendNotification(
        context: Context,
        title: String,
        message: String
    ) {
        val channelId = "toefl_ibt_expire_soon_notification_channel"
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

class IeltsExpireSoonWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val dao = EnglishInfoDatabase.getEnglishInfoDatabase(applicationContext).englishInfoDao()
        val today = LocalDate.now()
        val notifications = mutableListOf<String>()
        val ieltsList = dao.getAllIeltsInfo()

        ieltsList.forEach { ielts ->
            val expiryDate = LocalDate.parse(ielts.date).plusYears(2)
            val remainingDays = ChronoUnit.DAYS.between(today, expiryDate)

            if (remainingDays in 1..60) {
                notifications.add("IELTS の有効期限が近づいています。(残り${remainingDays}日)")
            }
        }
        notifications.forEach { message ->
            sendNotification(applicationContext, "資格の有効期限", message)
        }

        return Result.success()
    }
    private fun sendNotification(
        context: Context,
        title: String,
        message: String
    ) {
        val channelId = "ielts_expire_soon_notification_channel"
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

class ToeicExpiredWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val dao = EnglishInfoDatabase.getEnglishInfoDatabase(applicationContext).englishInfoDao()
        val today = LocalDate.now()
        val notifications = mutableListOf<String>()
        val toeicList = dao.getAllToeicInfo()

        toeicList.forEach { toeic ->
            val expiryDate = LocalDate.parse(toeic.date).plusYears(2)
            if (ChronoUnit.DAYS.between(today, expiryDate) < 0) {
                notifications.add("TOEIC の有効期限が切れました。")
            }
        }
        fun sendNotification(
            context: Context,
            title: String,
            message: String
        ) {
            val channelId = "toeic_expired_notification_channel"
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
        notifications.forEach { message ->
            sendNotification(applicationContext, "資格の有効期限", message)
        }
        return Result.success()
    }
}

class ToeicSwExpiredWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val dao = EnglishInfoDatabase.getEnglishInfoDatabase(applicationContext).englishInfoDao()
        val today = LocalDate.now()
        val notifications = mutableListOf<String>()
        val toeicSwList = dao.getAllToeicSwInfo()
        toeicSwList.forEach { toeicSw ->
            val expiryDate = LocalDate.parse(toeicSw.date).plusYears(2)
            if (ChronoUnit.DAYS.between(today, expiryDate) < 0) {
                notifications.add("TOEIC SW の有効期限が切れました。")
            }
        }
        fun sendNotification(
            context: Context,
            title: String,
            message: String
        ) {
            val channelId = "toeic_sw_expired_notification_channel"
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
        notifications.forEach { message ->
            sendNotification(applicationContext, "資格の有効期限", message)
        }
        return Result.success()
    }
}

class ToeflIbtExpiredWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val dao = EnglishInfoDatabase.getEnglishInfoDatabase(applicationContext).englishInfoDao()
        val today = LocalDate.now()
        val notifications = mutableListOf<String>()
        val toeflList = dao.getAllToeflIbtInfo()
        toeflList.forEach { toefl ->
            val expiryDate = LocalDate.parse(toefl.date).plusYears(2)
            if (ChronoUnit.DAYS.between(today, expiryDate) < 0) {
                notifications.add("TOEFL iBT の有効期限が切れました。")
            }
        }
        fun sendNotification(
            context: Context,
            title: String,
            message: String
        ) {
            val channelId = "toefl_ibt_expired_notification_channel"
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
        notifications.forEach { message ->
            sendNotification(applicationContext, "資格の有効期限", message)
        }
        return Result.success()
    }
}

class IeltsExpiredWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val dao = EnglishInfoDatabase.getEnglishInfoDatabase(applicationContext).englishInfoDao()
        val today = LocalDate.now()
        val notifications = mutableListOf<String>()
        val ieltsList = dao.getAllIeltsInfo()
        ieltsList.forEach { ielts ->
            val expiryDate = LocalDate.parse(ielts.date).plusYears(2)
            if (ChronoUnit.DAYS.between(today, expiryDate) < 0) {
                notifications.add("IELTS の有効期限が切れました。")
            }
        }
        fun sendNotification(
            context: Context,
            title: String,
            message: String
        ) {
            val channelId = "ielts_expired_notification_channel"
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
        notifications.forEach { message ->
            sendNotification(applicationContext, "資格の有効期限", message)
        }
        return Result.success()
    }
}

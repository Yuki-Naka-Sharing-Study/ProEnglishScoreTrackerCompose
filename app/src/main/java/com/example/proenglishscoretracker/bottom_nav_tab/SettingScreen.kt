package com.example.proenglishscoretracker.bottom_nav_tab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 画面タイトル
        Text(
            text = "設定",
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 設定項目リスト（例として他の項目も含む）
        val items: List<Pair<String, () -> Unit>> = listOf(
            // 受験日カウントダウン設定 → examCountdown 画面へ遷移
            "このアプリについて" to {},
            "お問い合わせ" to {
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:support@example.com")
                    putExtra(Intent.EXTRA_SUBJECT, "お問い合わせ")
                }
                context.startActivity(emailIntent)
            },
            "レビューする" to {
                val reviewIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.example.app"))
                context.startActivity(reviewIntent)
            },
            "このアプリを共有する" to {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "このアプリをチェックしてみて！\nhttps://play.google.com/store/apps/details?id=com.example.app"
                    )
                }
                context.startActivity(Intent.createChooser(shareIntent, "アプリを共有"))
            },
            "バージョン 1.0.0" to {},
            "受験日カウントダウンを設定" to { navController.navigate("examCountdown") },
            "登録推奨YouTuber" to { navController.navigate("youtuberScreen") },
        )

        items.forEach { (title, action) ->
            SettingsItem(title = title, onClick = action)
        }
    }
}

@Composable
fun SettingsItem(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            modifier = Modifier.padding(16.dp)
        )
    }
}


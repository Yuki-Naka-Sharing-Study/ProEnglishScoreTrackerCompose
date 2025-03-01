package com.example.proenglishscoretracker.settings

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.proenglishscoretracker.R

class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val webView = findViewById<WebView>(R.id.web_view)
        val url = intent.getStringExtra("URL") // IntentからURLを受け取る

        webView.webViewClient = WebViewClient() // リンククリック時にWebView内で遷移させる
        webView.loadUrl(url ?: "https://www.google.com") // URLが空ならGoogleを表示

        // JavaScriptを有効にする場合
        webView.settings.javaScriptEnabled = true
    }
}


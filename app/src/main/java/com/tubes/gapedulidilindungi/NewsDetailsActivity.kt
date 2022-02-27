package com.tubes.gapedulidilindungi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_news_details.*

class NewsDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_details)
        val url = intent.getStringExtra("news_url")
//        supportActionBar!!.title = url
        if (url != null) {
            initiateWebView(url)
        }
    }

    private fun initiateWebView(url: String = "https://google.com/") {
        webViewNewsDetails__article.webViewClient = WebViewClient()
        webViewNewsDetails__article.apply {
            loadUrl(url)
            settings.javaScriptEnabled = true
//            settings.safeBrowsingEnabled = true
        }
    }

    override fun onBackPressed() {
        if (webViewNewsDetails__article.canGoBack()) webViewNewsDetails__article.goBack() else super.onBackPressed()
    }
}
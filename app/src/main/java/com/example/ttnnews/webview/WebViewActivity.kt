package com.example.ttnnews.webview

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.ttnnews.constants.Constant
import com.example.ttnnews.R


class WebViewActivity : AppCompatActivity() {
    private lateinit var webview: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        webview = findViewById(R.id.web_view)
        webview.webViewClient = MyWebViewClient()

        if (intent.hasExtra(Constant.URL)) {
            if (intent.getStringExtra(Constant.URL)!!.isNotBlank()) {
                webview.loadUrl(intent.getStringExtra(Constant.URL)!!)
            }
        }
    }
    class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
    }
}
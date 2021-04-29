package com.example.ttnnews.webview

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.ttnnews.R


class WebViewActivity : AppCompatActivity() {
    var webview: WebView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        webview = findViewById(R.id.web_view)
        webview!!.setWebViewClient(MyWebViewClient())

        if(intent.hasExtra("url")){

            if(intent.getStringExtra("url")!!.isNotBlank()){
                webview!!.loadUrl(intent.getStringExtra("url")!!);

            }
        }


    }

    class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
    }}
package com.example.ttnnews.webview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.ttnnews.R
import com.example.ttnnews.constants.Constant

class WebViewFrag : Fragment() {
    private lateinit var webview: WebView

    private var param1: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(Constant.URL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_web_view, container, false)
        webview = view.findViewById(R.id.web_view)
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            WebViewFrag().apply {
                arguments = Bundle().apply {
                    putString(Constant.URL, param1)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webview.webViewClient = MyWebViewClient()
        webview.loadUrl(arguments!!.getString(Constant.URL)!!)
    }

    class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
    }
}
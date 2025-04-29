package com.example.pgdummyapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebView.setWebContentsDebuggingEnabled
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.net.URISyntaxException
import android.webkit.JavascriptInterface
import com.example.pgdummyapp.DebugLog.TAG

class WebViewActivity : AppCompatActivity() {

    companion object {
        private const val ISP_PACKAGE = "kvp.jjy.MispAndroid320"
        private const val ISP_MARKET_URL = "http://mobile.vpay.co.kr/jsp/MISP/andown.jsp"
        private const val KB_PACKAGE = "com.kbcard.kbkookmincard"
        private const val KB_MARKET_URL = "market://details?id=com.kbcard.kbkookmincard"
    }

    private lateinit var webView: WebView
    private var url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        setupUI()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackButtonPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupUI() {
        intent.getStringExtra("url")?.let {
            url = it
        }

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        webView = findViewById(R.id.web_view)
        webView.addJavascriptInterface(bwcBridge(), "isdlpshown")

        // Cookie 사용 설정
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        val cm = CookieManager.getInstance()
        cm.setAcceptCookie(true)
        cm.setAcceptThirdPartyCookies(webView, true)

        setWebContentsDebuggingEnabled(true)
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.domStorageEnabled = true // HTML5 local storage
        webView.settings.databaseEnabled = true // database storage API
        webView.settings.userAgentString = webView.settings.userAgentString + " isdlpshown"

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let {
                    if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("javascript")) {
                        try {
                            val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                            startActivity(intent)
                        } catch (anfe: ActivityNotFoundException) {
                            handleCardAppIntent(url)
                        } catch (e: URISyntaxException) {
                            Log.e(TAG, "URI Syntax Exception: $url", e)
                        }
                        return true
                    } else {
                        view?.loadUrl(url)
                        return false
                    }
                } ?: run {
                    Log.e(TAG, "url is null")
                }
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                webView.clearCache(false)
            }

            override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                Toast.makeText(applicationContext, "error code: $errorCode, description: $description \nfailingUrl: $failingUrl", Toast.LENGTH_LONG)
                    .show()
                Log.e(TAG, "Webview :: onReceivedError: Code $errorCode, description : $description, failingUrl : $failingUrl")
            }
        }

        // Chrome inspect
        if (0 != applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE) {
            setWebContentsDebuggingEnabled(true)
        }

        webView.loadUrl(url)
    }

    private fun handleCardAppIntent(url: String) {
        when {
            url.startsWith("ispmobile://") -> {
                if (!isAppInstalled(ISP_PACKAGE)) {
                    openMarket(ISP_MARKET_URL)
                }
            }
            url.contains("kb-acp") -> {
                if (!isAppInstalled(KB_PACKAGE)) {
                    openMarket(KB_MARKET_URL)
                }
            }
            else -> {
                val packageNm = getPackageNameFromIntent(url)
                if (packageNm != null && !isAppInstalled(packageNm)) {
                    openMarket("market://search?q=$packageNm")
                }
            }
        }
    }

    private fun isAppInstalled(packageName: String): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun openMarket(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun getPackageNameFromIntent(url: String): String? {
        return try {
            val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
            intent.`package`
        } catch (e: URISyntaxException) {
            null
        }
    }

    // Toolbar의 이전 버튼 처리
    private fun onBackButtonPressed() {
        onBackPressed()
    }

    var hd: Handler = Handler()
    var isDlp = "NO"

    //web 스크립트 연동
    private inner class bwcBridge {
        @JavascriptInterface
        fun setMessage(value: String) {
            hd.post(Runnable { isDlp = value })
        }
    }

    var clickCheck = false
    override fun onBackPressed() {
        if (isDlp == "YES") {
            webView.loadUrl("javascript:bwcAppClose();")
        } else {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                if (!clickCheck) {
                    clickCheck = true
                    Toast.makeText(this, "종료안내.", Toast.LENGTH_SHORT).show()
                    val hd = Handler()
                    hd.postDelayed({ clickCheck = false }, 1000)
                } else {
                    super.onBackPressed()
                }
            }
        }
    }
}
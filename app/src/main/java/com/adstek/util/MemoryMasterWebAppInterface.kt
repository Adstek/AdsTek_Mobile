package com.adstek.util

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast


class MemoryMasterWebAppInterface(private val sharedPref: SharedPref,private val context: Context, private val webView: WebView) {

    @JavascriptInterface
    fun onPlayButtonClicked() {
        Handler(Looper.getMainLooper()).post {
            // Handle play button click
            Log.d("WebApp", "Play button clicked")
            sharedPref.setPref("IS_AUTO_SWIPE_ENABLED", false)

        }
    }

    @JavascriptInterface
    fun onGameReady() {
        Handler(Looper.getMainLooper()).post {
            // Game is initialized and ready
            Log.d("WebApp", "Game ready")
        }
    }

    @JavascriptInterface
    fun onCountdownStarted(count: Int) {
        Handler(Looper.getMainLooper()).post {
            Log.d("WebApp", "Countdown started: $count")
        }
    }

    @JavascriptInterface
    fun onCountdownUpdate(count: Int) {
        Handler(Looper.getMainLooper()).post {
            Log.d("WebApp", "Countdown update: $count")
        }
    }

    @JavascriptInterface
    fun onCountdownComplete() {
        Handler(Looper.getMainLooper()).post {
            webView.loadUrl("file:///android_asset/memorycard/index.html")
            Log.d("WebApp", "Countdown complete")
        }
    }

    @JavascriptInterface
    fun onIntroHidden() {
        Handler(Looper.getMainLooper()).post {
            Log.d("WebApp", "Intro animation hidden")
        }
    }

    @JavascriptInterface
    fun onGameStatus(statusJson: String) {
        Handler(Looper.getMainLooper()).post {
            Log.d("WebApp", "Game status: $statusJson")
        }
    }

    @JavascriptInterface
    fun onError(error: String) {
        Handler(Looper.getMainLooper()).post {
            Log.e("WebApp", "Error: $error")
            Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
        }
    }
}
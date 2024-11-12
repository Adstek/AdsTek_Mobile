package com.adstek.util

import android.util.Log
import android.webkit.JavascriptInterface
import org.json.JSONObject
import javax.inject.Inject

class WebAppInterface(
    private val authToken: String
    ) {
    companion object {
        private const val TAG = "WebAppInterface"
    }

    @JavascriptInterface
    fun getAuthToken(): String = authToken

    @JavascriptInterface
    fun getHeaders(): String {
        return try {
            val headers = JSONObject().apply {
                put("Authorization", "JWT $authToken")
                put("Content-Type", "application/json")
                put("Accept", "application/json")
            }
            Log.d(TAG, "Headers prepared with JWT: $headers")
            headers.toString()
        } catch (e: Exception) {
            Log.e(TAG, "Error preparing headers with JWT", e)
            "{}"
        }
    }

    @JavascriptInterface
    fun log(message: String, level: String = "DEBUG") {
        when (level.uppercase()) {
            "ERROR" -> Log.e(TAG, message)
            "WARN" -> Log.w(TAG, message)
            "INFO" -> Log.i(TAG, message)
            "DEBUG" -> Log.d(TAG, message)
            "VERBOSE" -> Log.v(TAG, message)
            else -> Log.d(TAG, message)
        }
    }
}
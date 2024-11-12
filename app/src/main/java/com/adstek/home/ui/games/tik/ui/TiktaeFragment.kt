package com.adstek.home.ui.games.tik.ui

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.adstek.BuildConfig
import com.adstek.R
import com.adstek.databinding.FragmentTiktaeBinding
import com.adstek.util.Constants
import com.adstek.util.SharedPref
import com.adstek.util.WebAppInterface
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class TiktaeFragment : Fragment() {
    private lateinit var binding: FragmentTiktaeBinding
    private val TAG = "TicTacToe"


    @Inject
    lateinit var sharedPref: SharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTiktaeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            setupWebView()
        }
    }

    private fun setupWebView() {
        val authToken = sharedPref.getPref(Constants.KEY_ACCESS_TOKEN, null)
        binding.webview.apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                allowFileAccess = true
                allowContentAccess = true
                allowFileAccessFromFileURLs = true
                allowUniversalAccessFromFileURLs = true
                databaseEnabled = true
                useWideViewPort = true
                loadWithOverviewMode = true
                // Disable zoom
                builtInZoomControls = false
                displayZoomControls = false
                // Set cache mode
                cacheMode = WebSettings.LOAD_DEFAULT
                // Enable mixed content
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
            WebView.setWebContentsDebuggingEnabled(true)

            // Add JavaScript interface
            addJavascriptInterface(WebAppInterface(authToken), "Android")

            // Set WebViewClient

            // Add console message logging
            webChromeClient = object : WebChromeClient() {
                override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                    consoleMessage?.apply {
                        val messageLevel = when (messageLevel()) {
                            ConsoleMessage.MessageLevel.DEBUG -> "DEBUG"
                            ConsoleMessage.MessageLevel.ERROR -> "ERROR"
                            ConsoleMessage.MessageLevel.LOG -> "LOG"
                            ConsoleMessage.MessageLevel.TIP -> "TIP"
                            ConsoleMessage.MessageLevel.WARNING -> "WARNING"
                            else -> "INFO"
                        }

                        Log.d("TiktaeFragment", "Console $messageLevel: (${lineNumber()} : ${sourceId()})")
                    }
                    return true
                }
            }

            webViewClient = object : WebViewClient() {

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    Log.d(TAG, "📱 Page loading started: $url")

                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)
                }
            }

            // Enable remote debugging if in debug build
            if (BuildConfig.DEBUG) {
                WebView.setWebContentsDebuggingEnabled(true)
            }
            // Load the HTML file
            loadUrl("file:///android_asset/tictactoe/index.html")
        }
    }

}

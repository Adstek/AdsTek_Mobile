package com.adstek.home

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.STATE_BUFFERING
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.common.Player.STATE_READY
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.adstek.BuildConfig
import com.adstek.BuildConfig.GRADLE_BASE_URL
import com.adstek.databinding.ActivityAdsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.IOException


@AndroidEntryPoint
class AdsActivity : AppCompatActivity() {
    private var mediaUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"
    private lateinit var binding: ActivityAdsBinding
    private val handler = Handler(Looper.getMainLooper())
    private var totalDuration: Long = 0



    private lateinit var player: ExoPlayer
    private val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the URL from the Intent extras

        val videoLink = intent.getStringExtra("VIDEO_LINK")
        videoLink?.let { link ->
            val fullUrl = GRADLE_BASE_URL + link.substring(1)
            val file = File(getExternalFilesDir(null), "video.mp4")

            downloadVideo(fullUrl, file) { success ->
                if (success) {
                    // Initialize player with the downloaded file
                    lifecycleScope.launch {
                        initPlayer(file.absolutePath)
                    }
                    Log.e("AdsActivity", "Download Video download video")

                } else {
                    Log.e("AdsActivity", "Failed to download video Failed")
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish() // Finish the activity
            }
        })
//        startUpdatingPlayTime()
    }





    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    @androidx.annotation.OptIn(UnstableApi::class)
    private fun initPlayer(link: String) {
        player = ExoPlayer.Builder(this)
            .build()
            .apply {
                val source = if (link.contains("m3u8")) getHlsMediaSource(link) else getProgressiveMediaSource(link)
                setMediaSource(source)
                prepare()
                addListener(playerListener)
            }
        binding.playerView.player = player
    }

    @androidx.annotation.OptIn(UnstableApi::class)
    private fun getHlsMediaSource(link: String): MediaSource {
        return HlsMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(link))
    }

    @androidx.annotation.OptIn(UnstableApi::class)
    private fun getProgressiveMediaSource(link: String): MediaSource {
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.parse(link)))
    }

    private fun releasePlayer() {
        player.apply {
            playWhenReady = false
            stop()
            release()
        }
    }



    private fun play() {
        player.playWhenReady = true
    }

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                STATE_BUFFERING -> {
                    // Handle buffering state if needed
                }
                STATE_READY -> {
                    totalDuration = player.duration

                    if (!player.isPlaying) {
                        play()
                    }
                }
                STATE_ENDED -> {
                    releasePlayer()
                    finishActivityWithResult() // Finish the activity when video ends
                }
            }
        }
    }

    private fun finishActivityWithResult() {
        finish()
    }

    private fun startUpdatingPlayTime() {
        handler.post(object : Runnable {
            override fun run() {
                val currentPosition = player.currentPosition
                val remainingTime = totalDuration - currentPosition
                val formattedTime = formatTime(remainingTime)
                binding.tvTimer.text = formattedTime
                handler.postDelayed(this, 1000)
            }
        })
    }

    private fun formatTime(timeInMillis: Long): String {
        val totalSeconds = timeInMillis / 1000
        return totalSeconds.toString()
    }

    private fun downloadVideo(url: String, file: File, callback: (Boolean) -> Unit) {
        val request = Request.Builder().url(url).build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Log the error with a detailed message
                Log.e("DownloadVideo", "Failed to download video from $url", e)
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    // Log the HTTP error
                    Log.e("DownloadVideo", "HTTP error ${response.code} while downloading video from $url")
                    callback(false)
                    return
                }

                response.body?.byteStream()?.use { inputStream ->
                    file.outputStream().use { outputStream ->
                        try {
                            inputStream.copyTo(outputStream)
                            callback(true)
                        } catch (e: IOException) {
                            // Log error if file writing fails
                            Log.e("DownloadVideo", "Failed to write video file", e)
                            callback(false)
                        }
                    }
                } ?: run {
                    // Log an error if response body is null
                    Log.e("DownloadVideo", "Response body is null while downloading video from $url")
                    callback(false)
                }
            }
        })
    }
}

package com.adstek.home

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Player.STATE_BUFFERING
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.common.Player.STATE_READY
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.adstek.BuildConfig.GRADLE_BASE_URL
import com.adstek.databinding.ActivityAdsBinding
import com.adstek.util.downloadVideo
import com.adstek.util.getFileName
import com.adstek.util.getVideoAdFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


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
        // full screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController?.let { controller ->
                controller.hide(WindowInsets.Type.systemBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }

        binding = ActivityAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the URL from the Intent extras

        val videoLink = intent.getStringExtra("VIDEO_LINK")
        videoLink?.let { link ->
            val file = getVideoAdFile(context = this, fileName = getFileName(link))

            if (!file.exists()) {
                val fullUrl = GRADLE_BASE_URL + link.substring(1)
                downloadVideo(this, fullUrl) { success ->
                    if (success) {
                        lifecycleScope.launch {
                            initPlayer(file.absolutePath)
                        }
                    } else {
                        finish()
                    }
                }
                return
            }

            lifecycleScope.launch {
                initPlayer(file.absolutePath)
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    @androidx.annotation.OptIn(UnstableApi::class)
    private fun initPlayer(link: String) {
        val formattedLink = if (link.startsWith("/")) "file://$link" else link
        player = ExoPlayer.Builder(this)
            .build()
            .apply {
                val source = if (formattedLink.contains("file://")) {
                    getLocalFileMediaSource(formattedLink)
                } else if (formattedLink.contains("m3u8")) {
                    getHlsMediaSource(formattedLink)
                } else {
                    getProgressiveMediaSource(formattedLink)
                }
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
            .createMediaSource(MediaItem.fromUri(link))
    }

    @androidx.annotation.OptIn(UnstableApi::class)
    private fun getLocalFileMediaSource(link: String): MediaSource {
        val fileUri = Uri.parse(link)
        val dataSourceFactory = DefaultDataSource.Factory(this)
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(fileUri))
    }

    private fun releasePlayer() {
        if (::player.isInitialized) {
            player.apply {
                playWhenReady = false
                stop()
                release()
            }
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
                        startUpdatingPlayTime()
                    }
                }
                STATE_ENDED -> {
                    releasePlayer()
                    finishActivityWithResult() // Finish the activity when video ends
                }
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            error.printStackTrace()
            finish()
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
}

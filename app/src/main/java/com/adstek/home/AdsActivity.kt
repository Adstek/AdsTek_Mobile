package com.adstek.home

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
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
import com.adstek.databinding.ActivityAdsBinding
import com.adstek.home.ui.games.trivia.QuestionsAndAnswersFragment
import dagger.hilt.android.AndroidEntryPoint

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
        val link = mediaUrl

        initPlayer(link)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish() // Finish the activity
            }
        })
        startUpdatingPlayTime()
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

}

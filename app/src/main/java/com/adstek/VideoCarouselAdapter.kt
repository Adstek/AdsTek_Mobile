package com.adstek

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.STATE_BUFFERING
import androidx.media3.common.Player.STATE_ENDED
import androidx.media3.common.Player.STATE_READY
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.LoadControl
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.upstream.DefaultLoadErrorHandlingPolicy
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.adstek.databinding.ItemVideoCarouselBinding
import com.adstek.util.CacheManager
import timber.log.Timber
import java.io.File


@UnstableApi
class VideoCarouselAdapter(
    private val context: Context,
    private val onVideoClick: ((video: VideoModel) -> Unit)
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var currentlyPlayingHolder: VideoViewHolder? = null
    private val preloadingPlayers = mutableMapOf<Int, ExoPlayer>()
    private var isAutoPlayEnabled = true

    private val httpDataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
        .setConnectTimeoutMs(DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS)
        .setReadTimeoutMs(DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS)
        .setAllowCrossProtocolRedirects(true)

    private val fileDataSourceFactory = DefaultDataSourceFactory(context)

    private val cacheDataSourceFactory: CacheDataSource.Factory by lazy {
        CacheDataSource.Factory()
            .setCache(CacheManager.getInstance(context))
            .setUpstreamDataSourceFactory(httpDataSourceFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }

    private val diffCallback = object : DiffUtil.ItemCallback<VideoModel>() {
        override fun areItemsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    private fun buildOptimizedLoadControl(): LoadControl {
        return DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                2_000,
                5_000,
                1_000,
                1_000
            )
            .setPrioritizeTimeOverSizeThresholds(true)
            .setBackBuffer(
                1_000,
                true
            )
            .build()
    }

    private fun createOptimizedExoPlayer(videoUrl: String): ExoPlayer {
        return ExoPlayer.Builder(context)
            .setLoadControl(buildOptimizedLoadControl())
            .setRenderersFactory(
                DefaultRenderersFactory(context)
                    .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER)
            )
            .build()
            .apply {
                val source = createAppropriateMediaSource(videoUrl)
                setMediaSource(source)
                playWhenReady = false
                prepare()
            }
    }

    private fun createAppropriateMediaSource(videoUrl: String): MediaSource {
        return when {
            videoUrl.startsWith("raw://") -> {
                val rawId = context.resources.getIdentifier(
                    videoUrl.substringAfter("raw://"),
                    "raw",
                    context.packageName
                )
                Timber.d("Using raw resource source for: $videoUrl")
                ProgressiveMediaSource.Factory(fileDataSourceFactory)
                    .setLoadErrorHandlingPolicy(DefaultLoadErrorHandlingPolicy(4))
                    .createMediaSource(MediaItem.fromUri(RawResourceDataSource.buildRawResourceUri(rawId)))
            }
            videoUrl.startsWith("asset://") -> {
                Timber.d("Using asset source for: $videoUrl")
                ProgressiveMediaSource.Factory(fileDataSourceFactory)
                    .setLoadErrorHandlingPolicy(DefaultLoadErrorHandlingPolicy(4))
                    .createMediaSource(MediaItem.fromUri(videoUrl))
            }
            isLocalFile(videoUrl) && File(Uri.parse(videoUrl).path!!).exists() -> {
                Timber.d("Using local file source for: $videoUrl")
                ProgressiveMediaSource.Factory(fileDataSourceFactory)
                    .setLoadErrorHandlingPolicy(DefaultLoadErrorHandlingPolicy(4))
                    .createMediaSource(MediaItem.fromUri(Uri.parse(videoUrl)))
            }
            videoUrl.contains("m3u8") -> {
                Timber.d("Using HLS source for: $videoUrl")
                HlsMediaSource.Factory(cacheDataSourceFactory)
                    .setLoadErrorHandlingPolicy(DefaultLoadErrorHandlingPolicy(4))
                    .createMediaSource(MediaItem.fromUri(videoUrl))
            }
            else -> {
                Timber.d("Using network source for: $videoUrl")
                ProgressiveMediaSource.Factory(cacheDataSourceFactory)
                    .setLoadErrorHandlingPolicy(DefaultLoadErrorHandlingPolicy(4))
                    .createMediaSource(MediaItem.fromUri(Uri.parse(videoUrl)))
            }
        }
    }

    private fun isLocalFile(url: String): Boolean {
        return url.startsWith("file://") ||
                url.startsWith("/") ||
                File(url).exists() ||
                url.startsWith(context.filesDir.absolutePath) ||
                url.startsWith(context.getExternalFilesDir(null)?.absolutePath ?: "")
    }

    private fun preloadNextVideos(currentPosition: Int) {
        val totalVideos = differ.currentList.size
        if (totalVideos <= 1 || currentPosition >= totalVideos - 1) {
            return
        }

        val remainingVideos = totalVideos - (currentPosition + 1)
        val videosToPreload = minOf(2, remainingVideos)

        for (i in 1..videosToPreload) {
            val nextPosition = currentPosition + i
            if (!preloadingPlayers.containsKey(nextPosition)) {
                val nextVideo = differ.currentList[nextPosition]
                preloadingPlayers[nextPosition] = createOptimizedExoPlayer(nextVideo.videoUrl)
            }
        }
    }

    inner class VideoViewHolder(
        private val binding: ItemVideoCarouselBinding,
        private val onVideoClick: ((video: VideoModel) -> Unit)
    ) : RecyclerView.ViewHolder(binding.root) {

        private var player: ExoPlayer? = null

        private val playerListener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    STATE_BUFFERING -> {
                        Timber.tag("PlayerState").d("Buffering...")
                    }
                    STATE_READY -> {
                        Timber.tag("PlayerState").d("Ready - Starting playback")
                        if (isAutoPlayEnabled) {
                            player?.playWhenReady = true
                        }
                    }
                    STATE_ENDED -> {
                        Timber.tag("PlayerState").d("Playback ended")
                        handleVideoCompletion()
                    }
                }
            }

            override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                val videoUrl = differ.currentList[bindingAdapterPosition].videoUrl
                Log.e("PlayerError", "Error playing: $videoUrl")
                Log.e("PlayerError", "Error Code: ${error.errorCode}")
                Log.e("PlayerError", "Error Message: ${error.message}")
                Log.e("PlayerError", "Cause: ${error.cause}")
                Log.e("PlayerError", "Stack Trace: ${error.stackTraceToString()}")
                player?.prepare()
            }
        }

        private fun handleVideoCompletion() {
            val currentPosition = bindingAdapterPosition
            val nextPosition = currentPosition + 1
            val totalVideos = differ.currentList.size

            releasePlayer()

            if (nextPosition < totalVideos) {
                moveToPosition(nextPosition)
            } else {
                // Restart from beginning
                moveToPosition(0)
            }
        }

        private fun moveToPosition(position: Int) {
            val recyclerView = binding.root.parent as? RecyclerView
            recyclerView?.post {
                recyclerView.scrollToPosition(position)
                notifyItemChanged(position)
            }
        }

        fun bind(video: VideoModel) = with(binding) {
            Timber.tag("PlayerState").d("Binding view holder for ${video.videoUrl}")

            playerView.useController = false

            val preloadedPlayer = preloadingPlayers[bindingAdapterPosition]
            if (preloadedPlayer != null) {
                player = preloadedPlayer
                binding.playerView.player = player
                player?.addListener(playerListener)
                if (isAutoPlayEnabled) {
                    player?.playWhenReady = true
                }
                preloadingPlayers.remove(bindingAdapterPosition)
                Timber.d("Using preloaded player for ${video.videoUrl}")
            } else {
                Timber.d("Creating new player for ${video.videoUrl}")
                player = createOptimizedExoPlayer(video.videoUrl).also {
                    binding.playerView.player = it
                    it.addListener(playerListener)
                    if (isAutoPlayEnabled) {
                        it.playWhenReady = true
                    }
                }
            }

            preloadNextVideos(bindingAdapterPosition)

            root.setOnClickListener {
                onVideoClick.invoke(video)
            }
        }

        fun releasePlayer() {
            player?.removeListener(playerListener)
            player?.release()
            player = null
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VideoViewHolder(
            ItemVideoCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onVideoClick
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is VideoViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(list: List<VideoModel>) {
        differ.submitList(list)
    }

    fun releaseAllPlayers() {
        currentlyPlayingHolder?.releasePlayer()
        currentlyPlayingHolder = null
        releasePreloadPlayers()
    }

    private fun releasePreloadPlayers() {
        preloadingPlayers.values.forEach { player ->
            player.release()
        }
        preloadingPlayers.clear()
    }

    fun releaseCache() {
        CacheManager.release()
    }

    fun setAutoPlay(enabled: Boolean) {
        isAutoPlayEnabled = enabled
    }
}
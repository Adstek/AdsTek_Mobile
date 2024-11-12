package com.adstek.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adstek.VideoCarouselAdapter
import com.adstek.VideoModel
import com.adstek.databinding.FragmentVideoLoaderSwiperBinding
import com.adstek.extensions.navigateTo
import com.adstek.extensions.toast
import com.adstek.util.VideoTransitionAnimator
import dagger.hilt.android.AndroidEntryPoint


@UnstableApi
@AndroidEntryPoint
class VideoLoaderSwiperFragment : Fragment() {
    private lateinit var binding: FragmentVideoLoaderSwiperBinding
    private lateinit var videoAdapter: VideoCarouselAdapter
    private val transitionAnimator = VideoTransitionAnimator()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoLoaderSwiperBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadVideos()
    }

    private fun setupRecyclerView() {
        videoAdapter = VideoCarouselAdapter(
            context = requireContext(),
            onVideoClick = { video ->
                toast(video.title)
                when(video.id){
                    "1" -> {
                        navigateTo(VideoLoaderSwiperFragmentDirections.actionVideoLoaderSwiperFragmentToMemoryCardFragment())
                    }
                }
                // Handle video click if needed
            }
        )

        binding.rvList.apply {
            adapter = videoAdapter
            layoutManager = object : LinearLayoutManager(context, VERTICAL, false) {
                override fun canScrollHorizontally() = false
                override fun canScrollVertically() = false
            }
            itemAnimator = null
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            // Disable user scrolling but allow clicks
            addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    return false
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                    toast("${e.action}")
                }

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            })
        }
    }

    private fun loadVideos() {
        val videos = listOf(
            VideoModel(
                id = "3",
                videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
                title = "Second Video",
                thumbnail = "https://example.com/thumb1.jpg",
                duration = 120000
            ),

            VideoModel(
                id = "1",
                videoUrl = "raw://memory_card_into",
                title = "Memory Card Game",
                thumbnail = "https://example.com/thumb1.jpg",
                duration = 120000
            ),
            VideoModel(
                id = "2",
                videoUrl = "https://quickframe.com/wp-content/uploads/2022/09/Aveeno_eCom-Example_Max-Glow-SerumPrimer.mp4",
                title = "Second Video",
                thumbnail = "https://example.com/thumb1.jpg",
                duration = 120000
            ) ,
        )
        videoAdapter.submitList(videos)
    }

    override fun onPause() {
        super.onPause()
        videoAdapter.releaseAllPlayers()
    }

    override fun onDestroy() {
        super.onDestroy()
        videoAdapter.releaseAllPlayers()
        if (requireActivity().isFinishing) {
            videoAdapter.releaseCache()
        }
    }
}
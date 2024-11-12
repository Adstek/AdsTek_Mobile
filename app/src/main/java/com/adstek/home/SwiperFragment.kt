package com.adstek.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.adstek.R
import com.adstek.databinding.FragmentSwiperBinding
import com.adstek.home.ui.CustomPageTransformer
import com.adstek.home.ui.profile.ViewPagerAdapter
import com.adstek.util.SharedPref
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SwiperFragment : Fragment()  {
    private lateinit var binding: FragmentSwiperBinding
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private val SWIPE_DELAY = 3000L // 3 seconds

    @Inject
    lateinit var sharedPref: SharedPref


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSwiperBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupViewPager()
        setupAutoSwipe()
        setupPageChangeCallback()
    }

    private fun setupViewPager() = with(binding) {
        val pagerAdapter = ViewPagerAdapter(requireActivity())
        viewPager.adapter = pagerAdapter

        // Set up TabLayout with ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, _ ->
            // Leave tab empty for just dots
        }.attach()

        // Add animation transformer
        viewPager.setPageTransformer(CustomPageTransformer())
    }

    private fun setupAutoSwipe() = with(binding) {
        handler = Handler(Looper.getMainLooper())
        Toast.makeText(requireContext(), sharedPref.getPref("IS_AUTO_SWIPE_ENABLED", false).toString(), Toast.LENGTH_SHORT).show()
        runnable = Runnable {
            if (sharedPref.getPref("IS_AUTO_SWIPE_ENABLED", false)) {
                val nextItem = (viewPager.currentItem + 1) % 2
                viewPager.setCurrentItem(nextItem, true)
                if (sharedPref.getPref("IS_AUTO_SWIPE_ENABLED", false)) {
                    startAutoSwipe()
                }            }
        }
    }

    private fun setupPageChangeCallback() = with(binding) {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Reset timer when user swipes
                if (sharedPref.getPref("IS_AUTO_SWIPE_ENABLED", false)) {
                    stopAutoSwipe()
                    startAutoSwipe()
                }
            }
        })
    }



    private fun startAutoSwipe() {
        // Clear any existing callbacks first
        stopAutoSwipe()
        if (sharedPref.getPref("IS_AUTO_SWIPE_ENABLED", false)) {
            handler.postDelayed(runnable, SWIPE_DELAY)
        }
    }


    private fun stopAutoSwipe() {
        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()
        if (sharedPref.getPref("IS_AUTO_SWIPE_ENABLED", false)) {
            startAutoSwipe()
        }
    }

    override fun onPause() {
        super.onPause()
        stopAutoSwipe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopAutoSwipe()
    }
}
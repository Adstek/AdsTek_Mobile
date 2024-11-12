package com.adstek.home.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.adstek.R
import com.adstek.databinding.ActivityViewPagerModelBinding
import com.adstek.home.ui.profile.ViewPagerAdapter
import com.adstek.util.SharedPref
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ViewPagerModelActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    @Inject
    lateinit var sharedPref: SharedPref

    private lateinit var binding: ActivityViewPagerModelBinding
    private var isInitialNavigation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPagerModelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref.setPref("IS_AUTO_SWIPE_ENABLED", true)
        navController = findNavController(this, R.id.nav_host_fragment2)

        // Custom bottom navigation handling
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            // Clear entire back stack and start fresh
            navController.navigate(
                menuItem.itemId,
                null,
                NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setPopUpTo(navController.graph.startDestinationId, true)
                    .build()
            )
            true
        }
    }
}
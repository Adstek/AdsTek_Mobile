package com.adstek

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.adstek.databinding.ActivityMainBinding
import com.adstek.home.HomeActivity
import com.adstek.util.Constants
import com.adstek.util.SharedPref
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

    @AndroidEntryPoint
    class MainActivity : AppCompatActivity() {

        private lateinit var navController: NavController
        private lateinit var binding: ActivityMainBinding

        @Inject
        lateinit var sharedPref: SharedPref

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

            if (sharedPref.getPref(Constants.KEY_IS_SIGNED_IN, false) && sharedPref.getPref(Constants.KEY_IS_EMAIL_VERIFIED, true)) {
                // User is signed in, redirect to HomeActivity
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
                return
            }

            // Initialize the NavController using the NavHostFragment
            navController = findNavController(R.id.navigation_host_fragment)

            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

            // Set the onboarding navigation graph
            val navGraph = navController.navInflater.inflate(R.navigation.onboading_navigation)
            navController.graph = navGraph

        }
    }
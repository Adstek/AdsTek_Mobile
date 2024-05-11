package com.adstek

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adstek.databinding.FragmentSplashScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenFragment : Fragment() {
    private lateinit var binding: FragmentSplashScreenBinding

    // Splash screen timer
    private val splashTimeOut: Long = 4000 // 4 seconds


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSplashScreenBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler().postDelayed({
            // Start your MainActivity

        }, splashTimeOut)
    }

}
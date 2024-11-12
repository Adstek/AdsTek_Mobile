package com.adstek.home.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adstek.R
import com.adstek.databinding.FragmentHomeBinding
import com.adstek.extensions.navigateTo
import com.adstek.extensions.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        binding.gamesLayout.setOnClickListener {
            navigateTo(HomeFragmentDirections.actionHomeFragment2ToGamesFragment())
        }
        binding.driverLayout.setOnClickListener {
            navigateTo(HomeFragmentDirections.actionHomeFragment2ToDriverProfileFragment())
        }
    }
}
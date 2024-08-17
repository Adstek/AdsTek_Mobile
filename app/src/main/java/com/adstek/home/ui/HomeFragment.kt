package com.adstek.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.adstek.databinding.FragmentHomeBinding
import com.adstek.extensions.navigateTo
import com.adstek.home.viewmodel.SharedViewModel
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.gamesLayout.setOnClickListener {
            navigateTo(HomeFragmentDirections.navigateToTrivia())
        }
        binding.driverLayout.setOnClickListener {
            navigateTo(HomeFragmentDirections.actionHomeFragment2ToDriverProfileFragment())
        }
    }
}
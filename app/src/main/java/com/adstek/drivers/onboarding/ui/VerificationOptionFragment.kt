package com.adstek.drivers.onboarding.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adstek.databinding.FragmentVerificationOptionBinding
import com.adstek.extensions.navigateTo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerificationOptionFragment : Fragment() {
    private lateinit var binding: FragmentVerificationOptionBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentVerificationOptionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClicks()
    }

    private fun onClicks() = with(binding){
        phoneLayout.setOnClickListener {
            navigateTo(VerificationOptionFragmentDirections.navigateToVerifyPhone())
        }
        emailLayout.setOnClickListener {
//            navigateTo(VerificationOptionFragmentDirections.navigateToVerifyEmail())
        }
    }
}
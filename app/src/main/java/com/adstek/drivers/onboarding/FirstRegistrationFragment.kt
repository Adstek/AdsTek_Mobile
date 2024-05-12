package com.adstek.drivers.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adstek.databinding.FragmentFirstRegistrationBinding
import com.adstek.util.navigateTo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstRegistrationFragment : Fragment() {
    private lateinit var binding: FragmentFirstRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstRegistrationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickActions()
    }

    private fun onClickActions() = with(binding){
        continueBtn.onClick{
            navigateTo(FirstRegistrationFragmentDirections.navigateToSecondFragment())

        }
    }
}
package com.adstek.drivers.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.adstek.R
import com.adstek.databinding.FragmentSecondRegistrationBinding
import com.adstek.util.popBackStackOrFinish
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [SecondRegistrationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class SecondRegistrationFragment : Fragment() {
  private lateinit var binding: FragmentSecondRegistrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSecondRegistrationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClicks()
    }


    private fun onClicks() = with(binding){
        back.btnBack.setOnClickListener {
            popBackStackOrFinish()
        }
    }
}
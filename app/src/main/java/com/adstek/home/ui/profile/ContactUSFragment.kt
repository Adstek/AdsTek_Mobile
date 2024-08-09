package com.adstek.home.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.adstek.R
import com.adstek.databinding.FragmentContactUSBinding
import com.adstek.extensions.popBackStackOrFinish
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactUSFragment : Fragment() {
   private lateinit var binding: FragmentContactUSBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentContactUSBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
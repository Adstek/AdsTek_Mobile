package com.adstek.home.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.adstek.R
import com.adstek.data.remote.response.ProfileUserResponse
import com.adstek.databinding.FragmentProfileBinding
import com.adstek.extensions.observeLiveData
import com.adstek.extensions.popBackStackOrFinish
import com.adstek.home.ui.games.trivia.viewmodel.TriviaViewModel
import com.adstek.home.ui.profile.viewmodel.ProfileViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.getProfile()

        observeLiveData(profileViewModel.profileResponse) { details ->
            populateData(details)
            details?.message .let {it->
                Glide.with(requireActivity())
                    .load(it?.profile)
                    .placeholder(R.drawable.ic_user_account)
                    .into(binding.profilePic)

            }
        }
        binding.back.btnBack.setOnClickListener {
            popBackStackOrFinish()
        }
    }

    private fun populateData(profileUserResponse: ProfileUserResponse?) = with(binding){
        profileUserResponse?.let {data->
            name.text = data.message.firstName + " " + data.message.lastName
            momoNumber.text = data.message.phoneNumber
        }
    }
}
package com.adstek.home.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.adstek.R
import com.adstek.data.remote.requests.ProfileUpdateRequest
import com.adstek.data.remote.response.ProfileUserResponse
import com.adstek.databinding.FragmentProfileBinding
import com.adstek.extensions.observeLiveData
import com.adstek.extensions.popBackStackOrFinish
import com.adstek.extensions.toast
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

        onClicks()

        profileViewModel.getProfile()

        observeLiveData(profileViewModel.profileResponse) { details ->
            populateData(details)
            details?.message .let {
                Glide.with(requireActivity())
                    .load(it?.profile)
                    .placeholder(R.drawable.ic_user_account)
                    .into(binding.profilePic)

            }
        }

        observeLiveData(profileViewModel.profileUpdateResponse, enableProgressBar = true, onError = {
            toast(it)
        }) {
            toast("Profile updated successfully")
        }

    }

    private fun onClicks() = with(binding) {
        back.btnBack.setOnClickListener {
            popBackStackOrFinish()
        }

        proceedBtn?.onClick {
            updateProfile()
        }
    }

    private fun populateData(profileUserResponse: ProfileUserResponse?) = with(binding){
        profileUserResponse?.let {data->
            firstName.setFieldText(data.message.firstName)
            lastName.setFieldText(data.message.lastName)
            phone.setFieldText(data.message.phoneNumber)
            nationality.setFieldText(data.message.nationality)
            gender.setFieldText(data.message.gender)
            email.setFieldText(data.message.email)
            email.isEnabled = false
        }
    }

    private fun updateProfile() {
        val firstName = binding.firstName.getFieldText()
        val lastName = binding.lastName.getFieldText()
        val phone = binding.phone.getFieldText()
        val nationality = binding.nationality.getFieldText()
        val gender = binding.gender.getFieldText()

        if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || nationality.isEmpty() || gender.isEmpty()) {
            toast("Please fill all the fields")
            return
        }

        val updateProfileRequest = ProfileUpdateRequest(
            firstName = firstName,
            lastName = lastName,
            phoneNumber = phone,
            gender = gender,
            nationality = nationality
        )

        profileViewModel.updateProfile(updateProfileRequest)
    }
}
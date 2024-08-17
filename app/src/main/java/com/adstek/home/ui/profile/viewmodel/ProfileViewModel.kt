package com.adstek.home.ui.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.adstek.data.remote.requests.ProfileUpdateRequest
import com.adstek.data.remote.response.DataState
import com.adstek.data.remote.response.ProfileUserResponse
import com.adstek.extensions.BaseViewModel
import com.adstek.extensions.emitFlowResults
import com.adstek.home.ui.profile.repository.ProfileRepository
import com.adstek.util.SharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val sharedPref: SharedPref
): BaseViewModel() {


    private val _profileResponse: MutableLiveData<DataState<ProfileUserResponse>> = MutableLiveData()
    val profileResponse: LiveData<DataState<ProfileUserResponse>> get() = _profileResponse

    private val _profileUpdateResponse: MutableLiveData<DataState<ProfileUserResponse>> = MutableLiveData()
    val profileUpdateResponse: LiveData<DataState<ProfileUserResponse>> get() = _profileUpdateResponse

    fun getProfile() = emitFlowResults(_profileResponse) {
        profileRepository.getProfile()
    }

    fun updateProfile(updateProfileRequest: ProfileUpdateRequest) = emitFlowResults(_profileUpdateResponse) {
        profileRepository.updateProfile(updateProfileRequest)
    }
}
package com.adstek.home.ui.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.adstek.data.remote.requests.InteractRequest
import com.adstek.extensions.BaseViewModel
import com.adstek.data.remote.response.DataState
import com.adstek.data.remote.response.DidInteractResponse
import com.adstek.data.remote.response.ProfileUserResponse
import com.adstek.data.remote.response.TriviaQuestions
import com.adstek.data.remote.response.User
import com.adstek.extensions.emitFlowResults
import com.adstek.util.SharedPref
import com.adstek.home.ui.games.trivia.repository.TriviaRepository
import com.adstek.home.ui.profile.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val sharedPref: SharedPref
): BaseViewModel() {


    private val _profileResponse: MutableLiveData<DataState<ProfileUserResponse>> = MutableLiveData()
    val profileResponse: LiveData<DataState<ProfileUserResponse>> get() = _profileResponse

    fun getProfile() = emitFlowResults(_profileResponse) {
        profileRepository.getProfile()
    }
}
package com.adstek.home.ui.games.trivia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.adstek.data.remote.requests.InteractRequest
import com.adstek.extensions.BaseViewModel
import com.adstek.data.remote.response.DataState
import com.adstek.data.remote.response.DidInteractResponse
import com.adstek.data.remote.response.TriviaQuestions
import com.adstek.extensions.emitFlowResults
import com.adstek.util.SharedPref
import com.adstek.home.ui.games.trivia.repository.TriviaRepository
import com.adstek.home.ui.profile.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TriviaViewModel @Inject constructor(
    private val triviaRepository: TriviaRepository,
    private val sharedPref: SharedPref,
    private val profileRepository: ProfileRepository
): BaseViewModel() {


    private val _triviaResponse: MutableLiveData<DataState<TriviaQuestions>> = MutableLiveData()
    val triviaResponse: LiveData<DataState<TriviaQuestions>> get() = _triviaResponse

    private val _interactResponse: MutableLiveData<DataState<DidInteractResponse>> = MutableLiveData()
    val interactResponse: LiveData<DataState<DidInteractResponse>> get() = _interactResponse

    fun getTrivia() = emitFlowResults(_triviaResponse) {
        triviaRepository.getTriviaQuestions()
    }

    fun interactWithApi(interactRequest: InteractRequest) = emitFlowResults(_interactResponse) {
        triviaRepository.interact(interactRequest)
    }
}
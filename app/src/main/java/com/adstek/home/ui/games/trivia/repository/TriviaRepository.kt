package com.adstek.home.ui.games.trivia.repository

import com.adstek.api.AdsTekApi
import com.adstek.data.remote.requests.InteractRequest
import com.adstek.data.remote.response.DataState
import com.adstek.data.remote.response.DidInteractResponse
import com.adstek.data.remote.response.TriviaQuestions
import com.adstek.util.SharedPref
import com.adstek.extensions.makeNetworkRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TriviaRepository @Inject constructor(
    private val adsTekApi: AdsTekApi,
    private val sharedPrefFunctions: SharedPref
) {

    fun getTriviaQuestions(): Flow<DataState<TriviaQuestions>> {
        return makeNetworkRequest { adsTekApi.getTriviaQuestions() }
    }

    fun interact(interactRequest: InteractRequest): Flow<DataState<DidInteractResponse>> {
        return makeNetworkRequest { adsTekApi.interact(interactRequest) }
    }

}
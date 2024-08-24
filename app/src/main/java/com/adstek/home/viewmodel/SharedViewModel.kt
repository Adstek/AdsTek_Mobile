package com.adstek.home.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.adstek.extensions.BaseViewModel
import com.adstek.home.ui.games.trivia.repository.TriviaRepository
import com.adstek.home.ui.profile.repository.ProfileRepository
import com.adstek.home.ui.profile.viewmodel.ProfileViewModel
import com.adstek.scheduler.scheduleWeeklyVideoDownload
import com.adstek.util.SharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val sharedPref: SharedPref
): BaseViewModel() {

    fun fetchVideoListAndDownload(context: Context) {
        scheduleWeeklyVideoDownload(context)
    }
}
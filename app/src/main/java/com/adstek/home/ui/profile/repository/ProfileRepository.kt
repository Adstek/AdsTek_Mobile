package com.adstek.home.ui.profile.repository

import android.app.Application
import android.util.Log
import com.adstek.api.AdsTekApi
import com.adstek.data.local.dao.VideoAdDao
import com.adstek.data.local.model.VideoAd
import com.adstek.data.remote.response.DataState
import com.adstek.data.remote.response.ProfileUserResponse
import com.adstek.extensions.makeNetworkRequest
import com.adstek.util.SharedPref
import com.adstek.util.downloadVideo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val adsTekApi: AdsTekApi,
    private val sharedPrefFunctions: SharedPref,
    private val videoAdDao: VideoAdDao,
    private val application: Application
) {

    private val TAG = "ProfileRepository"

    fun getProfile(): Flow<DataState<ProfileUserResponse>> {
        return makeNetworkRequest { adsTekApi.getProfile() }
    }

    suspend fun fetchAndCacheVideoList() {
       try {
           val response = adsTekApi.getVideosList()
           if (!response.isSuccessful) {
               Log.d(TAG, "Video list api call failed")
               return
           }
           val videoAdsList: MutableList<VideoAd> = mutableListOf()
           videoAdsList.clear()
           videoAdsList.addAll(response.body()?.message?.results ?: emptyList())

           videoAdsList.forEach { videoAd ->
               videoAd.downloadVideo(application) { _ ->

               }
           }
       } catch (e: Exception) {
           Log.d(TAG, "Video list api call error caught")
           e.printStackTrace()
       }
    }
}
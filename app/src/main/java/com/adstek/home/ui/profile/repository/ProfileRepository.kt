package com.adstek.home.ui.profile.repository

import android.net.Uri
import androidx.core.net.toFile
import com.adstek.api.AdsTekApi
import com.adstek.data.remote.response.RegisterReponse
import com.adstek.data.remote.requests.LoginRequest
import com.adstek.data.remote.requests.VerifyEmail
import com.adstek.data.remote.requests.RegisterUserModel
import com.adstek.data.remote.response.DataState
import com.adstek.data.remote.response.SignInResponse
import com.adstek.data.remote.requests.ResendOTP
import com.adstek.data.remote.requests.ResetPassword
import com.adstek.data.remote.requests.StartResetPassword
import com.adstek.data.remote.response.ProfileUserResponse
import com.adstek.data.remote.response.User
import com.adstek.util.SharedPref
import com.adstek.extensions.makeNetworkRequest
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val adsTekApi: AdsTekApi,
    private val sharedPrefFunctions: SharedPref
) {

    fun getProfile(): Flow<DataState<ProfileUserResponse>> {
        return makeNetworkRequest { adsTekApi.getProfile() }
    }

    fun getVideosList(): Flow<DataState<Any>> {
        return makeNetworkRequest { adsTekApi.getVideosList() }
    }
}
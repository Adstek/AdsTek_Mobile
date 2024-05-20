package com.adstek.api

import com.adstek.data.remote.ApiResponse
import com.adstek.data.remote.RegisterReponse
import com.adstek.data.remote.models.LoginRequest
import com.adstek.data.remote.models.ResetPassword
import com.adstek.data.remote.models.VerifyEmail
import com.adstek.data.remote.models.auth.PhoneNumber
import com.adstek.data.remote.models.auth.StartResetPassword
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface AdsTekApi {

    @Multipart
    @POST("auth/register/")
    suspend fun registerDriver(
        @PartMap userData: MutableMap<String,RequestBody>,
        @Part profile: MultipartBody.Part,
        @Part idFile: MultipartBody.Part,
    ): Response<RegisterReponse>

    suspend fun sendOtpOnPhoneNumber(@Body phone: PhoneNumber): ApiResponse<RegisterReponse>

    @POST("auth/verify-email")
    suspend fun verifyEmail(@Body verifyEmail: VerifyEmail): Response<Any>

    @POST("auth/reset-password/")
    suspend fun startResetPassword(@Body email: StartResetPassword): Response<Any>

    @POST("auth/confirm-reset-password/")
    suspend fun confirmResetPassword(@Body resetPassword: ResetPassword): Response<Any>


    @POST("auth/login/")
    suspend fun login(@Body loginRequest: LoginRequest): Response<Any>

}
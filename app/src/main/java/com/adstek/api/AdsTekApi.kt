package com.adstek.api

import com.adstek.data.remote.requests.InteractRequest
import com.adstek.data.remote.response.ApiResponse
import com.adstek.data.remote.response.RegisterReponse
import com.adstek.data.remote.response.SignInResponse
import com.adstek.data.remote.response.TriviaQuestions
import com.adstek.data.remote.requests.LoginRequest
import com.adstek.data.remote.requests.PaymentWalletRequest
import com.adstek.data.remote.requests.ResendOTP
import com.adstek.data.remote.requests.ResetPassword
import com.adstek.data.remote.requests.VerifyEmail
import com.adstek.data.remote.requests.PhoneNumber
import com.adstek.data.remote.requests.StartResetPassword
import com.adstek.data.remote.response.DidInteractResponse
import com.adstek.data.remote.response.ProfileUserResponse
import com.adstek.data.remote.response.User
import com.adstek.data.remote.response.WalletResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path

interface AdsTekApi {

    @Multipart
    @POST("auth/register/")
    suspend fun registerDriver(
        @PartMap userData: MutableMap<String,RequestBody>,
        @Part profile: MultipartBody.Part,
        @Part idFile: MultipartBody.Part,
    ): Response<RegisterReponse>

    suspend fun sendOtpOnPhoneNumber(@Body phone: PhoneNumber): ApiResponse<RegisterReponse>

    @POST("auth/verify-email/")
    suspend fun verifyEmail(@Body verifyEmail: VerifyEmail): Response<Any>

   @POST("auth/resend-otp/")
    suspend fun resendOtp(@Body resendOTP: ResendOTP): Response<Any>

    @POST("auth/reset-password/")
    suspend fun startResetPassword(@Body email: StartResetPassword): Response<Any>

    @POST("auth/confirm-reset-password/")
    suspend fun confirmResetPassword(@Body resetPassword: ResetPassword): Response<Any>

    @POST("auth/login/")
    suspend fun login(@Body loginRequest: LoginRequest): Response<SignInResponse>

    @POST("auth/logout/")
    suspend fun logout(): Response<Any>

    @POST("driver/questions/interact/")
    suspend fun interact(@Body interactRequest: InteractRequest): Response<DidInteractResponse>

    @GET("driver/questions/")
    suspend fun getTriviaQuestions(): Response<TriviaQuestions>

    @POST("driver/payment/method/create/")
    suspend fun addPaymentWallet(@Body paymentWalletRequest: PaymentWalletRequest): Response<Any>

    @GET("driver/payment/method/")
    suspend fun getAllWallet():Response<List<WalletResponse>>

    @DELETE("driver/payment/method/{walletId}/")
    suspend fun deleteWallet(@Path("walletId") walletId: String):Response<Any>

    @PATCH("driver/payment/method/{walletId}/")
    suspend fun editWallet(@Path("walletId") walletId: String, @Body walletResponse: PaymentWalletRequest):Response<Any>

    @GET("auth/profile/")
    suspend fun getProfile():Response<ProfileUserResponse>

    @GET("admin/general/videolist/")
        suspend fun getVideosList():Response<Any>


}
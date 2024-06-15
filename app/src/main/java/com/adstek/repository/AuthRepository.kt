package com.adstek.repository

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
import com.adstek.util.SharedPref
import com.adstek.extensions.makeNetworkRequest
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val adsTekApi: AdsTekApi,
    private val sharedPrefFunctions: SharedPref
) {

    val map: MutableMap<String, RequestBody> = mutableMapOf()

//    fun registerDriver(
//         registerUserModel: RegisterUserModel
//     ): Flow<DataState<Any>> {
//        var multipartImage: MultipartBody.Part? = null
//
//        val fileUri = Uri.parse(registerUserModel.profileImage)
//        val file = fileUri.toFile()
//
//        val firstName = registerUserModel.firstName?.let { createPartFromString(it) }
//        val lastName = registerUserModel.lastName?.let { createPartFromString(it) }
//
//        firstName?.let { map.put("name", it) }
//        lastName?.let { map.put("email", it) }
//
//
//        val requestFile: RequestBody = RequestBody.create(
//            "image/jpg".toMediaType(),
//            file
//        )
//
//        multipartImage = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
//
//
//        val userId: RequestBody = registerUserModel.email!!.toRequestBody("multipart/formdata".toMediaTypeOrNull())
//
//
//        return makeNetworkRequest { adsTekApi.registerDriver( userId,multipartImage) }
//    }

    fun registerDriver(registerUserModel: RegisterUserModel): Flow<DataState<RegisterReponse>> {
        val map: MutableMap<String, RequestBody> = mutableMapOf()

        val profileUri = Uri.parse(registerUserModel.profileImage)
        val profileImage = profileUri.toFile()


        val IdUri = Uri.parse(registerUserModel.profileImage)
        val IdImage = profileUri.toFile()


        val firstName = registerUserModel.firstName?.toRequestBody("text/plain".toMediaTypeOrNull())
        val lastName = registerUserModel.lastName?.toRequestBody("text/plain".toMediaTypeOrNull())
        val email = registerUserModel.email?.toRequestBody("text/plain".toMediaTypeOrNull())
        val gender = registerUserModel.gender?.toRequestBody("text/plain".toMediaTypeOrNull())
        val phoneNumber = registerUserModel.phoneNumber?.toRequestBody("text/plain".toMediaTypeOrNull())
        val nationality = registerUserModel.nationality?.toRequestBody("text/plain".toMediaTypeOrNull())
        val nationaldType = registerUserModel.nationaldType?.toRequestBody("text/plain".toMediaTypeOrNull())
        val nationalIdNumber = registerUserModel.nationalIdNumber?.toRequestBody("text/plain".toMediaTypeOrNull())
        val numberPlate = registerUserModel.numberPlate?.toRequestBody("text/plain".toMediaTypeOrNull())
        val password = registerUserModel.password?.toRequestBody("text/plain".toMediaTypeOrNull())

        firstName?.let { map["first_name"] = it }
        lastName?.let { map["last_name"] = it }
        email?.let { map["email"] = it }
        gender?.let { map["gender"] = it }
        phoneNumber?.let { map["phone_number"] = it }
        nationality?.let { map["nationality"] = it }
        nationaldType?.let { map["national_id_type"] = it }
        nationalIdNumber?.let { map["national_id_number"] = it }
        numberPlate?.let { map["number_plate"] = it }
        password?.let { map["password"] = it }

        val requestFile: RequestBody = RequestBody.create("image/jpg".toMediaType(), profileImage)
        val multipartImage = MultipartBody.Part.createFormData("profile", profileImage.getName(), requestFile)

        val idRequestFile: RequestBody = RequestBody.create("image/jpg".toMediaType(), IdImage)
        val idMultipartImage = MultipartBody.Part.createFormData("national_id_image", profileImage.getName(), idRequestFile)



        return makeNetworkRequest { adsTekApi.registerDriver(map, multipartImage, idMultipartImage) }
    }

    fun verifyEmail(verifyEmail: VerifyEmail): Flow<DataState<Any>> {
        return makeNetworkRequest { adsTekApi.verifyEmail(verifyEmail) }
    }

    fun resendOTP(resendOTP: ResendOTP): Flow<DataState<Any>> {
        return makeNetworkRequest { adsTekApi.resendOtp(resendOTP) }
    }

    fun startResetPassword(startResetPassword: StartResetPassword): Flow<DataState<Any>> {
        return makeNetworkRequest { adsTekApi.startResetPassword(startResetPassword) }
    }

    fun confirmResetPassword(resetPassword: ResetPassword): Flow<DataState<Any>> {
        return makeNetworkRequest { adsTekApi.confirmResetPassword(resetPassword) }
    }

    fun login(loginRequest: LoginRequest): Flow<DataState<SignInResponse>> {
        return makeNetworkRequest { adsTekApi.login(loginRequest) }
    }

//    fun createPartFromString(stringData: String): RequestBody {
//        return stringData.toRequestBody("text/plain".toMediaTypeOrNull())
//    }
    private fun createPartFromString(string: String): RequestBody {
        return RequestBody.create(MultipartBody.FORM, string)
    }

}
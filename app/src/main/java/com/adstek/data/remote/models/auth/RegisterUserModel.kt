package com.adstek.data.remote.models.auth

import com.google.gson.annotations.SerializedName
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.Serial

data class RegisterUserModel(
    @SerializedName("first_name")
    var firstName: String? = null,
    @SerializedName("last_name")
    var lastName: String? = null,
    var email: String? = null,
    var gender: String? = null,
    @SerializedName("phone_number")
    var phoneNumber: String? = null,
    var nationality: String? = null,
    @SerializedName("national_id_type")
    var nationaldType: String? = null,
    @SerializedName("national_id_number")
    var nationalIdNumber: String? = null,
    @SerializedName("number_plate")
    var numberPlate: String? = null,
    var password: String? = null,
    @SerializedName("profile")
    var profileImage: String? = null,
    @SerializedName("national_id_image")
    var nationalDImage: String? = null
) {
}

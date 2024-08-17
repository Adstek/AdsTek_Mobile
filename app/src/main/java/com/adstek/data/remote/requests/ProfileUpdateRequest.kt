package com.adstek.data.remote.requests

import com.google.gson.annotations.SerializedName

data class ProfileUpdateRequest(
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("nationality") val nationality: String
)

package com.adstek.data.remote.models.auth

import com.google.gson.annotations.SerializedName

data class PhoneNumber(
    @SerializedName("phone_number")
    val phoneNumber: String
)
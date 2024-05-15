package com.adstek.data.remote.models

data class VerifyEmail(
    val user_id: Int,
    val otp: Int
)
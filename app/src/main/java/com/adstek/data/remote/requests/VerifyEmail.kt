package com.adstek.data.remote.requests

data class VerifyEmail(
    val user_id: Int,
    val otp: Int
)
package com.adstek.data.remote.requests

data class ResetPassword(
    val password: String,
    val new_password: String
)
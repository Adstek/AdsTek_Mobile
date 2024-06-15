package com.adstek.data.remote.response

data class SignInResponse(
    val refresh: String,
    val access: String,
    val is_email_verified: Boolean,
    val user_id: Int

)

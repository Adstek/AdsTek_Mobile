package com.adstek.data.remote

    data class RegisterReponse(
    val status: String,
    val detail: String,
    val user_id: String,
    val message: Map<String, List<String>>,
)

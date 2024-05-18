package com.adstek.data.remote

data class ErrorResponse(
    val message: Map<String, List<String>>,
    val status_code: Int
)

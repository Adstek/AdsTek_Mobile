package com.adstek.data.remote.response

data class ErrorResponse(
    val message: Map<String, List<String>>,
    val status_code: Int
)

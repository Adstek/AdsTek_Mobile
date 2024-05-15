package com.adstek.data.remote


data class ApiResponse<T>(
    val message: String,
    val data: T
)

package com.adstek

data class VideoModel(
    val id: String,
    val videoUrl: String,
    val title: String? = null,
    val thumbnail: String? = null,
    val duration: Long = 0
)
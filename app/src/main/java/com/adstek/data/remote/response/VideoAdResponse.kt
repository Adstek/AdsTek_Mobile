package com.adstek.data.remote.response

import com.adstek.data.local.model.VideoAd

data class VideoAdResponse(
    val message: Data? = null
)

data class Data (
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<VideoAd>
)


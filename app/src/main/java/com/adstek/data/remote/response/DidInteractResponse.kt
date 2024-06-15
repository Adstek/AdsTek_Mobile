package com.adstek.data.remote.response

data class DidInteractResponse(
    val message: DidInteractResponseData,
)

data class DidInteractResponseData(
    val message: String,
    val is_advideo: Boolean,
    val video: String?,
)

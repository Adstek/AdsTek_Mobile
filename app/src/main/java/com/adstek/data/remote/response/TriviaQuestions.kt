package com.adstek.data.remote.response

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement


@Serializable
data class TriviaQuestions (
    val message: Message
)

@Serializable
data class Message (
    val count: Long,
    val next: String,
    val previous: JsonElement? = null,
    val results: List<TriviaResult>
)

@Serializable
data class TriviaResult (
    val question: String,
    val options: List<Option>
)

@Serializable
data class Option (
    val question: String,
    val answer: String? = null,

    val option_a: String? = null,

    val option_b: String? = null,

    val option_c: String? = null,

    val option_d: String? = null
)
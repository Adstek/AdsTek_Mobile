package com.adstek.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
data class User(
    val id: Long,

    @SerializedName("last_login") val lastLogin: String? = null,

    @SerializedName("first_name") val firstName: String,

    @SerializedName("last_name") val lastName: String,

    @SerializedName("is_active") val isActive: Boolean,

    @SerializedName("date_joined") val dateJoined: String,

    val email: String, val role: String, val gender: String, val status: String,

    @SerializedName("phone_number") val phoneNumber: String,

    val nationality: String,

    @SerializedName("national_id_type") val nationalIDType: String? = null,

    @SerializedName("national_id_number") val nationalIDNumber: String,

    @SerializedName("number_plate") val numberPlate: String,

    val profile: String, val points: Long,

    @SerializedName("national_id_image") val nationalIDImage: String,

    @SerializedName("is_email_verified") val isEmailVerified: Boolean,

    @SerializedName("interaction_count") val interactionCount: Long
): Parcelable
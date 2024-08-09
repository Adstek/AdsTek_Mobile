package com.adstek.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
data class WalletResponse(
    val id: Long,
    val user: User,

    @SerializedName("bank_name")
    val bankName: String,

    val code: String? = "",

    @SerializedName("account_number")
    val accountNumber: String? = "",

    @SerializedName("account_name")
    val accountName: String? = "",

    val email: String? = "",

    @SerializedName("phone_number")
    val phoneNumber: String? = "",

    @SerializedName("payment_type")
    val paymentType: String? = "",

    @SerializedName("date_created")
    val dateCreated: String? = ""
): Parcelable

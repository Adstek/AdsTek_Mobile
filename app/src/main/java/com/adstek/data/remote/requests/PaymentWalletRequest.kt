package com.adstek.data.remote.requests

import com.google.gson.annotations.SerializedName

data class PaymentWalletRequest(
    @SerializedName("bank_name")
    val bankName: String,
    @SerializedName("bank_code")
    val bankCode: String,
    @SerializedName("account_number")
    val accountNumber: String,
    @SerializedName("account_name")
    val accountName: String,
    @SerializedName("email")
    val email: String ,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("payment_type")
    val paymentType: String,

)
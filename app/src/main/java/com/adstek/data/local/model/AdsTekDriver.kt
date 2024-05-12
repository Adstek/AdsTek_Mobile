package com.adstek.data.local.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "user_table")
data class AdsTekDriver(
    @PrimaryKey(autoGenerate = false)
    var baseId: Int,
    var msisdn: String,
    var name: String? = null,

): Parcelable

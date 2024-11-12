package com.adstek.data.remote.models

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class GamesListModel(
    val id: Int,
    val name: String,
    val description: String,
    @DrawableRes val icon: Int
): Parcelable

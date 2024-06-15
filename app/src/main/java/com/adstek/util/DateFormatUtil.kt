package com.adstek.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


    fun getCurrentTimeInISOFormat(): String {
    // Define the formatter with the required format
    val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
    } else {
        TODO("VERSION.SDK_INT < O")
    }

    // Get the current time and format it
    val currentTime = ZonedDateTime.now().format(formatter)

    return currentTime
}
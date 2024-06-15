package com.adstek.data.remote.requests

import android.text.format.DateUtils
import com.adstek.util.getCurrentTimeInISOFormat
import java.util.Date

data class InteractRequest(
    val interaction: Boolean,
    val started_at: String = getCurrentTimeInISOFormat()
)
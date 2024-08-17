package com.adstek.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "video_ad_table")
data class VideoAd(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val video: String,
    var localVideo: String,
    val number_of_posts: Int,
    var created_at: String
)
package com.adstek.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.adstek.data.local.model.VideoAd

@Dao
interface VideoAdDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVideoAd(videoAd: VideoAd)
}
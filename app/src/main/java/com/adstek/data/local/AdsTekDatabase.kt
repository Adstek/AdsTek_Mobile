package com.adstek.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.adstek.data.local.dao.VideoAdDao
import com.adstek.data.local.model.AdsTekDriver
import com.adstek.data.local.model.VideoAd


@Database(entities = [AdsTekDriver::class, VideoAd::class], version = 1)
abstract class AdsTekDatabase : RoomDatabase() {
    abstract fun videoAdDao(): VideoAdDao
}
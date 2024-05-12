package com.adstek.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.adstek.data.local.model.AdsTekDriver


@Database(entities = [AdsTekDriver::class], version = 1)
abstract class AdsTekDatabase : RoomDatabase() {

}
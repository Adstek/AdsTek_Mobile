package com.adstek.di

import android.content.Context
import androidx.room.Room
import com.adstek.data.local.AdsTekDatabase
import com.adstek.util.SharedPref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun providesUserFunctions(@ApplicationContext context: Context): SharedPref {
        return SharedPref(context)
    }

    @Singleton
    @Provides
    fun providesAdsTekDatabase(@ApplicationContext context: Context): AdsTekDatabase =
        Room.databaseBuilder(
            context,
            AdsTekDatabase::class.java,
            "AdsTekDatabase"
        ).fallbackToDestructiveMigration().build()


}

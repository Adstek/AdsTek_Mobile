package com.adstek

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory

import androidx.work.Configuration
//import com.adstek.scheduler.HiltWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class AdsTekApp: Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory : HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun workManagerConfig (): Configuration {
        return if (BuildConfig.DEBUG) {
            Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.DEBUG)
                .setWorkerFactory(workerFactory)
                .build()
        } else {
            Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.INFO)
                .setWorkerFactory(workerFactory)
                .build()
        }
    }

    override val workManagerConfiguration: Configuration
        get() = workManagerConfig()

}
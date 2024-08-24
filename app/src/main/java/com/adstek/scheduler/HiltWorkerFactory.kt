package com.adstek.scheduler

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HiltWorkerFactory @Inject constructor(
    private val workerFactoryMap: Map<Class<out ListenableWorker>, @JvmSuppressWildcards ChildWorkerFactory>
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        val workerClass = Class.forName(workerClassName)
        val foundEntry = workerFactoryMap.entries.find { workerClass.isAssignableFrom(it.key) }
        val factory = foundEntry?.value ?: return null
        return factory.create(appContext, workerParameters)
    }
}
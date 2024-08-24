package com.adstek.di

import androidx.hilt.work.HiltWorkerFactory
import androidx.work.WorkerFactory
import com.adstek.scheduler.ChildWorkerFactory
import com.adstek.scheduler.VideoDownloadWorker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
@InstallIn(SingletonComponent::class)
abstract class WorkerModule {

    @Binds
    @IntoMap
    @ClassKey(VideoDownloadWorker::class)
    abstract fun bindVideoDownloadWorker(factory: VideoDownloadWorker.Factory): ChildWorkerFactory

    @Binds
    abstract fun bindWorkerFactory(factory: HiltWorkerFactory): WorkerFactory
}
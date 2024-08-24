package com.adstek.scheduler

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.adstek.home.ui.profile.repository.ProfileRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

@HiltWorker
class VideoDownloadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val profileRepository: ProfileRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            Timber.i("VideoDownloadWorker download started")
            profileRepository.fetchAndCacheVideoList()
            Timber.i("VideoDownloadWorker download completed")
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    // Factory for creating instances of VideoDownloadWorker
    class Factory @Inject constructor(
        private val profileRepository: ProfileRepository
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, workerParams: WorkerParameters): ListenableWorker {
            return VideoDownloadWorker(appContext, workerParams, profileRepository)
        }
    }
}
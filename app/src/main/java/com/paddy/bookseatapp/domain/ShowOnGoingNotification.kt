package com.paddy.bookseatapp.domain

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.paddy.bookseatapp.data.worker.OnGoingNotificationWorker
import com.paddy.bookseatapp.utils.enableWorkerLogging
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowOnGoingNotification @Inject constructor(private val workManager: WorkManager) {

    fun show() {
        val workRequest = OneTimeWorkRequestBuilder<OnGoingNotificationWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                    .build()
            )
            .addTag(NOTIFICATION_WORK)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 1, TimeUnit.SECONDS)
            .build()
            .enableWorkerLogging()

        workManager
            .beginUniqueWork(NOTIFICATION_WORK, ExistingWorkPolicy.REPLACE, workRequest)
            .enqueue()
    }

    fun cancel() {
        workManager.cancelUniqueWork(NOTIFICATION_WORK)
    }

    companion object {
        const val NOTIFICATION_WORK = "notification_work"
    }

}
package com.paddy.bookseatapp.domain

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.paddy.bookseatapp.data.worker.BookSeatWorker
import com.paddy.bookseatapp.utils.enableWorkerLogging
import dagger.hilt.android.scopes.ViewModelScoped
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ViewModelScoped
class BookSeat @Inject constructor(private val workManager: WorkManager) {

    suspend fun bookSeat(scanResult: String) {
        val workRequest = OneTimeWorkRequestBuilder<BookSeatWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                    .build()
            )
            .setInputData(Data.Builder().putString(SCAN_RESULT, scanResult).build())
            .addTag(TIMER_WORK)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 1, TimeUnit.SECONDS)
            .build()
            .enableWorkerLogging()

        workManager
            .beginUniqueWork(TIMER_WORK, ExistingWorkPolicy.REPLACE, workRequest)
            .enqueue()
    }

    suspend fun cancel() {
        workManager.cancelUniqueWork(TIMER_WORK)
    }

    companion object {
        const val SCAN_RESULT = "scan_result"
        const val TIMER_WORK = "timer_work"
    }

}
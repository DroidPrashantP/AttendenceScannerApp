package com.paddy.bookseatapp.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.paddy.bookseatapp.domain.BookSeat
import com.paddy.bookseatapp.domain.BookSeatResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class BookSeatWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val bookSeatResult: BookSeatResult
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val scanResult = inputData.getString(BookSeat.SCAN_RESULT)
        bookSeatResult.saveTime(scanResult!!)
        return Result.success()
    }
}
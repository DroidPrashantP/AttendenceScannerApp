package com.paddy.bookseatapp.domain

import android.content.Context
import com.paddy.bookseatapp.R
import com.paddy.bookseatapp.utils.NetworkUtils
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SubmitLibrarySession @Inject constructor(
    private val libraryRepository: LibraryRepository,
    private val countUpTimer: CountUpTimer,
    private val bookSeat: BookSeat,
    private val validateSession: ValidateSession,
    private val calculateTotalAmount : CalculateTotalAmount
) {

    suspend fun submit(context: Context, endQrrScanResult: String, endTime:Long) {
        if (NetworkUtils.isNetworkConnected(context)){
            if (validateSession.isValid(endQrrScanResult)) {
                endSession(endTime)
            } else {
                throw Exception(context.getString(R.string.please_scan_qr_code_msg))
            }
        }
        else{
            throw Exception(context.getString(R.string.network_connection_msg))
        }
    }

    private suspend fun endSession(endTime:Long) {
       // Stop the timer
        countUpTimer.stopTimer()

        // cancel the worker
        bookSeat.cancel()

        // ends the session by updating values in db
        libraryRepository.endSession(endTime)

        // calculates the total amount and updates to db
        calculateTotalAmount.calculateTotalAmount()

        // gets data from db & call api for submit
        libraryRepository.submitLibrarySession()
    }

}
package com.paddy.bookseatapp.ui.fragments

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.paddy.bookseatapp.data.model.DataResult
import com.paddy.bookseatapp.domain.BookSeat
import com.paddy.bookseatapp.domain.GetUpdatedTime
import com.paddy.bookseatapp.domain.LibrarySessionStatus
import com.paddy.bookseatapp.domain.SubmitLibrarySession
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class BookSeatInLibraryViewModel @Inject constructor(
    private var bookSeat: BookSeat,
    private var getUpdatedTime: GetUpdatedTime,
    private val submitLibrarySession: SubmitLibrarySession,
    private val librarySessionStatus: LibrarySessionStatus
) : ViewModel() {

    suspend fun bookSeat(qrScanResult: String) {
        bookSeat.bookSeat(qrScanResult)
    }

    suspend fun getUpdatedTime() = liveData {
            try {
                getUpdatedTime.get().collect {
                    emit(DataResult(data = it))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(DataResult(error = "Something went wrong!"))
            }
    }

    suspend fun submitPayload(context: Context?, scanResult: String, endTime: Long) = liveData {
            context?.let {
                try {
                    emit(DataResult(data = submitLibrarySession.submit(it, scanResult, endTime)))
                } catch (e: Exception) {
                    emit(DataResult(error = e.message))
                }
            }
    }

    suspend fun getLibraryStatus() = liveData {
        librarySessionStatus.get().collect {
            emit(it)
        }
    }

    suspend fun clearSession() = liveData {
        librarySessionStatus.clearSession()
        emit(true)
    }
}
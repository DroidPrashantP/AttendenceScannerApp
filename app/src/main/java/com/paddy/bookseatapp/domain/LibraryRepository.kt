package com.paddy.bookseatapp.domain

import com.paddy.bookseatapp.data.api.ApiService
import com.paddy.bookseatapp.data.model.LibraryQRScanResult
import com.paddy.bookseatapp.data.model.OnGoingTimer
import com.paddy.bookseatapp.data.db.BookSeatDao
import com.paddy.bookseatapp.data.model.LibrarySessionPayload
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LibraryRepository @Inject constructor(
    private var bookSeatDao: BookSeatDao,
    private var apiService: ApiService
) {

    suspend fun saveQrScanResult(scanResult: LibraryQRScanResult, startTime: Long) {
        val activeSession = scanResult.copy(sessionStatus = true, startTime = startTime)
        bookSeatDao.save(activeSession)
    }

    suspend fun updateTime(onGoingTimer: OnGoingTimer) {
        val scanResult = withContext(Dispatchers.IO) {
            bookSeatDao.getQrResult()
        }
        withContext(Dispatchers.IO) {
            bookSeatDao.updateTime(onGoingTimer.hour, onGoingTimer.minute, onGoingTimer.seconds, scanResult.locationId)
        }
    }

    suspend fun getLibrarySession(): LibraryQRScanResult {
        return withContext(Dispatchers.IO) {
            bookSeatDao.getQrResult()
        }
    }

    suspend fun getLibrarySessionStatus(): Flow<Boolean> {
        return bookSeatDao.getSessionStatus()
            .flowOn(Dispatchers.IO)
            .distinctUntilChanged()
    }

    suspend fun getUpdatedTime(): Flow<LibraryQRScanResult> {
        return bookSeatDao.getUpdatedTime()
            .flowOn(Dispatchers.IO)
            .distinctUntilChanged()
    }

    suspend fun clearSession() {
        bookSeatDao.deleteSession()
    }

    suspend fun endSession(endTime: Long) {
        val scanResult = withContext(Dispatchers.IO) {
            bookSeatDao.getQrResult()
        }
        withContext(Dispatchers.IO) {
            bookSeatDao.endSession(active = false, endTime = endTime, id = scanResult.locationId)
        }
    }

    suspend fun submitLibrarySession() {
        val libraryQRScanResult = withContext(Dispatchers.IO) {
            bookSeatDao.getQrResult()
        }
        val totalTimeSpent = (libraryQRScanResult.endTime - libraryQRScanResult.startTime) / 60000
        apiService.submitSession(LibrarySessionPayload(libraryQRScanResult.locationId, totalTimeSpent, libraryQRScanResult.totalPrice))
    }

    suspend fun updateAmount(price: Float) {
        val scanResult = withContext(Dispatchers.IO) {
            bookSeatDao.getQrResult()
        }
        withContext(Dispatchers.IO) {
            bookSeatDao.updateAmount(price, scanResult.locationId)
        }
    }
}
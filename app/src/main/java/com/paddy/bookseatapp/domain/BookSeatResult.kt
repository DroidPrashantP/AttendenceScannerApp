package com.paddy.bookseatapp.domain

import com.paddy.bookseatapp.utils.CommonUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookSeatResult @Inject constructor(
    private val repository: LibraryRepository,
    private val countUpTimer: CountUpTimer
) {

    suspend fun saveTime(scanResult: String) {
        repository.saveQrScanResult(scanResult = CommonUtils.parseUnformattedJson(scanResult) ,startTime = System.currentTimeMillis())
        countUpTimer.start()
    }
}
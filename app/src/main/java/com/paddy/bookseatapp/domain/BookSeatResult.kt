package com.paddy.bookseatapp.domain

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookSeatResult @Inject constructor(
    private val repository: LibraryRepository,
    private val countUpTimer: CountUpTimer,
    private var libraryDataParser: LibraryDataParser) {

    suspend fun saveTime(scanResult: String) {
        repository.saveLibrarySessionDataInDB(scanResult = libraryDataParser.parseUnformattedJson(scanResult), startTime = System.currentTimeMillis())
        countUpTimer.start()
    }
}
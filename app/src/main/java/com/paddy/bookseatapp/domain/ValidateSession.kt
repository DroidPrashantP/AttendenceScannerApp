package com.paddy.bookseatapp.domain

import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class ValidateSession @Inject constructor(private val repository: LibraryRepository,
                                          private var libraryDataParser : LibraryDataParser) {

    suspend fun isValid(endQRrScanResult: String): Boolean {
        val activeSession = repository.getLibrarySession()
        val endSession = libraryDataParser.parseUnformattedJson(endQRrScanResult)
        return activeSession.locationId == endSession.locationId
    }
}
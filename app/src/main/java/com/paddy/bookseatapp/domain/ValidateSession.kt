package com.paddy.bookseatapp.domain

import com.paddy.bookseatapp.utils.CommonUtils
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class ValidateSession @Inject constructor(private val repository: LibraryRepository) {

    suspend fun isValid(endQRrScanResult: String): Boolean {
        val activeSession = repository.getLibrarySession()
        val endSession = CommonUtils.parseUnformattedJson(endQRrScanResult)
        return activeSession.locationId == endSession.locationId
    }
}
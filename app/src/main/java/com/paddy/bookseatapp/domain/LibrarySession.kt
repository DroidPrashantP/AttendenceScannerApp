package com.paddy.bookseatapp.domain

import com.paddy.bookseatapp.data.model.LibraryQRScanResult
import javax.inject.Inject

class LibrarySession @Inject constructor(private val repository: LibraryRepository) {

    suspend fun get(): LibraryQRScanResult {
        return repository.getLibrarySession()
    }
}
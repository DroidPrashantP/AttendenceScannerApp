package com.paddy.bookseatapp.domain

import com.paddy.bookseatapp.data.model.LibraryQRScanResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetUpdatedTime @Inject constructor(private val repository: LibraryRepository) {

    suspend fun get(): Flow<LibraryQRScanResult> {
        return repository.getUpdatedTime().filterNotNull()
    }
}
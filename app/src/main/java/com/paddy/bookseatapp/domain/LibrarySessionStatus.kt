package com.paddy.bookseatapp.domain

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

@ViewModelScoped
class LibrarySessionStatus @Inject constructor(private val libraryRepository: LibraryRepository) {

    suspend fun get(): Flow<Boolean> {
        return libraryRepository.getLibrarySessionStatus().filterNotNull()
    }

    suspend fun clearSession() {
        return libraryRepository.clearSession()
    }
}
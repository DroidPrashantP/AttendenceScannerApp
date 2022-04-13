package com.paddy.bookseatapp.data.api

import com.paddy.bookseatapp.data.model.LibrarySessionPayload
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("submit-session")
    suspend fun submitSession(@Body librarySessionPayload: LibrarySessionPayload): ResponseBody
}
package com.paddy.bookseatapp.data.model

import androidx.annotation.Keep

@Keep
data class LibrarySessionPayload(var location_id: String,
                                 val time_spent: Long,
                                 val end_time: Float)

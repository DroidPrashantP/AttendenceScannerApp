package com.paddy.bookseatapp.data.model

import androidx.annotation.Keep
@Keep
data class DataResult(
    val data: Any? = null,
    val error: String? = null
)
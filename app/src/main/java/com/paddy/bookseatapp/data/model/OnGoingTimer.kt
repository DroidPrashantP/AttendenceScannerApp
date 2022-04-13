package com.paddy.bookseatapp.data.model

import androidx.annotation.Keep

@Keep
data class OnGoingTimer(val hour: Long, val minute: Long, val seconds: Long)
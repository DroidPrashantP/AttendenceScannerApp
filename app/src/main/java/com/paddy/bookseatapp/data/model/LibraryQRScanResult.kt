package com.paddy.bookseatapp.data.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Keep
@Entity
data class LibraryQRScanResult(@PrimaryKey @SerializedName("location_id") val locationId: String,
                               @SerializedName("location_details") val locationDetails: String?,
                               @SerializedName("price_per_min") val pricePerMin: Float,
                               val sessionStatus: Boolean = false,
                               val startTime: Long = 0L,
                               val endTime: Long = 0L,
                               val totalPrice: Float = 0F,
                               val hour: Long = 0,
                               val minute: Long = 0,
                               val seconds: Long = 0)

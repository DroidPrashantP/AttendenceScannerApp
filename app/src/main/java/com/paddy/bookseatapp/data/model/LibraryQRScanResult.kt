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
                               var sessionStatus: Boolean = false,
                               var startTime: Long = 0L,
                               var endTime: Long = 0L,
                               var totalPrice: Float = 0F,
                               var hour: Long = 0,
                               var minute: Long = 0,
                               var seconds: Long = 0)

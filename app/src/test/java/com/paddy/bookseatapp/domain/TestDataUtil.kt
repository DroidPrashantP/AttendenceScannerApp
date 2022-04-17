package com.paddy.bookseatapp.domain

import com.paddy.bookseatapp.data.model.LibraryQRScanResult

object TestDataUtil {

    fun getSessionMockData(): LibraryQRScanResult {
        return LibraryQRScanResult(
            locationId = "ButterKnifeLib-1234",
            locationDetails = "ButterKnife Lib, 80 Feet Rd, Koramangala 1A Block, Bangalore",
            pricePerMin = 5.50F,
        )
    }

    fun dummyScanResult():String{
        return "{\"location_id\":\"ButterKnifeLib-1234\",\"location_details\":\"ButterKnife Lib, 80 Feet Rd, Koramangala 1A Block, Bangalore\",\"price_per_min\":5.50}"
    }
}
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
}
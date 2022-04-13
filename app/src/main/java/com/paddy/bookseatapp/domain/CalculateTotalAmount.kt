package com.paddy.bookseatapp.domain

import timber.log.Timber
import java.lang.Exception
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class CalculateTotalAmount @Inject constructor(private val libraryRepository: LibraryRepository) {

    suspend fun calculateTotalAmount() {
        try {
            val session = libraryRepository.getLibrarySession()
            val duration = (session.endTime - session.startTime).toBigDecimal().divide(60000.toBigDecimal(), 2, RoundingMode.HALF_UP)
            val amount =  BigDecimal(session.pricePerMin.toString()).multiply(BigDecimal(duration.toString()))
            libraryRepository.updateAmount(String.format("%.1f", amount).toFloat())
        } catch (ex : Exception){
            Timber.e("Exception caught in CalculateTotalAmount : ${ex}")
        }
    }
}
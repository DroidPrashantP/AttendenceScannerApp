package com.paddy.bookseatapp.domain

import com.google.common.truth.Truth
import com.paddy.bookseatapp.utils.priceFormatting
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.math.RoundingMode

class CalculateTotalAmountTest{

    @MockK
    private lateinit var libraryRepository : LibraryRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test // Positive test case
    fun testCalculateTotalAmount()  = runBlocking {
        val mockDuration = 4f
        val mockExpectedAmount = 22f
        val startTime = System.currentTimeMillis()
        val endTime = startTime + (4 * 60000)

        val mockSession = TestDataUtil.getSessionMockData().apply {
            this.startTime = startTime
            this.endTime = endTime

        }

        coEvery {
            libraryRepository.getLibrarySession()
        }.returns(mockSession)


        val session = libraryRepository.getLibrarySession()
        val duration = (session.endTime - session.startTime).toBigDecimal().divide(60000.toBigDecimal(), 2, RoundingMode.HALF_UP)

        Truth.assertThat(duration.priceFormatting().toFloat()).isEqualTo(mockDuration)

        val amount =  BigDecimal(session.pricePerMin.toString()).multiply(BigDecimal(duration.toString()))
        Truth.assertThat(amount.priceFormatting().toFloat()).isEqualTo(mockExpectedAmount)

        libraryRepository.updateAmount(amount.priceFormatting().toFloat())

        val roundOfAmount  = amount.priceFormatting().toFloat()
        coVerify(exactly = 1) { libraryRepository.getLibrarySession() }
        coVerify(exactly = 1) { libraryRepository.updateAmount(roundOfAmount) }

    }

    @Test
    fun testShouldFailedIfAmountFormatIncorrect()  = runBlocking {
        val mockExpectedAmount = 10
        val startTime = System.currentTimeMillis()
        val endTime = startTime + (4 * 60000)

        val mockSession = TestDataUtil.getSessionMockData().apply {
            this.startTime = startTime
            this.endTime = endTime

        }

        coEvery {
            libraryRepository.getLibrarySession()
        }.returns(mockSession)


        val session = libraryRepository.getLibrarySession()
        val duration = (session.endTime - session.startTime).toBigDecimal().divide(60000.toBigDecimal(), 2, RoundingMode.HALF_UP)

        val amount =  BigDecimal(session.pricePerMin.toString()).multiply(BigDecimal(duration.toString()))
        Truth.assertThat(amount.priceFormatting().toFloat()).isNotEqualTo(mockExpectedAmount)

        coVerify(exactly = 1) { libraryRepository.getLibrarySession() }
    }

    @Test
    fun testShouldFailedOnInvalidDuration()  = runBlocking {
        val mockDuration = 4f// minutes
        val startTime = System.currentTimeMillis()
        val endTime = startTime + (2 * 60000)

        val mockSession = TestDataUtil.getSessionMockData().apply {
            this.startTime = startTime
            this.endTime = endTime

        }

        coEvery {
            libraryRepository.getLibrarySession()
        }.returns(mockSession)


        val session = libraryRepository.getLibrarySession()
        val duration = (session.endTime - session.startTime).toBigDecimal().divide(60000.toBigDecimal(), 2, RoundingMode.HALF_UP)

        Truth.assertThat(duration.priceFormatting().toFloat()).isNotEqualTo(mockDuration)
    }
}
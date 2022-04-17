package com.paddy.bookseatapp.domain

import com.google.common.truth.Truth.assertThat
import com.paddy.bookseatapp.data.model.LibraryQRScanResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ScanResultValidTestCase {

    @MockK
    private lateinit var libraryDataParser : LibraryDataParser

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun scanResultShouldBeValid(){
        val dummyData = TestDataUtil.dummyScanResult()

        val expectedResult = LibraryQRScanResult(
            locationId = "ButterKnifeLib-1234",
            locationDetails = "ButterKnife Lib, 80 Feet Rd, Koramangala 1A Block, Bangalore",
            pricePerMin = 5.50f
        )

        coEvery { libraryDataParser.parseUnformattedJson(dummyData)}.returns(expectedResult)
        assertThat(libraryDataParser.parseUnformattedJson(dummyData)).isEqualTo(expectedResult)
        verify(exactly = 1) { libraryDataParser.parseUnformattedJson(dummyData) }

    }
}
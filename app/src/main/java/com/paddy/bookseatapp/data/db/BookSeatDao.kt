package com.paddy.bookseatapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.paddy.bookseatapp.data.model.LibraryQRScanResult
import kotlinx.coroutines.flow.Flow

@Dao
interface BookSeatDao {

    @Transaction
    suspend fun save(libraryQRScanResult: LibraryQRScanResult) {
        deleteSession()
        insert(libraryQRScanResult)
    }

    @Insert
    suspend fun insert(libraryQRScanResult: LibraryQRScanResult)

    @Query("SELECT * FROM LibraryQRScanResult")
    suspend fun getQrResult(): LibraryQRScanResult

    @Query("UPDATE LibraryQRScanResult SET hour=:hour,minute=:minute,seconds=:seconds WHERE locationId = :id")
    suspend fun updateTime(hour: Long, minute: Long, seconds: Long, id: String)

    @Query("UPDATE LibraryQRScanResult SET totalPrice=:amount WHERE locationId = :id")
    suspend fun updateAmount(amount: Float, id: String)

    @Query("SELECT * FROM LibraryQRScanResult")
    fun getUpdatedTime(): Flow<LibraryQRScanResult>

    @Query("SELECT sessionStatus FROM LibraryQRScanResult")
    fun getSessionStatus(): Flow<Boolean>

    @Query("UPDATE LibraryQRScanResult SET endTime=:endTime, sessionStatus=:active WHERE locationId = :id")
    suspend fun endSession(active: Boolean, endTime: Long, id: String)

    @Query("DELETE FROM LibraryQRScanResult")
    suspend fun deleteSession()
}
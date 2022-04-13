package com.paddy.bookseatapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.paddy.bookseatapp.data.model.LibraryQRScanResult

@Database(entities = [LibraryQRScanResult::class], version = 1)
abstract class BookSeatDatabase : RoomDatabase() {
    abstract fun bookSeatDao(): BookSeatDao
}
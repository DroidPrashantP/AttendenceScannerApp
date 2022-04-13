package com.paddy.bookseatapp

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.multidex.MultiDex
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BookSeatApplication : Application(), Configuration.Provider{

    @Inject
    lateinit var mHiltWorkerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun getWorkManagerConfiguration() : Configuration {
        return Configuration.Builder()
            .setWorkerFactory(mHiltWorkerFactory)
            .build()
    }

}
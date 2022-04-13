package com.paddy.bookseatapp.domain

import com.paddy.bookseatapp.utils.CommonUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountUpTimer @Inject constructor(private val repository: LibraryRepository) {

    private val scope = MainScope()
    private var job: Job? = null
    suspend fun start() {
        val startTime = System.currentTimeMillis()
        job = scope.launch {
            while (true) {
                val elapsedTime = CommonUtils.millisecondToStandard(System.currentTimeMillis() - startTime)
                repository.updateTime(elapsedTime)
                delay(INTERVAL)
            }
        }
    }

    fun stopTimer() {
        job?.cancel()
        job = null
    }

    companion object {
        const val INTERVAL = 1000L
    }
}
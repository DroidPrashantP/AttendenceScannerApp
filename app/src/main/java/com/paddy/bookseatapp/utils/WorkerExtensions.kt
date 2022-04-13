package com.paddy.bookseatapp.utils

import androidx.work.OneTimeWorkRequest

fun OneTimeWorkRequest.enableWorkerLogging(): OneTimeWorkRequest {
    CommonUtils.logWorkInfo(this.id)
    return this
}
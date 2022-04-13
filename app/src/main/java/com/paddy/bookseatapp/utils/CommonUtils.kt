package com.paddy.bookseatapp.utils

import android.os.Handler
import android.os.Looper
import androidx.work.WorkManager
import com.google.gson.Gson
import com.paddy.bookseatapp.data.model.LibraryQRScanResult
import com.paddy.bookseatapp.data.model.OnGoingTimer
import org.apache.commons.text.StringEscapeUtils
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object CommonUtils {

    fun parseUnformattedJson(unformattedJson: String): LibraryQRScanResult {
        val unQuotedString = unformattedJson.replace("^\"|\"$".toRegex(), "")
        val unescapeString = StringEscapeUtils.unescapeJava(unQuotedString)
        return Gson().fromJson(unescapeString, LibraryQRScanResult::class.java)
    }

    fun logWorkInfo(id: UUID) {
        val mainHandler = Handler(Looper.getMainLooper())

        val myRunnable =
            Runnable {
                WorkManager.getInstance().getWorkInfoByIdLiveData(id).observeForever { it ->
                    it?.let { data ->
                        Timber.v(
                            "<<<<<Worker State:%s tags=%s runAttemptCount=%d",
                            data.state.toString(),
                            data.tags.toString(),
                            data.runAttemptCount
                        )
                    }
                }
            }
        mainHandler.post(myRunnable)
    }

    fun millisecondToStandard(millis: Long): OnGoingTimer {
        val day = TimeUnit.MILLISECONDS.toDays(millis).toInt()
        val hours = TimeUnit.MILLISECONDS.toHours(millis) - day * 24
        val minute = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.MILLISECONDS.toHours(millis) * 60
        val second = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MILLISECONDS.toMinutes(millis) * 60

        return OnGoingTimer(hours, minute = minute, seconds = second)
    }

    fun convertMillisToFormattedTime(millis: Long): String {
        try {
            val dt1 = SimpleDateFormat("hh:mm aa", Locale.ENGLISH)
            val date = Date(millis)
            return dt1.format(date)
        } catch (e: Exception) {
            Timber.e("Exception caught in convertMillisToDate ==>> %s ", e.message)
        }
        return ""
    }
}
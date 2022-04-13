package com.paddy.bookseatapp.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.paddy.bookseatapp.R
import com.paddy.bookseatapp.domain.GetUpdatedTime
import com.paddy.bookseatapp.domain.LibrarySession
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.lang.Exception

@HiltWorker
class OnGoingNotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val librarySession : LibrarySession,
    private var getUpdatedTime : GetUpdatedTime
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val CHANNEL_NAME = "Active Session"
        const val CHANNEL_ID = "Book Library Seat"
        const val NOTIFICATION_ID = 1000
        const val NOTIFICATION_NAME = "Library Notification"
        const val MAIN_ACTIVITY = "com.paddy.bookseatapp.ui.activities.MainActivity"
    }

    private var mNotificationManager: NotificationManager? = null

    init {
        mNotificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override suspend fun doWork(): Result {
        try {
            if (librarySession.get().sessionStatus) {
                setForeground(buildForegroundNotification())
                getUpdatedTime.get().collect {
                    showNotificationTimer(
                        getSessionTimerText(it.hour, it.minute, it.seconds), mNotificationManager
                    )
                }
            }
        } catch (ex : Exception){
            Timber.e("Exception caught in StickyNotificationWorker#doWork() : ${ex.message}")
        }

        return Result.success()
    }

    private fun buildForegroundNotification(): ForegroundInfo {
        createNotificationChannel(mNotificationManager)
        return ForegroundInfo(NOTIFICATION_ID, NotificationCompat.Builder(applicationContext, CHANNEL_ID).apply {
            setContentTitle(CHANNEL_NAME)
            setSmallIcon(R.drawable.ic_launcher_background)
            setOngoing(true)
        }.build())
    }

    private fun showNotificationTimer(session: String, notificationManager: NotificationManager?) {
        val contentIntent = PendingIntent.getActivity(applicationContext,
            0, Intent(applicationContext, Class.forName(MAIN_ACTIVITY)), 0
        )
        notificationManager?.notify(NOTIFICATION_ID, NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(CHANNEL_NAME)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setOngoing(true)
            .setStyle(NotificationCompat.BigTextStyle())
            .setContentText(session)
            .setContentIntent(contentIntent)
            .build())
    }

    private fun createNotificationChannel(notificationManager: NotificationManager?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var notificationChannel = notificationManager?.getNotificationChannel(CHANNEL_ID)
            if (notificationChannel == null) {
                notificationChannel = NotificationChannel(
                    CHANNEL_ID, NOTIFICATION_NAME, NotificationManager.IMPORTANCE_HIGH
                )
                notificationManager?.createNotificationChannel(notificationChannel)
            }
        }
    }

    private fun getSessionTimerText(hour: Long = 0L, minute: Long = 0L, seconds: Long = 0L): String {
        return "Session :${hour}:${minute}:${seconds}"
    }
}
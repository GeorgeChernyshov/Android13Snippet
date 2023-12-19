package com.example.pre33.screen.privacy

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.pre33.Pre33Application
import com.example.pre33.R

class NotificationHelper(private val context: Context) {
    fun showNotification() {
        val builder = NotificationCompat
            .Builder(context, Pre33Application.NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString(R.string.notification_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOnlyAlertOnce(true)
            .setNumber(10)
            .setTimeoutAfter(10000)
            .setColor(context.resources.getColor(R.color.purple_200))

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }
    }

    companion object {
        var notificationId = 0
    }
}
package com.example.post33.screen.performance

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.post33.Post33Application.Companion.NOTIFICATION_CHANNEL
import com.example.post33.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SimpleForegroundService : Service() {

    private val binder = SimpleForegroundServiceBinder()

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = ActionValues.from(intent?.action)
        when (action) {
            ActionValues.START -> startService()
            ActionValues.STOP -> stopService()
            else -> {}
        }

        return START_NOT_STICKY
    }

    private fun startService() {
        startForeground(
            354,
            createSimpleNotification()
        )

        binder.setState(ServiceState.STARTED)
    }

    private fun stopService() {
        binder.setState(ServiceState.STOPPED)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createSimpleNotification(): Notification {
        val notificationBuilder = NotificationCompat
            .Builder(this, NOTIFICATION_CHANNEL)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_text))
            .setSmallIcon(R.drawable.ic_launcher_foreground)

        return notificationBuilder.build()
    }

    inner class SimpleForegroundServiceBinder: Binder() {
        private val _state = MutableStateFlow(ServiceState.STOPPED)
        val state = _state.asStateFlow()

        val coroutineScope = CoroutineScope(Dispatchers.IO)

        fun setState(state: ServiceState) = coroutineScope.launch {
            _state.emit(state)
        }
    }

    enum class ActionValues(val actionName: String) {
        START("start"),
        STOP("stop");

        companion object {
            fun from(actionName: String?): ActionValues? {
                return ActionValues.values()
                    .find { it.actionName == actionName }
            }
        }
    }

    enum class ServiceState {
        STARTED,
        STOPPED
    }
}
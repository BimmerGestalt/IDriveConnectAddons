package io.bimmergestalt.idriveconnectaddons.screenmirror

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import io.bimmergestalt.idriveconnectkit.android.IDriveConnectionStatus

/**
 * This ForegroundService is needed to maintain the MediaProjection permission
 * As soon as it closes, any previously-obtained permission becomes invalidated
 *
 * It needs to be started before prompting for permission to record the screen.
 * To enable the user to grant before plugging into the car, the notification
 * will keep running for a while before autoclosing if the car isn't connected
 */
class NotificationService: Service() {
    companion object {
        val PERMISSION_NOTIFICATION_ID = 53456      // dismissable permission prompt
        val ONGOING_NOTIFICATION_ID = 53457         // foreground ongoing notification
        val PERMISSION_CHANNEL_ID = "PermissionNotification"
        val ONGOING_CHANNEL_ID = "ConnectionNotification"
        val TIMEOUT = 60000L

        val INTENT_EXTRA_FOREGROUND = "foreground"

        /**
         * Whether notification requests should be foreground
         * Ongoing mirror status (waiting or active) need to be foreground, to keep the permission
         * Not Granted should be foreground if we are requesting permission
         *   but if it's just a reminder, should not be foreground
         */
        fun shouldBeForeground(): Boolean {
            val mirroringState = ScreenMirrorProvider.state.value
            return mirroringState == MirroringState.WAITING || mirroringState == MirroringState.ACTIVE
        }

        /** Start or update the current notification */
        fun startNotification(context: Context, foreground: Boolean) {
            val intent = Intent(context, NotificationService::class.java)
            intent.putExtra(INTENT_EXTRA_FOREGROUND, foreground)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && foreground) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
        /** Stop the notification, which will invalidate any Projection permission */
        fun stopNotification(context: Context) {
            context.stopService(Intent(context, NotificationService::class.java))
        }
    }

    val handler by lazy { Handler(Looper.getMainLooper()) }
    val notificationManager by lazy { getSystemService(NotificationManager::class.java) }

    /**
     * Called if we were launched while the car wasn't connected
     * If the car isn't connected after timeout, close the notification
     * If the car is connected, update to remove the autoremove comment
     */
    val autostop = Runnable {
        if (!IDriveConnectionStatus.isConnected || ScreenMirrorProvider.state.value == MirroringState.NOT_ALLOWED) {
            stopSelf()
        } else {
            startNotification(true)
        }
    }

    /** Subscribe to ScreenMirrorProvider.state to update the notification text */
    val stateObserver = Observer<MirroringState> {
        // redraw if the state changes
        if (shouldBeForeground()) {
            startNotification(true)
        }
    }

    override fun onCreate() {
        super.onCreate()
        ScreenMirrorProvider.state.observeForever(stateObserver)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        if (intent?.hasExtra(INTENT_EXTRA_FOREGROUND) == true) {
            val foreground = intent.getBooleanExtra(INTENT_EXTRA_FOREGROUND, false)
            startNotification(foreground)
            startTimeout()
        }
        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val requests = NotificationChannel(PERMISSION_CHANNEL_ID,
                getString(R.string.notification_requests_channel_name),
                NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(requests)
            val channel = NotificationChannel(ONGOING_CHANNEL_ID,
                getString(R.string.notification_ongoing_channel_name),
                NotificationManager.IMPORTANCE_MIN)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun startNotification(foreground: Boolean) {
        val requestActivityIntent = Intent(applicationContext, RequestActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val mainActivityIntent = Intent(applicationContext, MainActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val notificationBuilder = NotificationCompat.Builder(this, ONGOING_CHANNEL_ID)
            .setChannelId(ONGOING_CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setSmallIcon(R.drawable.ic_notify)
            .setContentTitle(getText(R.string.lbl_status_not_ready))
        if (foreground) {
            notificationBuilder.setOngoing(true)
        }

        // Either a foreground notification prompt for recording permission
        // or an in-app request to tap to open the prompt for recording permission
        if (ScreenMirrorProvider.state.value == MirroringState.NOT_ALLOWED) {
            notificationBuilder
                .setContentText(getText(R.string.btn_grant_mirror_auth))
                .setContentIntent(PendingIntent.getActivity(applicationContext, 0, requestActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE))
            if (!foreground) {
                // a background request to tap and start the grant flow
                @Suppress("DEPRECATION")
                notificationBuilder
                    .setChannelId(PERMISSION_CHANNEL_ID)
                    .setPriority(Notification.PRIORITY_LOW)
                    .setAutoCancel(true)
            }
        }
        if (ScreenMirrorProvider.state.value == MirroringState.WAITING) {
            notificationBuilder
                .setContentTitle(getText(R.string.lbl_status_waiting))
                .setContentIntent(PendingIntent.getActivity(applicationContext, 0, mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE))

            if (!IDriveConnectionStatus.isConnected) {
                notificationBuilder
                    .setStyle(NotificationCompat.BigTextStyle()
                        .bigText(getString(R.string.lbl_status_autoclose)))
            }
        }
        if (ScreenMirrorProvider.state.value == MirroringState.ACTIVE) {
            notificationBuilder
                .setContentTitle(getText(R.string.lbl_status_active))
                .setContentIntent(PendingIntent.getActivity(applicationContext, 0, mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE))
        }

        val notification = notificationBuilder.build()
        if (foreground) {
            notificationManager.cancel(PERMISSION_NOTIFICATION_ID)
            startForeground(ONGOING_NOTIFICATION_ID, notification)
        } else {
            notificationManager.notify(PERMISSION_NOTIFICATION_ID, notification)
            stopForeground(true)
        }
    }

    /**
     * Whenever we start up, set a timer to automatically close after a bit of time
     */
    private fun startTimeout() {
        handler.removeCallbacks(autostop)
        handler.postDelayed(autostop, TIMEOUT)
    }

    override fun onDestroy() {
        super.onDestroy()

        handler.removeCallbacks(autostop)
        ScreenMirrorProvider.state.removeObserver(stateObserver)
        // the notification timed out, or the ScreenMirrorProvider shut down
        // so clear the projection permission
        ScreenMirrorProvider.projection = null

    }
}
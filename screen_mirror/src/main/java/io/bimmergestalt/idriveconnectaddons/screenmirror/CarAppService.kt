package io.bimmergestalt.idriveconnectaddons.screenmirror

import android.app.Service
import android.app.UiModeManager
import android.content.Intent
import android.os.IBinder
import android.util.Log
import io.bimmergestalt.idriveconnectaddons.lib.CarCapabilities
import io.bimmergestalt.idriveconnectaddons.screenmirror.carapp.CarApp
import io.bimmergestalt.idriveconnectkit.android.CarAppAssetResources
import io.bimmergestalt.idriveconnectkit.android.IDriveConnectionReceiver
import io.bimmergestalt.idriveconnectkit.android.IDriveConnectionStatus
import io.bimmergestalt.idriveconnectkit.android.security.SecurityAccess

class CarAppService: Service() {
    var thread: CarThread? = null
    var app: CarApp? = null

    override fun onCreate() {
        super.onCreate()
        SecurityAccess.getInstance(applicationContext).connect()
    }

    /**
     * When a car is connected, it will bind the Addon Service
     */
    override fun onBind(intent: Intent?): IBinder? {
        intent ?: return null
        IDriveConnectionReceiver().onReceive(applicationContext, intent)
        startThread()
        return null
    }

    /**
     * If the thread crashes for any reason,
     * opening the main app will trigger a Start on the Addon Services
     * as a chance to reconnect
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent ?: return START_NOT_STICKY
        IDriveConnectionReceiver().onReceive(applicationContext, intent)
        startThread()
        return START_STICKY
    }

    /**
     * The car has disconnected, so forget the previous details
     */
    override fun onUnbind(intent: Intent?): Boolean {
        IDriveConnectionStatus.reset()
        return super.onUnbind(intent)
    }

    /**
     * Starts the thread for the car app, if it isn't running
     */
    fun startThread() {
        val iDriveConnectionStatus = IDriveConnectionReceiver()
        val securityAccess = SecurityAccess.getInstance(applicationContext)
        if (iDriveConnectionStatus.isConnected &&
            securityAccess.isConnected() &&
            thread?.isAlive != true) {

            L.loadResources(applicationContext)
            thread = CarThread("ScreenMirroring") {
                Log.i(TAG, "CarThread is ready, starting CarApp")
                val carCapabilities = CarCapabilities(applicationContext)
                val screenMirrorProvider = ScreenMirrorProvider(thread?.handler!!)
                if (iDriveConnectionStatus.port == 4007) {
                    // running over bluetooth, decimate image quality
                    screenMirrorProvider.jpgQuality = 30
                }
                app = CarApp(
                    iDriveConnectionStatus,
                    securityAccess,
                    CarAppAssetResources(applicationContext, "smartthings"),
                    AndroidResources(applicationContext),
                    carCapabilities,
                    applicationContext.getSystemService(UiModeManager::class.java),
                    screenMirrorProvider
                ) {
                    // start up the notification when we enter the app
                    val foreground = NotificationService.shouldBeForeground()
                    NotificationService.startNotification(applicationContext, foreground)
                }
            }
            thread?.start()
        } else if (thread?.isAlive != true) {
            if (thread?.isAlive != true) {
                Log.i(TAG, "Not connecting to car, because: iDriveConnectionStatus.isConnected=${iDriveConnectionStatus.isConnected} securityAccess.isConnected=${securityAccess.isConnected()}")
            } else {
                Log.d(TAG, "CarThread is still running, not trying to start it again")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        app?.onDestroy()
        NotificationService.stopNotification(applicationContext)
    }
}
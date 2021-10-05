package io.bimmergestalt.idriveconnectaddons.screenmirror

import android.app.Service
import android.app.UiModeManager
import android.content.Intent
import android.os.IBinder
import android.util.Log
import io.bimmergestalt.idriveconnectaddons.screenmirror.carapp.CarApp
import io.bimmergestalt.idriveconnectkit.android.IDriveConnectionReceiver
import io.bimmergestalt.idriveconnectkit.android.IDriveConnectionStatus
import io.bimmergestalt.idriveconnectkit.android.security.SecurityAccess

class CarAppService: Service() {
    var thread: CarThread? = null
    var app: CarApp? = null
    val focusRectDecorator by lazy { FocusRectDecorator(applicationContext) }

    override fun onCreate() {
        super.onCreate()
        SecurityAccess.getInstance(applicationContext).connect()
        focusRectDecorator.register()
    }

    /**
     * When a car is connected, it will bind the Addon Service
     */
    override fun onBind(intent: Intent?): IBinder? {
        intent ?: return null
        setConnection(intent)
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
        setConnection(intent)
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
     * Parses the connection intent and sets the connection details
     * todo: Move to IDriveConnectionListener
     */
    fun setConnection(intent: Intent) {
        val brand = intent.getStringExtra("EXTRA_BRAND")
        val host = intent.getStringExtra("EXTRA_HOST")
        val port = intent.getIntExtra("EXTRA_PORT", -1)
        val instanceId = intent.getIntExtra("EXTRA_INSTANCE_ID", -1)
        Log.i(TAG, "Received connection details: $brand at $host:$port($instanceId)")
        brand ?: return
        host ?: return
        if (port == -1) {
            return
        }
        IDriveConnectionStatus.setConnection(brand, host, port, instanceId)
        startThread()
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
                val screenMirrorProvider = ScreenMirrorProvider(thread?.handler!!) {
                    focusRectDecorator.decorate(it)
                }
                if (iDriveConnectionStatus.port == 4007) {
                    // running over bluetooth, decimate image quality
                    screenMirrorProvider.jpgQuality = 30
                }
                app = CarApp(
                    iDriveConnectionStatus,
                    securityAccess,
                    CarAppAssetResources(applicationContext, "smartthings"),
                    AndroidResources(applicationContext),
                    applicationContext.getSystemService(UiModeManager::class.java),
                    screenMirrorProvider,
                    MirroringAccessibilityInteraction(applicationContext)
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

        focusRectDecorator.unregister()
        app?.onDestroy()
        NotificationService.stopNotification(applicationContext)
    }
}
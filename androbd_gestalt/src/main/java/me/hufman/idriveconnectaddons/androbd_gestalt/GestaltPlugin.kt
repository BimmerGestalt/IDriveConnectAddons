package me.hufman.idriveconnectaddons.androbd_gestalt

import android.app.Service
import android.content.Intent
import android.os.IBinder

class GestaltPlugin: Service() {
    override fun onBind(intent: Intent?): IBinder? {
        MainModel.isGestaltConnected.value = true
        return null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        MainModel.isGestaltConnected.value = false
        return super.onUnbind(intent)
    }
}
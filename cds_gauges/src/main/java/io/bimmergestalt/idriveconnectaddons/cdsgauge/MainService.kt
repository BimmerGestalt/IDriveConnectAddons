package io.bimmergestalt.idriveconnectaddons.cdsgauge

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Needs a service intent to be discoverable in the Addons UI
 */
class MainService: Service() {
	override fun onBind(p0: Intent?): IBinder? {
		return null
	}
}
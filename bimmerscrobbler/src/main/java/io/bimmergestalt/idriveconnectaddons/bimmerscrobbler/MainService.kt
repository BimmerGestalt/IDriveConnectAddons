package io.bimmergestalt.idriveconnectaddons.bimmerscrobbler

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.Observer
import io.bimmergestalt.idriveconnectaddons.lib.CDSLiveData
import io.bimmergestalt.idriveconnectkit.CDSProperty

/**
 * Needs a service intent to be discoverable in the Addons UI
 */
class MainService: Service() {
	companion object {
		const val TAG = "BimmerScrobbler"
	}

	private val scrobbleAnnouncer by lazy { ScrobbleAnnouncer(this.applicationContext) }
	private val multimedia by lazy { CDSLiveData(this.contentResolver, CDSProperty.ENTERTAINMENT_MULTIMEDIA) }
	private val multimediaObserver by lazy { MultimediaObserver(scrobbleAnnouncer) }

	override fun onCreate() {
		super.onCreate()
		Log.i(TAG, "Starting service")
		MainModel.isConnected.value = true
		multimedia.observeForever(multimediaObserver)
	}

	override fun onBind(p0: Intent?): IBinder? {
		Log.i(TAG, "Binding service")
		return null
	}

	override fun onUnbind(intent: Intent?): Boolean {
		Log.i(TAG, "Unbinding service")
		return super.onUnbind(intent)
	}

	override fun onDestroy() {
		super.onDestroy()
		Log.i(TAG, "Destroying service")
		MainModel.isConnected.value = false
		multimedia.removeObserver(multimediaObserver)
	}
}

class MultimediaObserver(private val announcer: ScrobbleAnnouncer): Observer<Map<String, Any>> {
	override fun onChanged(value: Map<String, Any>) {
		val sourceId = value["source"] as? Int ?: 0
		val artist = value["artist"] as? String ?: ""
		val album = value["album"] as? String ?: ""
		val title = value["title"] as? String ?: ""
		MainModel.source.value = if (sourceId > 0) sourceId.toString() else ""
		MainModel.artist.value = artist
		MainModel.album.value = album
		MainModel.title.value = title
		announcer.announce(ScrobblerMetadata(artist, album, title, sourceId))
	}

}
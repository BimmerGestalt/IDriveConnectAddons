package io.bimmergestalt.idriveconnectaddons.bimmerscrobbler

import android.content.Context
import android.content.Intent
import android.util.Log
import io.bimmergestalt.idriveconnectaddons.bimmerscrobbler.MainService.Companion.TAG
import java.lang.Exception

class ScrobbleAnnouncer(val appContext: Context) {
    init {
        Log.i(TAG, "Simple Lastfm Scrobbler is installed: $hasSLS")
    }
    var previousMetadata = ScrobblerMetadata("", "", "", 0)

    fun announce(metadata: ScrobblerMetadata) {
        // if we finished the previous song
        val previousState = if (previousMetadata.source == metadata.source &&
                previousMetadata.title != metadata.title &&
                previousMetadata.title.isNotBlank()) {
            ScrobblerEventState.COMPLETE
        } else {
            null
        }
        // what the current scrobble state is
        val state = if (previousMetadata.source != metadata.source) {
            // switched music source, resume an existing song
            ScrobblerEventState.RESUME       // resume
        } else if (previousMetadata.source == metadata.source &&
                previousMetadata.title != metadata.title) {
            // new song
            ScrobblerEventState.START
        } else {
            // unknown
            ScrobblerEventState.RESUME
        }

        if (metadata.title.isNotBlank()) {
            Log.i(TAG, "Announcing scrobble of ${metadata.artist} - ${metadata.title}")
            if (hasSLS) {
                // if we finished a song
                if (previousState != null) {
                    announceSimpleLastFm(previousMetadata, previousState)
                }
                announceSimpleLastFm(metadata, state)
            } else {
                announceScrobbleDroid(metadata)
            }
        }
        previousMetadata = metadata
    }

    val hasSLS: Boolean
        get() = try {
            appContext.packageManager.getPackageInfo("com.adam.aslfms", 0)
            true
        } catch (e: Exception) {false}

    private fun announceScrobbleDroid(metadata: ScrobblerMetadata) {
        val intent = Intent("net.jjc1138.android.scrobbler.action.MUSIC_STATUS").apply {
            putExtra("playing", true)
            putExtra("source", "U")     // the car generally announces usb or bluetooth audio, but could also do radio, or the bluetooth may be a radio
            putExtra("artist", metadata.artist)
            putExtra("album", metadata.album)
            putExtra("track", metadata.title)
        }
        appContext.sendBroadcast(intent)
    }

    private fun announceSimpleLastFm(metadata: ScrobblerMetadata, state: ScrobblerEventState) {
        val intent = Intent("com.adam.aslfms.notify.playstatechanged").apply {
            setPackage("com.adam.aslfms")
            putExtra("app-name", "Bimmer Scrobbler")
            putExtra("app-package", appContext.packageName)
            putExtra("state", state.id)        // resumed
            putExtra("artist", metadata.artist)
            putExtra("album", metadata.album)
            putExtra("track", metadata.title)
            putExtra("duration", 180)       // unknown duration
            putExtra("source", "U")
        }
        appContext.sendBroadcast(intent)
    }
}

data class ScrobblerMetadata(val artist: String, val album: String, val title: String, val source: Int)

enum class ScrobblerEventState(val id: Int) {
    START(0),
    RESUME(1),
    PAUSE(2),
    COMPLETE(3)
}
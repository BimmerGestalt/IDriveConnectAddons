package me.hufman.idriveconnectaddons.lib

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.google.gson.JsonParser

/**
 * A LiveData that loads data from a Content Provider
 * Based on https://medium.com/@jmcassis/android-livedata-and-content-provider-updates-5f8fd3b2b3a4
 */
class CDSLiveData(
    private val context: Context,
    private val property: CDSProperty
): MutableLiveData<JsonObject>() {
    private lateinit var observer: ContentObserver

    val uri = Uri.parse("content://io.bimmergestalt.cardata.provider/cds/${property.ident}")

    override fun onActive() {
        observer = object : ContentObserver(null) {
            override fun onChange(self: Boolean) {
                // Notify LiveData listeners an event has happened
                postValue(getContentProviderValue())
            }
        }

        try {
            context.contentResolver.registerContentObserver(uri, true, observer)
            context.contentResolver.notifyChange(uri, null)     // kick off an initial query, to subscribe to CDS
        } catch (e: SecurityException) {
            // not allowed to view this value
            // or doesn't exist yet
        }
    }

    override fun onInactive() {
        try {
            context.contentResolver.unregisterContentObserver(observer)
        } catch (e: SecurityException) {
            // not allowed to view this value
            // or doesn't exist yet
        }
    }

    fun getContentProviderValue(): JsonObject? {
        // running on a background thread, use cursor synchronously
        val cursor = try {
            context.contentResolver.query(uri, null, null, null, null)
        } catch (e: SecurityException) {
            // not allowed to view this value
            // or doesn't exist yet
            null
        }
        cursor?.moveToFirst()
        val data = cursor?.getString(2)
        cursor?.close()
        data ?: return null
        return try {
            JsonParser.parseString(data) as? JsonObject
        } catch (e: Exception) {
            null
        }
    }
}
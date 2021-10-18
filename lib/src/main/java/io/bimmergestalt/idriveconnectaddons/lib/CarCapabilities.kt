package io.bimmergestalt.idriveconnectaddons.lib

import android.content.Context
import android.net.Uri

class CarCapabilities(private val context: Context) {
    private val uri = Uri.parse("content://io.bimmergestalt.cardata.provider/capabilities/")

    fun getCapabilities(): Map<String, String> {
        val cursor = try {
            context.contentResolver.query(uri, null, null, null, null)
        } catch (e: SecurityException) {
            // content provider doesn't exist
            null
        } catch (e: IllegalArgumentException) {
            // unknown parameter? probably from an older AAIdrive without this endpoint
            null
        } ?: return emptyMap()
        cursor.moveToFirst()
        val results = HashMap<String, String>()
        (0 until cursor.count).forEach { _ ->
            results[cursor.getString(0)] = cursor.getString(1)
            cursor.moveToNext()
        }
        cursor.close()
        return results
    }
}
package io.bimmergestalt.idriveconnectaddons.screenmirror

import android.content.Context

class AndroidResources(val context: Context) {
    fun getRaw(id: Int): ByteArray {
        return context.resources.openRawResource(id).readBytes()
    }
}
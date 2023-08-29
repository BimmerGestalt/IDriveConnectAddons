package io.bimmergestalt.idriveconnectaddons.screenmirror

import android.content.Context
import android.content.Intent

class MainController(val context: Context) {
    fun promptPermission(fromBackground: Boolean) {
        val intent = Intent(context, RequestActivity::class.java)
        if (fromBackground) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}
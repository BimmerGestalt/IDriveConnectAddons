package io.bimmergestalt.idriveconnectaddons.screenmirror

import android.content.Context
import android.content.Intent

class MainController(val context: Context) {
    fun promptPermission() {
        val intent = Intent(context, RequestActivity::class.java)
        context.startActivity(intent)
    }
}
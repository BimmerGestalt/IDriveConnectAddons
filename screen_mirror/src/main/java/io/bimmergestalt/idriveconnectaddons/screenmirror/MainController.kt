package io.bimmergestalt.idriveconnectaddons.screenmirror

import android.content.Context
import android.content.Intent
import android.provider.Settings

class MainController(val context: Context) {
    fun promptPermission() {
        val intent = Intent(context, RequestActivity::class.java)
        context.startActivity(intent)
    }

    fun manageInput() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
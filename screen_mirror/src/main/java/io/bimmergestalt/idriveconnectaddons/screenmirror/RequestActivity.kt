package io.bimmergestalt.idriveconnectaddons.screenmirror

import android.app.Activity
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle

class RequestActivity: Activity() {
    companion object {
        const val PROJECTION_PERMISSION_CODE = 8345
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotificationService.startNotification(applicationContext, true)
        requestPermission()
    }

    private fun requestPermission() {
        val projectionManager = getSystemService(MediaProjectionManager::class.java)
        startActivityForResult(projectionManager.createScreenCaptureIntent(), PROJECTION_PERMISSION_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val projectionManager = getSystemService(MediaProjectionManager::class.java)
        if (requestCode == PROJECTION_PERMISSION_CODE && resultCode == RESULT_OK && data != null) {
            ScreenMirrorProvider.projection = projectionManager.getMediaProjection(resultCode, data.clone() as Intent)
        } else {
            ScreenMirrorProvider.projection = null
            NotificationService.startNotification(applicationContext, false)
        }
        finish()
    }
}
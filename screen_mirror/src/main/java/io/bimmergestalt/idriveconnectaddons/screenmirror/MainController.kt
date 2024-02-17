package io.bimmergestalt.idriveconnectaddons.screenmirror

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat

class MainController(val activity: Activity) {

    companion object {
        const val REQUEST_POST_NOTIFICATIONS = 60
    }

    private fun tryOpenActivity(intent: Intent): Boolean {
        if (activity.packageManager.resolveActivity(intent, 0) != null) {
            try {
                activity.startActivity(intent)
                return true
            } catch (e: ActivityNotFoundException) {
            } catch (e: IllegalArgumentException) {}
        }
        return false
    }

    fun openApplicationPermissions(packageName: String) {
        run {
            val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.setClassName("com.miui.securitycenter",
                "com.miui.permcenter.permissions.PermissionsEditorActivity")
            intent.putExtra("extra_pkgname", packageName)
            if (tryOpenActivity(intent)) return
        }
        run {
            val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            // try an implicit intent without a classname
            intent.putExtra("extra_pkgname", packageName)
            if (tryOpenActivity(intent)) return
        }
        run {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.data = Uri.fromParts("package", packageName, null)
            if (tryOpenActivity(intent)) return
        }
        run {
            val intent = Intent(Settings.ACTION_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (tryOpenActivity(intent)) return
        }
    }

    fun promptMirroringPermission() {
        val intent = Intent(activity, RequestActivity::class.java)
        activity.startActivity(intent)
    }

    fun promptPostNotificationsPermission() {
        if (android.os.Build.VERSION.SDK_INT >= 33 && activity.applicationInfo.targetSdkVersion >= 33) {       // Android 13+
            ActivityCompat.requestPermissions(activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_POST_NOTIFICATIONS)
        } else {
            openApplicationPermissions(activity.packageName)
        }
    }

}
package me.hufman.idriveconnectaddons.androbd_gestalt

import android.util.Log
import com.fr3ts0n.androbd.plugin.PluginReceiver
import me.hufman.idriveconnectaddons.androbd_gestalt.AndrobdPlugin.Companion.TAG

class AndrobdPluginReceiver: PluginReceiver() {
    override fun getPluginClass(): Class<*> {
        Log.i(TAG, "Received AndrobdPlugin query")
        return AndrobdPlugin::class.java
    }
}
package io.bimmergestalt.idriveconnectaddons.androbd_gestalt

import android.content.*
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.Observer
import com.fr3ts0n.androbd.plugin.Plugin
import com.fr3ts0n.androbd.plugin.PluginInfo
import io.bimmergestalt.idriveconnectaddons.lib.CDSLiveData
import io.bimmergestalt.idriveconnectkit.CDSProperty
import io.bimmergestalt.idriveconnectkit.android.CDSLiveData

class AndrobdPlugin: Plugin(), Plugin.DataProvider, Plugin.ConfigurationHandler {
    companion object {
        const val TAG = "AndrobdBimmerGestalt"
    }

    private val pluginInfo = PluginInfo("AndrOBD Bimmer Gestalt",
    AndrobdPlugin::class.java, "AndrOBD Bimmer Gestalt bridge",
    "Copyright (C) 2021 Bimmer Gestalt", "MIT", "https://github.com/BimmerGestalt/IDriveConnectAddons")

    val receiver = AndrobdPluginReceiver()

    val bindListener = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i(TAG, "Connected to AndrOBD")
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i(TAG, "Disconnected from AndrOBD")
        }

    }

    val cdsListeners = mapOf(
        CDSProperty.DRIVING_ACCELERATORPEDAL to Observer<Map<String, Any>> {
            val value = it?.get("position") as? Number
            sendDataUpdate("driving.acceleratorPedal", value?.toString())
        },
        CDSProperty.DRIVING_BRAKECONTACT to Observer<Map<String, Any>> {
            val value = it?.get("brakeContact") as? Number
            sendDataUpdate("driving.brakeContact", value?.toString())
        },
        CDSProperty.DRIVING_CLUTCHPEDAL to Observer<Map<String, Any>> {
            val value = it?.get("position") as? Number
            sendDataUpdate("driving.clutchPedal", value?.toString())
        },
        CDSProperty.DRIVING_STEERINGWHEEL to Observer<Map<String, Any>> {
            val value = it?.get("angle") as? Number
            sendDataUpdate("driving.steeringWheel", value?.toString())
        },
        CDSProperty.DRIVING_ACCELERATION to Observer<Map<String, Any>> {
            val lat = (it?.get("lateral") as? Number)?.toDouble()?.div(9.80665)
            sendDataUpdate("driving.acceleration.lateral", lat?.toString())
            val long = (it?.get("longitudinal") as? Number)?.toDouble()?.div(9.80665)
            sendDataUpdate("driving.acceleration.longitudinal", long?.toString())
        },
        CDSProperty.DRIVING_GEAR to Observer<Map<String, Any>> {
            sendDataUpdate("driving.gear", (it?.get("gear") as? Int)?.toString())
        },
        CDSProperty.DRIVING_SPEEDACTUAL to Observer<Map<String, Any>> {
            sendDataUpdate("driving.speed", (it?.get("speedActual") as? Number)?.toString())
        },
        CDSProperty.ENGINE_CONSUMPTION to Observer<Map<String, Any>> {
            sendDataUpdate("engine.consumption", (it?.get("consumption") as? Number)?.toString())
        },
        CDSProperty.ENGINE_RPMSPEED to Observer<Map<String, Any>> {
            sendDataUpdate("engine.rpmSpeed", (it?.get("RPMSpeed") as? Number)?.toString())
        },
        CDSProperty.ENGINE_TORQUE to Observer<Map<String, Any>> {
            sendDataUpdate("engine.torque", (it?.get("torque") as? Number)?.toString())
        },
        CDSProperty.NAVIGATION_GPSPOSITION to Observer<Map<String, Any>> {
            val lat = it?.get("latitude") as? Number
            sendDataUpdate("navigation.gpsPosition.latitude", lat?.toString())
            val long = it?.get("longitude") as? Number
            sendDataUpdate("navigation.gpsPosition.longitude", long?.toString())
        },
        CDSProperty.NAVIGATION_GPSEXTENDEDINFO to Observer<Map<String, Any>> {
            val altitude = it?.get("altitude") as? Number
            if ((altitude?.toInt() ?: 65530) < 50000) {
                sendDataUpdate("navigation.gpsPosition.altitude", altitude?.toString())
            }
            val heading = it?.get("heading") as? Number
            sendDataUpdate("navigation.gpsPosition.heading", heading?.toString())
        },
    )
    val csvDataList = listOf(
        PvDefinition("driving.acceleratorPedal", 0.0, 100.0, "%"),
        PvDefinition("driving.brakeContact", 0.0, 100.0, "%"),
        PvDefinition("driving.clutchPedal", 0.0, 100.0, "%"),
        PvDefinition("driving.steeringWheel", -560.0, 560.0, "°"),
        PvDefinition("driving.acceleration.lateral", -10.0, 10.0, "g"),
        PvDefinition("driving.acceleration.longitudinal", -10.0, 10.0, "g"),
        PvDefinition("driving.gear", 0.0, 8.0, "."),
        PvDefinition("driving.speed", 0.0, 250.0, "kmph"),
        PvDefinition("engine.consumption", 0.0, 100.0, "."),
        PvDefinition("engine.rpmSpeed", 0.0, 9000.0, "RPM"),
        PvDefinition("engine.torque", -150.0, 500.0, "nm"),
        PvDefinition("navigation.gpsPosition.latitude", -360.0, 360.0, "°"),
        PvDefinition("navigation.gpsPosition.longitude", -360.0, 360.0, "°"),
        PvDefinition("navigation.gpsPosition.altitude", -360.0, 360.0, "°"),
        PvDefinition("navigation.gpsPosition.heading", -360.0, 360.0, "°"),
    ).joinToString("\n") {
        it.toCSV()
    }
    val cdsData = HashMap<CDSProperty, CDSLiveData>()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStart(intent: Intent?, startId: Int) {
        MainModel.isAndrobdConnected.value = true
        val filter = IntentFilter(IDENTIFY)
        filter.addCategory(REQUEST)
        registerReceiver(receiver, filter)
        sendDataList()

        // start listening to the car data
        cdsListeners.entries.forEach {
            if (!cdsData.containsKey(it.key)) {
                val liveData = CDSLiveData(applicationContext.contentResolver, it.key)
                liveData.observeForever(it.value)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MainModel.isAndrobdConnected.value = false
        try {
            unregisterReceiver(receiver)
        } catch (e: Exception) {}
        try {
            unbindService(bindListener)
        } catch (e: Exception) {}
    }

    override fun handleIdentify(context: Context?, intent: Intent?) {
        super.handleIdentify(context, intent)

//        headerSent = false
        sendDataList()
    }

    override fun getPluginInfo(): PluginInfo {
        return pluginInfo
    }

    fun sendDataList() {
        Log.i(TAG, "Sending Data List")
        super.sendDataList(csvDataList)
    }

    override fun performConfigure() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

}
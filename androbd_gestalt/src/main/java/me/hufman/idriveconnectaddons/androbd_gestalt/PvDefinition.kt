package me.hufman.idriveconnectaddons.androbd_gestalt

import me.hufman.idriveconnectaddons.lib.CDSProperty

data class PvDefinition(val name: String, val min: Double, val max: Double, val units: String) {
    fun toCSV(): String {
        return "$name;$name;$min;$max;$units"
    }
}

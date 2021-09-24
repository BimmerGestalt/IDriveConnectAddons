package io.bimmergestalt.idriveconnectaddons.androbd_gestalt

data class PvDefinition(val name: String, val min: Double, val max: Double, val units: String) {
    fun toCSV(): String {
        return "$name;$name;$min;$max;$units"
    }
}

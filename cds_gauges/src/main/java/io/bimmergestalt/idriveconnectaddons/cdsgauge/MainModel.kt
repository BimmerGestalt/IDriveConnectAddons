package io.bimmergestalt.idriveconnectaddons.cdsgauge

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.gson.JsonObject
import io.bimmergestalt.idriveconnectaddons.lib.CDS
import io.bimmergestalt.idriveconnectaddons.lib.CDSLiveData
import io.bimmergestalt.idriveconnectaddons.lib.CDSVehicleUnits
import io.bimmergestalt.idriveconnectaddons.lib.GsonNullable.tryAsDouble
import io.bimmergestalt.idriveconnectaddons.lib.GsonNullable.tryAsInt
import io.bimmergestalt.idriveconnectaddons.lib.GsonNullable.tryAsJsonObject
import io.bimmergestalt.idriveconnectaddons.lib.GsonNullable.tryAsJsonPrimitive
import io.bimmergestalt.idriveconnectaddons.lib.GsonNullable.tryAsString
import io.bimmergestalt.idriveconnectaddons.lib.LiveDataHelpers.combine
import io.bimmergestalt.idriveconnectaddons.lib.LiveDataHelpers.map

class MainModel(app: Application): AndroidViewModel(app) {
    val units: LiveData<CDSVehicleUnits> = CDSLiveData(app, CDS.VEHICLE.UNITS).map(CDSVehicleUnits.UNKNOWN) {
        CDSVehicleUnits.fromCdsProperty(it)
    }

    val unitsDistanceLabel: LiveData<Context.() -> String> = units.map({"km"}) {
        when (it.distanceUnits) {
            CDSVehicleUnits.Distance.Kilometers -> {{ "km" }}
            CDSVehicleUnits.Distance.Miles -> {{ "mi" }}
        }
    }
    val unitsSpeedLabel: LiveData<Context.() -> String> = units.map({"kmph"}) {
        when (it.distanceUnits) {
            CDSVehicleUnits.Distance.Kilometers -> {{ "kmph" }}
            CDSVehicleUnits.Distance.Miles -> {{ "mph" }}
        }
    }

    val vin: LiveData<String> = CDSLiveData(app, CDS.VEHICLE.VIN).map("") {
        it.tryAsJsonPrimitive("VIN")?.tryAsString
    }
    val speed: LiveData<Double> = CDSLiveData(app, CDS.DRIVING.SPEEDACTUAL).map(0.0) {
        it.tryAsJsonPrimitive("speedActual")?.tryAsInt
    }.combine(units) { value, units ->
        units.distanceUnits.fromCarUnit(value)
    }
    val torque: LiveData<Double> = CDSLiveData(app, CDS.ENGINE.TORQUE).map(0.0) {
        it.tryAsJsonPrimitive("torque")?.tryAsDouble
    }

    val temp: LiveData<JsonObject> = CDSLiveData(app, CDS.ENGINE.TEMPERATURE)
    val engineTemp = temp.map(0.0) {
        it.tryAsJsonObject("temperature")?.tryAsJsonPrimitive("engine")?.tryAsDouble
    }
    val oilTemp = temp.map(0.0) {
        it.tryAsJsonObject("temperature")?.tryAsJsonPrimitive("oil")?.tryAsDouble
    }
}
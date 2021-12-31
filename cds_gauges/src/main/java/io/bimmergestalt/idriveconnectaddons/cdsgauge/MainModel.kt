package io.bimmergestalt.idriveconnectaddons.cdsgauge

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.bimmergestalt.idriveconnectaddons.lib.CDSLiveData
import io.bimmergestalt.idriveconnectaddons.lib.CDSVehicleUnits
import io.bimmergestalt.idriveconnectaddons.lib.LiveDataHelpers.combine
import io.bimmergestalt.idriveconnectaddons.lib.LiveDataHelpers.map
import io.bimmergestalt.idriveconnectkit.CDS

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
        it["VIN"] as? String
    }
    val speed: LiveData<Double> = CDSLiveData(app, CDS.DRIVING.SPEEDACTUAL).map(0.0) {
        it["speedActual"] as? Number
    }.combine(units) { value, units ->
        units.distanceUnits.fromCarUnit(value)
    }
    val torque: LiveData<Double> = CDSLiveData(app, CDS.ENGINE.TORQUE).map(0.0) {
        it["torque"] as? Number
    }

    val temp: LiveData<Map<String, Any>> = CDSLiveData(app, CDS.ENGINE.TEMPERATURE)
    val engineTemp = temp.map(0.0) {
        it["engine"] as? Number
    }
    val oilTemp = temp.map(0.0) {
        it["oil"] as? Number
    }
}
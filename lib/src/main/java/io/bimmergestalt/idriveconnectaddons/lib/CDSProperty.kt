package io.bimmergestalt.idriveconnectaddons.lib

/**
 * The list of all known CDS Properties
 */
enum class CDSProperty(val ident: Int, val propertyName: String) {
    REPLAYING(0, "replaying"),
    CLIMATE_ACCOMPRESSOR(1, "climate.ACCompressor"),
    CLIMATE_ACMODE(3, "climate.ACMode"),
    CLIMATE_ACSYSTEMTEMPERATURES(4, "climate.ACSystemTemperatures"),
    CLIMATE_DRIVERSETTINGS(5, "climate.driverSettings"),
    CLIMATE_PASSENGERSETTINGS(6, "climate.passengerSettings"),
    CLIMATE_RESIDUALHEAT(7, "climate.residualHeat"),
    CLIMATE_SEATHEATDRIVER(8, "climate.seatHeatDriver"),
    CLIMATE_SEATHEATPASSENGER(9, "climate.seatHeatPassenger"),
    COMMUNICATION_CURRENTCALLINFO(10, "communication.currentCallInfo"),
    COMMUNICATION_LASTCALLINFO(11, "communication.lastCallInfo"),
    CONTROLS_CONVERTIBLETOP(12, "controls.convertibleTop"),
    CONTROLS_CRUISECONTROL(13, "controls.cruiseControl"),
    CONTROLS_DEFROSTREAR(15, "controls.defrostRear"),
    CONTROLS_HEADLIGHTS(16, "controls.headlights"),
    CONTROLS_LIGHTS(18, "controls.lights"),
    CONTROLS_STARTSTOPSTATUS(20, "controls.startStopStatus"),
    CONTROLS_SUNROOF(21, "controls.sunroof"),
    CONTROLS_TURNSIGNAL(22, "controls.turnSignal"),
    CONTROLS_WINDOWDRIVERFRONT(23, "controls.windowDriverFront"),
    CONTROLS_WINDOWDRIVERREAR(24, "controls.windowDriverRear"),
    CONTROLS_WINDOWPASSENGERFRONT(25, "controls.windowPassengerFront"),
    CONTROLS_WINDOWPASSENGERREAR(26, "controls.windowPassengerRear"),
    CONTROLS_WINDSHIELDWIPER(27, "controls.windshieldWiper"),
    DRIVING_ACCELERATION(28, "driving.acceleration"),
    DRIVING_ACCELERATORPEDAL(29, "driving.acceleratorPedal"),
    DRIVING_AVERAGECONSUMPTION(30, "driving.averageConsumption"),
    DRIVING_AVERAGESPEED(31, "driving.averageSpeed"),
    DRIVING_BRAKECONTACT(32, "driving.brakeContact"),
    DRIVING_CLUTCHPEDAL(34, "driving.clutchPedal"),
    DRIVING_DSCACTIVE(35, "driving.DSCActive"),
    DRIVING_ECOTIP(36, "driving.ecoTip"),
    DRIVING_GEAR(37, "driving.gear"),
    DRIVING_KEYPOSITION(38, "driving.keyPosition"),
    DRIVING_ODOMETER(39, "driving.odometer"),
    DRIVING_PARKINGBRAKE(40, "driving.parkingBrake"),
    DRIVING_SHIFTINDICATOR(41, "driving.shiftIndicator"),
    DRIVING_SPEEDACTUAL(42, "driving.speedActual"),
    DRIVING_SPEEDDISPLAYED(43, "driving.speedDisplayed"),
    DRIVING_STEERINGWHEEL(44, "driving.steeringWheel"),
    DRIVING_MODE(45, "driving.mode"),
    ENGINE_CONSUMPTION(46, "engine.consumption"),
    ENGINE_INFO(47, "engine.info"),
    ENGINE_RPMSPEED(50, "engine.RPMSpeed"),
    ENGINE_STATUS(51, "engine.status"),
    ENGINE_TEMPERATURE(52, "engine.temperature"),
    ENGINE_TORQUE(53, "engine.torque"),
    ENTERTAINMENT_MULTIMEDIA(54, "entertainment.multimedia"),
    ENTERTAINMENT_RADIOFREQUENCY(55, "entertainment.radioFrequency"),
    ENTERTAINMENT_RADIOSTATION(56, "entertainment.radioStation"),
    NAVIGATION_CURRENTPOSITIONDETAILEDINFO(57, "navigation.currentPositionDetailedInfo"),
    NAVIGATION_FINALDESTINATION(59, "navigation.finalDestination"),
    NAVIGATION_FINALDESTINATIONDETAILEDINFO(60, "navigation.finalDestinationDetailedInfo"),
    NAVIGATION_GPSEXTENDEDINFO(61, "navigation.GPSExtendedInfo"),
    NAVIGATION_GPSPOSITION(62, "navigation.GPSPosition"),
    NAVIGATION_GUIDANCESTATUS(63, "navigation.guidanceStatus"),
    NAVIGATION_INFOTONEXTDESTINATION(65, "navigation.infoToNextDestination"),
    NAVIGATION_NEXTDESTINATION(66, "navigation.nextDestination"),
    NAVIGATION_NEXTDESTINATIONDETAILEDINFO(67, "navigation.nextDestinationDetailedInfo"),
    SENSORS_BATTERY(68, "sensors.battery"),
    SENSORS_DOORS(70, "sensors.doors"),
    SENSORS_FUEL(71, "sensors.fuel"),
    SENSORS_PDCRANGEFRONT(72, "sensors.PDCRangeFront"),
    SENSORS_PDCRANGEREAR(73, "sensors.PDCRangeRear"),
    SENSORS_PDCSTATUS(74, "sensors.PDCStatus"),
    SENSORS_SEATOCCUPIEDPASSENGER(76, "sensors.seatOccupiedPassenger"),
    SENSORS_SEATBELT(77, "sensors.seatbelt"),
    SENSORS_TEMPERATUREEXTERIOR(78, "sensors.temperatureExterior"),
    SENSORS_TEMPERATUREINTERIOR(79, "sensors.temperatureInterior"),
    SENSORS_TRUNK(81, "sensors.trunk"),
    VEHICLE_COUNTRY(82, "vehicle.country"),
    VEHICLE_LANGUAGE(83, "vehicle.language"),
    VEHICLE_TYPE(84, "vehicle.type"),
    VEHICLE_UNITSPEED(85, "vehicle.unitSpeed"),
    VEHICLE_UNITS(86, "vehicle.units"),
    VEHICLE_VIN(87, "vehicle.VIN"),
    ENGINE_RANGECALC(88, "engine.rangeCalc"),
    ENGINE_ELECTRICVEHICLEMODE(89, "engine.electricVehicleMode"),
    DRIVING_SOCHOLDSTATE(90, "driving.SOCHoldState"),
    DRIVING_ELECTRICALPOWERDISTRIBUTION(91, "driving.electricalPowerDistribution"),
    DRIVING_DISPLAYRANGEELECTRICVEHICLE(92, "driving.displayRangeElectricVehicle"),
    SENSORS_SOCBATTERYHYBRID(93, "sensors.SOCBatteryHybrid"),
    SENSORS_BATTERYTEMP(94, "sensors.batteryTemp"),
    HMI_IDRIVE(95, "hmi.iDrive"),
    DRIVING_ECORANGEWON(96, "driving.ecoRangeWon"),
    CLIMATE_AIRCONDITIONERCOMPRESSOR(97, "climate.airConditionerCompressor"),
    CONTROLS_STARTSTOPLEDS(98, "controls.startStopLEDs"),
    DRIVING_ECORANGE(99, "driving.ecoRange"),
    DRIVING_FDRCONTROL(100, "driving.FDRControl"),
    DRIVING_KEYNUMBER(101, "driving.keyNumber"),
    NAVIGATION_INFOTOFINALDESTINATION(102, "navigation.infoToFinalDestination"),
    NAVIGATION_UNITS(103, "navigation.units"),
    SENSORS_LID(104, "sensors.lid"),
    SENSORS_SEATOCCUPIEDDRIVER(105, "sensors.seatOccupiedDriver"),
    SENSORS_SEATOCCUPIEDREARLEFT(106, "sensors.seatOccupiedRearLeft"),
    SENSORS_SEATOCCUPIEDREARRIGHT(107, "sensors.seatOccupiedRearRight"),
    VEHICLE_SYSTEMTIME(108, "vehicle.systemTime"),
    VEHICLE_TIME(109, "vehicle.time"),
    DRIVING_DRIVINGSTYLE(110, "driving.drivingStyle"),
    DRIVING_DISPLAYRANGEELECTRICVEHICLE_ALT(111, "driving.displayRangeElectricVehicle"),
    NAVIGATION_ROUTEELAPSEDINFO(112, "navigation.routeElapsedInfo"),
    HMI_TTS(113, "hmi.tts"),
    HMI_GRAPHICALCONTEXT(114, "hmi.graphicalContext"),
    SENSORS_PDCRANGEFRONT2(115, "sensors.PDCRangeFront2"),
    SENSORS_PDCRANGEREAR2(116, "sensors.PDCRangeRear2"),
    CDS_APIREGISTRY(117, "cds.apiRegistry"),
    API_CARCLOUD(118, "api.carcloud"),
    API_STARTJSAPP(119, "api.startJSApp");

    companion object {
        fun fromIdent(ident: Int?): CDSProperty? {
            return values().firstOrNull {
                it.ident == ident
            }
        }
        fun fromIdent(ident: String?): CDSProperty? {
            return values().firstOrNull {
                it.ident == ident?.toIntOrNull()
            }
        }
    }
}

/**
 * An organized tree of CDS properties
 */
object CDS {
    object API {
        val CARCLOUD = CDSProperty.API_CARCLOUD
        val STARTJSAPP = CDSProperty.API_STARTJSAPP
        val APIREGISTRY = CDSProperty.CDS_APIREGISTRY
    }
    object CLIMATE {
        val ACCOMPRESSOR = CDSProperty.CLIMATE_ACCOMPRESSOR
        val ACMODE = CDSProperty.CLIMATE_ACMODE
        val ACSYSTEMTEMPERATURES = CDSProperty.CLIMATE_ACSYSTEMTEMPERATURES
        val AIRCONDITIONERCOMPRESSOR = CDSProperty.CLIMATE_AIRCONDITIONERCOMPRESSOR
        val DRIVERSETTINGS = CDSProperty.CLIMATE_DRIVERSETTINGS
        val PASSENGERSETTINGS = CDSProperty.CLIMATE_PASSENGERSETTINGS
        val RESIDUALHEAT = CDSProperty.CLIMATE_RESIDUALHEAT
        val SEATHEATDRIVER = CDSProperty.CLIMATE_SEATHEATDRIVER
        val SEATHEATPASSENGER = CDSProperty.CLIMATE_SEATHEATPASSENGER
    }
    object COMMUNICATION {
        val CURRENTCALLINFO = CDSProperty.COMMUNICATION_CURRENTCALLINFO
        val LASTCALLINFO = CDSProperty.COMMUNICATION_LASTCALLINFO
    }
    object CONTROLS {
        val CONVERTIBLETOP = CDSProperty.CONTROLS_CONVERTIBLETOP
        val CRUISECONTROL = CDSProperty.CONTROLS_CRUISECONTROL
        val DEFROSTREAR = CDSProperty.CONTROLS_DEFROSTREAR
        val HEADLIGHTS = CDSProperty.CONTROLS_HEADLIGHTS
        val LIGHTS = CDSProperty.CONTROLS_LIGHTS
        val STARTSTOPLEDS = CDSProperty.CONTROLS_STARTSTOPLEDS
        val STARTSTOPSTATUS = CDSProperty.CONTROLS_STARTSTOPSTATUS
        val SUNROOF = CDSProperty.CONTROLS_SUNROOF
        val TURNSIGNAL = CDSProperty.CONTROLS_TURNSIGNAL
        val WINDOWDRIVERFRONT = CDSProperty.CONTROLS_WINDOWDRIVERFRONT
        val WINDOWDRIVERREAR = CDSProperty.CONTROLS_WINDOWDRIVERREAR
        val WINDOWPASSENGERFRONT = CDSProperty.CONTROLS_WINDOWPASSENGERFRONT
        val WINDOWPASSENGERREAR = CDSProperty.CONTROLS_WINDOWPASSENGERREAR
        val WINDSHIELDWIPER = CDSProperty.CONTROLS_WINDSHIELDWIPER
    }
    object DRIVING {
        val ACCELERATION = CDSProperty.DRIVING_ACCELERATION
        val ACCELERATORPEDAL = CDSProperty.DRIVING_ACCELERATORPEDAL
        val AVERAGECONSUMPTION = CDSProperty.DRIVING_AVERAGECONSUMPTION
        val AVERAGESPEED = CDSProperty.DRIVING_AVERAGESPEED
        val BRAKECONTACT = CDSProperty.DRIVING_BRAKECONTACT
        val CLUTCHPEDAL = CDSProperty.DRIVING_CLUTCHPEDAL
        val DISPLAYRANGEELECTRICVEHICLE = CDSProperty.DRIVING_DISPLAYRANGEELECTRICVEHICLE
        val DRIVINGSTYLE = CDSProperty.DRIVING_DRIVINGSTYLE
        val DSCACTIVE = CDSProperty.DRIVING_DSCACTIVE
        val ECORANGE = CDSProperty.DRIVING_ECORANGE
        val ECORANGEWON = CDSProperty.DRIVING_ECORANGEWON
        val ECOTIP = CDSProperty.DRIVING_ECOTIP
        val ELECTRICALPOWERDISTRIBUTION = CDSProperty.DRIVING_ELECTRICALPOWERDISTRIBUTION
        val FDRCONTROL = CDSProperty.DRIVING_FDRCONTROL
        val GEAR = CDSProperty.DRIVING_GEAR
        val KEYNUMBER = CDSProperty.DRIVING_KEYNUMBER
        val KEYPOSITION = CDSProperty.DRIVING_KEYPOSITION
        val MODE = CDSProperty.DRIVING_MODE
        val ODOMETER = CDSProperty.DRIVING_ODOMETER
        val PARKINGBRAKE = CDSProperty.DRIVING_PARKINGBRAKE
        val SHIFTINDICATOR = CDSProperty.DRIVING_SHIFTINDICATOR
        val SOCHOLDSTATE = CDSProperty.DRIVING_SOCHOLDSTATE
        val SPEEDACTUAL = CDSProperty.DRIVING_SPEEDACTUAL
        val SPEEDDISPLAYED = CDSProperty.DRIVING_SPEEDDISPLAYED
        val STEERINGWHEEL = CDSProperty.DRIVING_STEERINGWHEEL
    }
    object ENGINE {
        val CONSUMPTION = CDSProperty.ENGINE_CONSUMPTION
        val ELECTRICVEHICLEMODE = CDSProperty.ENGINE_ELECTRICVEHICLEMODE
        val INFO = CDSProperty.ENGINE_INFO
        val RANGECALC = CDSProperty.ENGINE_RANGECALC
        val RPMSPEED = CDSProperty.ENGINE_RPMSPEED
        val STATUS = CDSProperty.ENGINE_STATUS
        val TEMPERATURE = CDSProperty.ENGINE_TEMPERATURE
        val TORQUE = CDSProperty.ENGINE_TORQUE
    }
    object ENTERTAINMENT {
        val MULTIMEDIA = CDSProperty.ENTERTAINMENT_MULTIMEDIA
        val RADIOFREQUENCY = CDSProperty.ENTERTAINMENT_RADIOFREQUENCY
        val RADIOSTATION = CDSProperty.ENTERTAINMENT_RADIOSTATION
    }
    object HMI {
        val GRAPHICALCONTEXT = CDSProperty.HMI_GRAPHICALCONTEXT
        val IDRIVE = CDSProperty.HMI_IDRIVE
        val TTS = CDSProperty.HMI_TTS
    }
    object NAVIGATION {
        val CURRENTPOSITIONDETAILEDINFO = CDSProperty.NAVIGATION_CURRENTPOSITIONDETAILEDINFO
        val FINALDESTINATION = CDSProperty.NAVIGATION_FINALDESTINATION
        val FINALDESTINATIONDETAILEDINFO = CDSProperty.NAVIGATION_FINALDESTINATIONDETAILEDINFO
        val GPSEXTENDEDINFO = CDSProperty.NAVIGATION_GPSEXTENDEDINFO
        val GPSPOSITION = CDSProperty.NAVIGATION_GPSPOSITION
        val GUIDANCESTATUS = CDSProperty.NAVIGATION_GUIDANCESTATUS
        val INFOTOFINALDESTINATION = CDSProperty.NAVIGATION_INFOTOFINALDESTINATION
        val INFOTONEXTDESTINATION = CDSProperty.NAVIGATION_INFOTONEXTDESTINATION
        val NEXTDESTINATION = CDSProperty.NAVIGATION_NEXTDESTINATION
        val NEXTDESTINATIONDETAILEDINFO = CDSProperty.NAVIGATION_NEXTDESTINATIONDETAILEDINFO
        val ROUTEELAPSEDINFO = CDSProperty.NAVIGATION_ROUTEELAPSEDINFO
        val UNITS = CDSProperty.NAVIGATION_UNITS
    }
    object SENSORS {
        val BATTERY = CDSProperty.SENSORS_BATTERY
        val BATTERYTEMP = CDSProperty.SENSORS_BATTERYTEMP
        val DOORS = CDSProperty.SENSORS_DOORS
        val FUEL = CDSProperty.SENSORS_FUEL
        val LID = CDSProperty.SENSORS_LID
        val PDCRANGEFRONT = CDSProperty.SENSORS_PDCRANGEFRONT
        val PDCRANGEFRONT2 = CDSProperty.SENSORS_PDCRANGEFRONT2
        val PDCRANGEREAR = CDSProperty.SENSORS_PDCRANGEREAR
        val PDCRANGEREAR2 = CDSProperty.SENSORS_PDCRANGEREAR2
        val PDCSTATUS = CDSProperty.SENSORS_PDCSTATUS
        val SEATBELT = CDSProperty.SENSORS_SEATBELT
        val SEATOCCUPIEDDRIVER = CDSProperty.SENSORS_SEATOCCUPIEDDRIVER
        val SEATOCCUPIEDPASSENGER = CDSProperty.SENSORS_SEATOCCUPIEDPASSENGER
        val SEATOCCUPIEDREARLEFT = CDSProperty.SENSORS_SEATOCCUPIEDREARLEFT
        val SEATOCCUPIEDREARRIGHT = CDSProperty.SENSORS_SEATOCCUPIEDREARRIGHT
        val SOCBATTERYHYBRID = CDSProperty.SENSORS_SOCBATTERYHYBRID
        val TEMPERATUREEXTERIOR = CDSProperty.SENSORS_TEMPERATUREEXTERIOR
        val TEMPERATUREINTERIOR = CDSProperty.SENSORS_TEMPERATUREINTERIOR
        val TRUNK = CDSProperty.SENSORS_TRUNK
    }
    object VEHICLE {
        val COUNTRY = CDSProperty.VEHICLE_COUNTRY
        val LANGUAGE = CDSProperty.VEHICLE_LANGUAGE
        val SYSTEMTIME = CDSProperty.VEHICLE_SYSTEMTIME
        val TIME = CDSProperty.VEHICLE_TIME
        val TYPE = CDSProperty.VEHICLE_TYPE
        val UNITS = CDSProperty.VEHICLE_UNITS
        val UNITSPEED = CDSProperty.VEHICLE_UNITSPEED
        val VIN = CDSProperty.VEHICLE_VIN
    }
}
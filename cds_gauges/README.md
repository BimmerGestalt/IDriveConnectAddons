# CDS Gauges

This app loads some CDS data points over the CDS Content Provider and displays them in some gauge widgets.

This is intended to demonstrate the ease of querying the CDS Content Provider and to use data binding to show the result in the UI.

The magic happens in MainModel:
  - CDSLiveData is used to conveniently load data from the CDS Content Provider as a JsonObject
  - Some LiveData helpers are used to map from JsonObject to various numbers and unit labels
  - The CDSVehicleUnits object is parsed from the car to know what units the driver has chosen in the car
  - This CDSVehicleUnits is combined with the car's raw data to scale units appropriately

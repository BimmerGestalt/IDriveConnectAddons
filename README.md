# IDriveConnectAddons

The Connected Apps protocol is very powerful and provides an unparalleled convergence experience.
While the AAIdrive app focuses on a specific feature set, addons providing extra functionality have been [built and released](https://github.com/BimmerGestalt/IDriveConnectAddons/releases) and creative people could implement their own ideas to unlock other possibilities!

# Current Integrations

In recent AAIdrive builds, the CDS data is available through an Android Content Provider for any other apps to easily consume while the car is connected.
A [LiveData subclass](lib/src/main/java/me/hufman/idriveconnectaddons/lib/CDSLiveData.kt) has been implemented to enable incredibly convenient access to this data.

Built on top of this easy CDS access, a few example apps have been provided to inspire other apps:

- [androbd_gestalt](androbd_gestalt) provides CDS data to be viewed and recorded in [AndrOBD](https://github.com/fr3ts0n/AndrOBD)
- [bimmerscrobbler](bimmerscrobbler) watches the car's Multimedia information and announces any currently playing music to Simple Last.FM Scrobbler. Currently, this seems to provide the Bluetooth and USB music metadata, but unfortunately not Radio information.
- [cds_details](cds_details) provides a list of every single CDS data point provided by the car, with live-updating values.
- [cds_gauges](cds_gauges) provides a few needle gauges to represent a few select data points from the car.

There's also the concept of a Car Connection Addon, which starts up and shuts down together with the main car connection. These require implementing the full car app protocol and are much more complicated to develop, but can provide some incredible features:

- [screen_mirror](screen_mirror) adds an app to the car to show a copy of the phone screen on the car's screen.
- [Hass Gestalt](https://github.com/BimmerGestalt/HassGestalt) provides access to Home Assistant dashboards in the car's screen
- [ReadYou Gestalt](https://github.com/BimmerGestalt/ReadYou) adds an RSS news app to the car's screen, based on [ReadYou](https://f-droid.org/packages/me.ash.reader/)

# Future Ideas

## Car Data Service

The car provides [so much data](https://bimmergestalt.github.io/BMWConnectedAnalysis/cds/), what are possible ways to use it?

- Trip logger, both locally and to the cloud in real-time
- Fuel logger, automatically reporting fill-ups to Fuelly or Spritmonitor
- Scrobbling of car-sourced music such as SiriusXM or USB (basic implementation in [bimmerscrobbler](bimmerscrobbler))
- Live gauges display of some basic performance data, without needing extra hardware (simple example in [cds_gauges](cds_gauges))
- Car race recording, inspired by BMW M Laptimer (one attempt with [androbd_gestalt](androbd_gestalt))
- Automatically load VIN from the car to decode the list of options that the car came with

## Legacy Apps

There were previously some incredibly innovative features using the Connected Apps protocol, perhaps they can be brought back and updated?

- Official Twitter client could be reused for Mastodon
- Official RSS News reader (implemented as a [fork of ReadYou](https://github.com/BimmerGestalt/ReadYou))
- Dynamic Music would be fun
- Sports Displays could be reused for accelerometer display, perhaps?
- Smart Things could be a frontend for Home Assistant (implemented as [Hass Gestalt](https://github.com/BimmerGestalt/HassGestalt))
- The GoPro app inspires a dashboard-integrated dashcam control/view interface, perhaps with support for real GoPros
- Perhaps replicate the new IDrive Weather map for cars that don't include it
- Bring OnlineSearch functionality (navigation, calling) to all cars, without relying on the car's own data service

## AM App Icons

The car provides a [basic API for adding placeholder icons](https://bimmergestalt.github.io/BMWConnectedAnalysis/am/), allowing an icon with any image and label to be placed in a variety of top-level menus.
This icon is limited to only triggering a callback when selected, holding no other UI. Combined with the ability to change the name and image at will, this could be creatively used for quick functionality:

- High-level direct access to toggle devices in a smart home
- Trigger specific Tasker or Triggr actions from the car

## Customizable apps

By using AM App Icons as launchers into other apps, we can essentially create custom apps to provide richer experiences:

- An overview of upcoming traffic obstacles or a way to report them, using blitzer.de for example
- An app to view and add to-do task list items, perhaps as voice memos, integrating into an existing tasklist app somehow

## Unknown Possibilities

The protocol has some other APIs which haven't been fully explored, such as the Map api that takes a KMZ file. This might enable some fun features:

- Add a POI layer to show speed traps (blitzer.de Flitsmeister Yanosik, for example). OSM has some of [this data](https://wiki.openstreetmap.org/wiki/Relation:enforcement).
- Add a POI layer to show addresses of contacts from the phone's address book

Turns out this map functionality is less of a POI layer and more like a mini map showing search results, which lends itself to different functionality:

- Reimplement the parking/gas(charging) search functionality from BMW Connected
- Viewing Redfin/Zillow prices of houses, or nearby open houses to tour

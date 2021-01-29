# IDriveConnectAddons

The Connected Apps protocol is very powerful and provides an unparalleled convergence experience.
Here are some ideas for new apps using this functionality.

## Car Data Service

The car provides [so much data](https://hufman.github.io/BMWConnectedAnalysis/cds/), what are good ways to show it to the user?

- Trip logger, both locally and to the cloud in real-time
- Scrobbling of car-sourced music such as SiriusXM or USB
- Live gauges display of some basic performance data, without needing extra hardware

## Legacy Apps

There used to be some incredibly innovative features using the Connected Apps protocol, perhaps they can be brought back and updated?

- Official Twitter client could be reused for Mastodon
- Perhaps integrated with the official RSS News reader
- Dynamic Music would be fun
- Sports Displays could be reused for accelerometer display, perhaps?
- Smart Things could be a frontend for Home Assistant
- The GoPro app inspires a dashboard-integrated dashcam control/view interface, perhaps with support for real GoPros
- Perhaps replicate the new IDrive Weather map for cars that don't include it

## AM App Icons

The car provides a [basic API for adding placeholder icons](https://hufman.github.io/BMWConnectedAnalysis/am/), allowing an icon with any image and label to be placed in a variety of top-level menus. This icon is limited to only triggering a callback when selected, holding no other UI. Combined with the ability to change the name and image at will, this could be creatively used for quick functionality:

- High-level direct access to toggle devices in a smart home
- Trigger specific Tasker or Triggr actions from the car

## Customizable apps

By using AM App Icons as launchers into other apps, we can essentially create custom apps to provide richer experiences:

- An overview of upcoming traffic obstacles or a way to report them, using blitzer.de for example

## Unknown Possibilities

The protocol has some other APIs which haven't been fully explored, such as the Map api that takes a KMZ file. This might enable some fun features:

- Add a POI layer to show speed traps (blitzer.de, for example)
- Add a POI layer to show addresses of contacts from the phone's address book

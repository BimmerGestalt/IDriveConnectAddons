Car Resources
=============

The car requires that all Connected Apps authenticate with BMW-signed certificate files.
Some components of this project need copies of these official certificate files to log in to the car.
These certificates and other resource files can be found in official Connected Apps, and every compatible Connected App will have a `assets/carapplications` directory inside it's APK File.
This project's build script automatically extracts the needed files from these official APKs during compilation.

Generally, the exact version of the APK doesn't matter, as long as it contains the necessary resources.
The main challenge is that some apps have removed their car compatibility, and so specific older versions need to be found.

Please place copies of the following Android APKs in this `external` directory:
  - [SmartThings Classic](https://apkpure.com/smartthings-classic/com.smartthings.android/download/211001-APK?from=versions%2Fversion) v2.3.1 or earlier

After placing these files in this `external` directory, re-run the build process and it should complete successfully.
The build process should automatically extract the necessary files to the proper directories.

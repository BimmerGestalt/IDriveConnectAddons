#!/bin/bash
set -x

[ -e 'external/SmartThings_Classic_v2.1.6_apkpure.com.apk' ] ||
wget --quiet -P external 'https://bimmergestalt.s3.amazonaws.com/aaidrive/external/SmartThings_Classic_v2.1.6_apkpure.com.apk'
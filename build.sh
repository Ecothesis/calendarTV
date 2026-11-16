#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
: "${JAVA_HOME:?Set JAVA_HOME to JDK 17 or newer}"
: "${ANDROID_JAR:?Set ANDROID_JAR to your SDK android.jar}"
: "${BUILD_TOOLS:?Set BUILD_TOOLS to Android SDK build-tools directory}"
export PATH="$JAVA_HOME/bin:$PATH"
mkdir -p build/classes build/dex
"$BUILD_TOOLS/aapt" package -f -M AndroidManifest.xml -S res -I "$ANDROID_JAR" -F build/resources.apk
javac -source 8 -target 8 -bootclasspath "$ANDROID_JAR" -d build/classes src/com/weekview/tv/MainActivity.java
jar cf build/classes.jar -C build/classes .
"$BUILD_TOOLS/d8" --lib "$ANDROID_JAR" --min-api 23 --output build/dex build/classes.jar
cp build/resources.apk build/unsigned.apk
(cd build/dex && zip -q -j ../unsigned.apk classes.dex)
"$BUILD_TOOLS/zipalign" -f -p 4 build/unsigned.apk build/aligned.apk
if [ ! -f build/test-signing.jks ]; then
  keytool -genkeypair -keystore build/test-signing.jks -storepass android -keypass android -alias tv-test -keyalg RSA -keysize 2048 -validity 3650 -dname 'CN=Week on TV Test Build'
fi
"$BUILD_TOOLS/apksigner" sign --ks build/test-signing.jks --ks-pass pass:android --out build/week-on-tv-0.1-demo.apk build/aligned.apk
"$BUILD_TOOLS/apksigner" verify --verbose build/week-on-tv-0.1-demo.apk

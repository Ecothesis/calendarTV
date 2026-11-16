Week on TV — test APK 0.1

INSTALL
1. Transfer week-on-tv-0.1-demo.apk to your Android TV or Google TV, using a USB drive or a file-transfer app.
2. Open it with a file manager on the TV.
3. If prompted, allow that file manager to install unknown apps, then install.
4. Open Week on TV from the TV apps list.
Requires Android 6.0 or later and an Android TV / Google TV device.

REMOTE
Arrow keys: select a day and hour; the view scrolls through all 24 hours.
OK: open sample event details or the selected time.
Menu: focus Previous / Today / Next / About controls.
If your remote has no Menu button, press Up until you pass midnight to reach those controls.
Down from week controls: return to the calendar.
Left / right past the first / last day: move to the adjacent week.
Back: close a dialog, leave week controls, or exit the app.

WHAT IS INCLUDED
Seven-day week view, sample events, event details, week navigation, current-day highlighting, a current-time line, and a date label refreshed every 30 seconds while the app is open.
The TV home-screen icon is static. Android TV launchers do not provide a standard dynamic-date icon mechanism for this app.

LIMITATIONS
This is a sample-data prototype, not the finished Google Calendar app.
No Google login or live calendar synchronization is included. These require a registered Google OAuth project and an appropriate authorization flow.
The app requests no permissions and makes no network calls.
Built and APK signatures verified here; not run on a physical TV or Android emulator. Please test installation and remote navigation on your TV.
This APK uses a development signing key and is not a Play Store release.

SOURCE AND REBUILD
The source archive contains native Java Android sources and build.sh; no third-party app libraries.
Install JDK 17+, Android SDK platform 35, Android build-tools 35.0.0 and the zip utility.
Set JAVA_HOME to your JDK, ANDROID_JAR to platforms/android-35/android.jar and BUILD_TOOLS to build-tools/35.0.0, then run bash build.sh.
The script generates a local test-signing key under build/. Preserve it if you want your own subsequent APKs to update your installed build. The distributed source archive excludes signing keys.

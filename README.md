# Week on TV

Android TV / Google TV week-view calendar prototype.

## Download and install

[Download the test APK](https://github.com/Ecothesis/calendarTV/releases/latest/download/week-on-tv-0.1-demo.apk)

Transfer the APK to your TV, open it with a file manager and allow installation from that source when prompted. Requires Android TV / Google TV running Android 6.0 or newer.

See [installation instructions](downloads/INSTALL.txt) for remote controls and rebuilding.

## Included

- Seven-day calendar with sample events and event details.
- Remote control navigation, scrolling through all 24 hours and week switching.
- Today highlighting, current-time indicator and an automatically refreshed date inside the app.
- Static TV launcher icon and banner.

## Prototype status

**This version uses sample events. Google sign-in and live Calendar synchronization are not included.** They require a registered Google OAuth project and a supported authorization flow. This app makes no network requests and requests no permissions.

The APK was compiled, aligned and its signatures verified. It has not yet been run on a physical TV or an emulator. It uses a development signing key and is not a Play Store release.

Android TV does not offer a standard dynamic-date launcher icon mechanism for third-party apps; the in-app date refreshes automatically instead.

## Source

Native Java Android app with no third-party app libraries. See [README.txt](README.txt) for build instructions. Signing keys are excluded from this repository.

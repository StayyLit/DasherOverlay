# Dasher Overlay (Personal Decision Support)

## What it does
- Floating overlay displaying:
  - $/mile
  - estimated $/hour
  - estimated total time
- Uses Android Accessibility (opt-in) to read on-screen offer text.
- No automation of taps. On-device processing only.

## Setup
1) Install dependencies in Termux (optional, for wrapper jar download):
- pkg install git curl zip openjdk-17

2) Download wrapper jar:
- bash ./fetch_wrapper.sh

3) Open project in Android Studio (recommended) OR set up Android SDK in Termux to build.

4) On phone:
- Grant overlay permission
- Enable Accessibility service
- Start overlay in app

## Build (needs Android SDK)
- ./gradlew :app:assembleDebug
- ./gradlew :app:bundleRelease  (for Play, requires signing setup)

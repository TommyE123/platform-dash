# Quickstart: Commute Departures Widget

## Prerequisites
- Android Studio / command-line Android SDK configured
- JDK 17
- Emulator or physical Android device with launcher widget support
- Build tooling aligned to Android Gradle Plugin (AGP) 9.2.1

## Build and run
1. From repository root, build the app:
   - Android Studio: Build > Make Project
   - If Gradle wrapper scripts are present: run assembleDebug from the wrapper script for your shell
2. Install and launch on a device/emulator:
   - Android Studio: Run app on device/emulator
   - If Gradle wrapper scripts are present: run installDebug from the wrapper script for your shell
3. Add Platform Dash widget from the launcher widget picker.

## Verify functional behavior
1. Header displays normalized route as LBG -> RDH by default.
2. Widget shows 3 departures on small/medium heights and 5 on large heights.
3. Empty scenario shows No departures available text.
4. CANCELLED rows display in error red.
5. ON_TIME rows display in success green.
6. Theme mode changes in app settings reflect in widget colors.
7. Widget supports horizontal and vertical resize and recalculates scale.

## Mock scenario notes
- Active data source is MockTrainRepository.
- Non-default routes currently return empty lists.
- Deterministic fetchedAt and service fixtures are expected.

## Deferred in this increment
- Live API integration
- WorkManager refresh scheduling and stale-data UI treatment
- Persistent route + key save flow completion

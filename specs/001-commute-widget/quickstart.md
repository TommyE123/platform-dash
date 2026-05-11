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
5. ON_TIME and DELAYED rows display in contrast theme text color for the active mode.
6. Light mode displays white background with outer 6dp navy (#071D49) and inner 3dp red (#E60000) borders, with navy text.
7. Dark mode displays navy (#071D49) background with outer 6dp white and inner 3dp red (#E60000) borders, with white text.
8. Root widget border rendering uses nested border/padding modifiers and both border strokes are visible.
9. Theme changes update the widget colors immediately without closing and reopening the app.
10. App settings show GBR navy/red buttons with yellow 3dp borders and white/navy page backgrounds by mode.
11. Widget supports horizontal and vertical resize and recalculates scale.

## Mock scenario notes
- Active data source is MockTrainRepository.
- Non-default routes currently return empty lists.
- Deterministic fetchedAt and service fixtures are expected.

## Deferred in this increment
- Live API integration
- WorkManager refresh scheduling and stale-data UI treatment
- Persistent route + key save flow completion

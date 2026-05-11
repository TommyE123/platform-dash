# Implementation Plan: Commute Departures Widget

**Branch**: `001-lbg-rdh-glance-widget` | **Date**: 2026-05-10 | **Spec**: `/specs/001-commute-widget/spec.md`
**Input**: Feature specification from `/specs/001-commute-widget/spec.md`

## Summary

Implement and refine the Android Jetpack Glance commute widget for configurable routes using MockTrainRepository, with exact-size responsive row limits, shared theme support, explicit status styling rules, and AGP 9.2.1 build baseline alignment.

## Technical Context

**Language/Version**: Kotlin (Kotlin Compose plugin 2.3.21), Java 17 toolchain
**Build Tooling**: Android Gradle Plugin (AGP) 9.2.1
**Primary Dependencies**: AndroidX Glance AppWidget 1.1.1, Jetpack Compose BOM 2025.09.01, Material 3, AndroidX Activity Compose, Lifecycle Runtime KTX
**Storage**: SharedPreferences for theme preference (current); no API key persistence in this feature scope
**Testing**: JUnit4 (unit), AndroidX instrumentation JUnit, Espresso, Compose UI test APIs (test suites to be added)
**Target Platform**: Android app widget and companion app, minSdk 26, targetSdk/compileSdk 36
**Project Type**: Android mobile app with home screen widget
**Performance Goals**: Widget render remains glanceable with deterministic mock data and displays within standard launcher widget redraw expectations; no blocking network calls in widget render path
**Constraints**: SizeMode.Exact rendering, deterministic mock-only data source, no live API calls, no hardcoded secrets, resize support horizontal and vertical, maintain readable status contrast in light/dark themes, AGP pinned to 9.2.1 for this feature scope
**Scale/Scope**: Single widget provider and one app module, route header plus up to 5 services per render, three active mock scenarios

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

### Pre-Research Gate Review

- `Architecture Gate`: PASS. Widget remains Jetpack Glance-based and app UI remains Compose plus Material 3.
- `Security Gate`: PASS. Feature does not introduce key handling and keeps hardcoded secrets disallowed.
- `Commute UX Gate`: PASS WITH DEFERRED ITEMS. This plan covers default LBG->RDH and cancellation/on-time emphasis; configurable stations and swap-direction behavior remain tracked follow-on work.
- `Data Contract Gate`: PASS. TrainRepository and MockTrainRepository are the active contract boundary.
- `Refresh Gate`: PASS WITH DEFERRED IMPLEMENTATION. This feature keeps `updatePeriodMillis=0` and deterministic mock behavior; WorkManager refresh and stale-state behavior are captured as follow-on requirements.
- `History Gate`: PASS. Plan retains gitmoji.dev commit convention requirement.

### Post-Design Gate Review

- `Architecture Gate`: PASS. Data model and contracts preserve Glance plus Compose plus Material 3 alignment.
- `Security Gate`: PASS. No secret material added; BYOK/DataStore path remains documented as out-of-scope for this feature increment.
- `Commute UX Gate`: PASS WITH TRACKED GAP. Contracts include default-route behavior and status emphasis; swap/configurable-route interactions remain task-level follow-ups.
- `Data Contract Gate`: PASS. Contract artifact specifies repository behavior and mock scenario determinism.
- `Refresh Gate`: PASS WITH TRACKED GAP. Quickstart and research document deferred WorkManager delivery and degraded-mode expectation.
- `History Gate`: PASS. No contradiction with commit policy.

## Project Structure

### Documentation (this feature)

```text
specs/001-commute-widget/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── widget-rendering-contract.md
└── tasks.md
```

### Source Code (repository root)

```text
app/
├── build.gradle.kts
└── src/
    └── main/
        ├── AndroidManifest.xml
        ├── java/com/platformdash/
        │   ├── data/
        │   │   └── MockTrainRepository.kt
        │   ├── domain/
        │   │   ├── Departures.kt
        │   │   ├── Route.kt
        │   │   └── TrainRepository.kt
        │   ├── settings/
        │   │   ├── ThemeMode.kt
        │   │   └── ThemePreferences.kt
        │   └── widget/
        │       ├── CommuteWidget.kt
        │       └── CommuteWidgetReceiver.kt
        └── res/
            └── xml/commute_widget_info.xml
```

**Structure Decision**: Single Android app module structure is retained. Feature work is localized to domain, data, settings, and widget packages in `app/src/main/java/com/platformdash`, with planning artifacts under `specs/001-commute-widget`.

## Complexity Tracking

No constitution violations requiring formal justification.

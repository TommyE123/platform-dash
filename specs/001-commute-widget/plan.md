# Implementation Plan: Commute Departures Widget

**Branch**: `001-lbg-rdh-glance-widget` | **Date**: 2026-05-11 | **Spec**: `/specs/001-commute-widget/spec.md`
**Input**: Feature specification from `/specs/001-commute-widget/spec.md`

## Summary

Consolidate widget visual behavior to strict fixed GBR colors with Light/Dark-only theme selection, enforce the double-border rendering using nested `border()` and `padding()` on the root Glance Column, and align typography so all non-cancelled text uses contrast theme color while Cancelled stays GBR Red.

## Technical Context

**Language/Version**: Kotlin (Compose plugin 2.3.21), Java 17
**Build Tooling**: Android Gradle Plugin 9.2.1
**Primary Dependencies**: AndroidX Glance AppWidget 1.1.1, Compose BOM 2025.09.01, Material 3, AndroidX Activity Compose, Lifecycle Runtime KTX
**Storage**: SharedPreferences for current theme mode persistence
**Testing**: JUnit4, AndroidX instrumentation tests, Compose UI test APIs
**Target Platform**: Android app widget + companion app, minSdk 26, targetSdk/compileSdk 36
**Project Type**: Android mobile app with Glance home-screen widget
**Performance Goals**: Maintain readable glanceable rendering with deterministic mock data and no blocking work in render path
**Constraints**: No System mode toggle, strict fixed GBR palette (`#071D49`, `#E60000`, `#FFFFFF`), visible outer 2dp + inner 1dp borders, SizeMode.Exact behavior
**Scale/Scope**: Single widget provider, three mock scenarios, 3/5 rows depending on widget height

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- `Architecture Gate`: Plan uses Jetpack Glance for widget surfaces and
  Jetpack Compose + Material 3 for app UI.
- `Security Gate`: Plan enforces BYOK with user-entered API key flow and
  disallows hardcoded secrets in any artifact.
- `Commute UX Gate`: Plan covers default LBG->RDH route, configurable stations,
  widget swap-direction action, and cancellation emphasis in red.
- `Data Contract Gate`: Plan defines TrainRepository and MockTrainRepository
  prior to live UK rail API integration.
- `Refresh Gate`: Plan defines WorkManager-based background refresh behavior,
  including degraded-mode handling.
- `History Gate`: Plan acknowledges gitmoji.dev commit convention for all
  implementation commits.

Gate outcome: PASS with existing deferred items (swap-direction action, refresh scheduling, and secure key flow) tracked outside this strict visual alignment increment.

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
└── src/main/
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
    │       └── CommuteWidget.kt
    └── res/
        ├── values/colors.xml
        └── xml/commute_widget_info.xml
```

**Structure Decision**: Keep the existing single-module Android structure and localize drift fixes to `widget`, `settings`, and `res/values` assets plus feature documentation artifacts.

## Phase 0 Research Output

- Fixed official GBR colors are the only allowed palette values in this feature:
  - GBR Navy `#071D49`
  - GBR Red `#E60000`
  - GBR White `#FFFFFF`
- System theme toggle is explicitly removed from requirements.
- Border visibility depends on nested `border()` and `padding()` chain on the root Column.
- Typography uses contrast color per mode, except Cancelled which remains GBR Red.

## Phase 1 Design Output

- Contract and model artifacts must reflect exact border widths (outer 2dp, inner 1dp) and mode-specific colors.
- Quickstart verification must include explicit border visibility checks in Light and Dark modes.
- Tasks must remove any System theme or ON_TIME-green assumptions.

## Phase 2 Status

`/speckit.tasks` is executed after this plan refresh to regenerate `specs/001-commute-widget/tasks.md` and remove implementation drift.

## Complexity Tracking

No constitution violations requiring formal complexity justification for this visual-alignment increment.

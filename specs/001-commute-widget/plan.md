# Implementation Plan: Commute Departures Widget

**Branch**: `001-lbg-rdh-glance-widget` | **Date**: 2026-05-11 | **Spec**: `specs/001-commute-widget/spec.md`
**Input**: Feature specification from `specs/001-commute-widget/spec.md`

## Summary

Implement the commute widget and companion settings UI to use fixed GBR styling with Light/Dark-only behavior, a thicker dual-border treatment in the widget, and immediate palette updates when the theme changes. Keep deterministic mock departures and the AGP 9.2.1 baseline intact.

## Technical Context

**Language/Version**: Kotlin (Compose plugin 2.3.21), Java 17
**Build Tooling**: Android Gradle Plugin 9.2.1
**Primary Dependencies**: AndroidX Glance AppWidget 1.1.1, Compose BOM 2025.09.01, Material 3, AndroidX Activity Compose, Lifecycle Runtime KTX
**Storage**: SharedPreferences for current theme persistence; API key handling is not part of this visual-alignment increment
**Testing**: JUnit4, AndroidX instrumentation tests, Compose UI test APIs
**Target Platform**: Android app widget plus companion app, minSdk 26, targetSdk/compileSdk 36
**Project Type**: Android mobile app with Glance home-screen widget
**Performance Goals**: Maintain glanceable rendering with deterministic mock data and immediate theme-to-widget refresh without app restart
**Constraints**: No System theme toggle, fixed GBR palette only, widget uses exact size mode, dual border uses visible nested treatment at 3x the previous thickness baseline, app controls use yellow border accents
**Scale/Scope**: Single widget provider, one settings screen, three mock scenarios, 3/5 row rendering based on widget height

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

Gate outcome: PASS with tracked gaps for follow-on work outside this visual/styling refresh increment.

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
    │   │   ├── AppTheme.kt
    │   │   ├── SettingsHostScreen.kt
    │   │   ├── ThemeMode.kt
    │   │   └── ThemePreferences.kt
    │   └── widget/
    │       ├── CommuteWidget.kt
    │       └── CommuteWidgetReceiver.kt
    └── res/
        ├── values/colors.xml
        └── xml/commute_widget_info.xml
```

**Structure Decision**: Keep the single-module Android app structure and localize all widget/theme work to `app/src/main/java/com/platformdash` plus shared resource files.

## Phase 0 Research Output

- Use fixed GBR colors only in the widget and app UI.
- Light and Dark are the only supported theme modes.
- Widget border visibility requires a nested treatment so both border strokes are visible at 6dp outer and 3dp inner thickness.
- Theme changes must refresh the widget immediately without requiring a close/reopen cycle.

## Phase 1 Design Output

- `specs/001-commute-widget/data-model.md` should mirror the fixed GBR palette and mode-specific contrast colors.
- `specs/001-commute-widget/contracts/widget-rendering-contract.md` should define widget/app styling and border widths in mode-specific terms, including the thicker widget border treatment.
- `specs/001-commute-widget/quickstart.md` should verify immediate theme refresh and both border modes.

## Phase 2 Task Generation Status

`/speckit.tasks` is the next step after this plan refresh so the task list reflects the updated styling and refresh requirements.

## Complexity Tracking

No formal complexity exceptions captured for this increment.

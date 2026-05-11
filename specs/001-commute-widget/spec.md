# Feature Specification: Commute Departures Widget

**Feature Branch**: 001-commute-widget
**Updated**: 2026-05-10
**Status**: In Progress

## Scope

Build and iterate an Android Jetpack Glance commute widget that can display departures for any configured origin and destination route, using MockTrainRepository as the active data source during current development, with build tooling standardized on Android Gradle Plugin (AGP) 9.2.1.

## Technical Baseline

- TB-001: Feature development and build configuration for this scope MUST target Android Gradle Plugin (AGP) 9.2.1.

## Functional Requirements

- FR-001: Widget MUST render route header for the currently selected origin -> destination route.
- FR-002: Widget MUST display departures based on widget height breakpoint (3 for Small/Medium, 5 for Large).
- FR-003: Widget MUST use MockTrainRepository and MUST NOT call a live train API in current phase.
- FR-004: Cancelled services MUST be highlighted in red using the error color.
- FR-005: Widget MUST support light/dark behavior from shared theme preference (System, Light, Dark).
- FR-006: Widget MUST use SizeMode.Exact for rendering behavior.
- FR-007: On-time services MUST be highlighted in green using the success color.

## Visual Requirements

- VR-001 Surface: Widget surface uses a white background in light mode and dark surface in dark mode via shared theme handling.
- VR-002 Header Typography: Route header is bold, 14sp.
- VR-003 Departure Typography: Departure lines are medium weight, 14sp.
- VR-004 Spacing:
  - Outer padding: 12dp
  - Header-to-list spacing: 8dp
  - Row spacing: 6dp
- VR-005 Cancellation Emphasis: Cancelled rows are rendered in error red.
- VR-006 Empty State: If no departures exist, widget shows "No departures available" in body text style.
- VR-007 On-time Emphasis: On-time rows are rendered in success green.

## Resizing Logic

- RL-001 Widget uses Glance SizeMode.Exact.
- RL-002 AppWidget provider metadata allows horizontal and vertical resize for launcher placement and uses target cell hints.

## Responsive Scaling Rules

- Small (< 100dp height): Header 12sp, Times 14sp. (Compact view)
- Medium (100dp - 200dp height): Header 14sp, Times 18sp. (Standard view)
- Large (> 200dp height): Header 18sp, Times 24sp, show 5 trains instead of 3. (Expanded view)

## Data Source Behavior

- DS-001 MockTrainRepository returns deterministic services with mixed status (on-time, delayed, cancelled) for active mock scenarios.
- DS-002 Non-default routes currently return an empty services list.
- DS-003 Service count selection is responsive: top 3 services for Small/Medium and top 5 services for Large based on height breakpoint.

## Current Active Mock Profile

- AMP-001 Active mock scenario in code is currently LBG -> RDH (MockTrainRepository.Scenario.LBG_TO_RDH_DEFAULT).
- AMP-002 Additional active mock scenarios currently defined are LBG_TO_RDH_NO_SERVICES and LBG_TO_RDH_DELAY_HEAVY.

## Out Of Scope (Current Phase)

- Live UK rail API integration.
- Real-time background refresh scheduling.
- Persistent route and key save flow completion.

## Acceptance Checks

- AC-001 Header shows selected origin -> destination at 14sp bold.
- AC-002 Departure count follows breakpoints: 3 in Small/Medium views, 5 in Large view.
- AC-003 Cancelled service text appears in error red.
- AC-004 Switching app theme mode updates app and widget visuals.
- AC-005 Widget behavior remains deterministic when using mock scenarios.
- AC-006 On-time service text appears in success green.
- AC-007 Project build baseline for this feature remains on AGP 9.2.1.

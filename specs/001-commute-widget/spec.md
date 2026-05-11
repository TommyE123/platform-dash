# Feature Specification: Commute Departures Widget

**Feature Branch**: 001-commute-widget
**Updated**: 2026-05-11
**Status**: In Progress

## Scope

Build and iterate an Android Jetpack Glance commute widget that can display departures for any configured origin and destination route, using MockTrainRepository as the active data source during current development, with build tooling standardized on Android Gradle Plugin (AGP) 9.2.1.

## Technical Baseline

- TB-001: Feature development and build configuration for this scope MUST target Android Gradle Plugin (AGP) 9.2.1.

## Functional Requirements

- FR-001: Widget MUST render route header for the currently selected origin -> destination route.
- FR-002: Widget MUST display departures based on widget height breakpoint (3 for Small/Medium, 5 for Large).
- FR-003: Widget MUST use MockTrainRepository and MUST NOT call a live train API in current phase.
- FR-004: Cancelled services MUST be highlighted in GBR Red (#E60000).
- FR-005: Widget MUST support only Light and Dark modes and MUST NOT expose any System theme toggle.
- FR-006: Widget MUST use SizeMode.Exact for rendering behavior.
- FR-007: Root widget container MUST render the dual-border treatment via nested border and padding modifiers at 3x the previous border thickness baseline.
- FR-008: All text MUST use the contrast theme color for the active mode, except Cancelled status text which MUST use GBR Red (#E60000).
- FR-009: App settings UI MUST use GBR color styling with navy/red primary buttons, white/navy page background by mode, and yellow border treatment.
- FR-010: App border accents MUST use yellow and MUST be rendered at 3x the previous border thickness baseline.
- FR-011: Changing app theme mode MUST refresh widget colors immediately without requiring app restart.

## Visual Requirements

- VR-001 Surface Light: Widget surface uses GBR White (#FFFFFF) background in Light mode.
- VR-001b Surface Dark: Widget surface uses GBR Navy (#071D49) background in Dark mode.
- VR-002 Header Typography: Route header is bold, 14sp.
- VR-003 Departure Typography: Departure lines are medium weight, 14sp.
- VR-004 Spacing:
  - Outer padding: 12dp
  - Header-to-list spacing: 8dp
  - Row spacing: 6dp
- VR-005 Cancellation Emphasis: Cancelled rows are rendered in GBR Red (#E60000).
- VR-006 Empty State: If no departures exist, widget shows "No departures available" in body text style.
- VR-007 Border Styling Light: Light mode border is double-stroked using outer 6dp GBR Navy (#071D49) then inner 3dp GBR Red (#E60000).
- VR-008 Border Styling Dark: Dark mode border is double-stroked using outer 6dp GBR White (#FFFFFF) then inner 3dp GBR Red (#E60000).
- VR-009 Border Implementation Rule: Root Column MUST apply nested border and padding modifiers in sequence so both border strokes remain visible.
- VR-010 Text Colors Light: Text in light mode uses GBR Navy (#071D49) for primary readability over white background.
- VR-011 Text Colors Dark: Text in dark mode uses GBR White (#FFFFFF) for primary readability over navy background.
- VR-012 App Control Colors: Theme controls and primary actions use GBR Navy or GBR Red fill with yellow border accents.
- VR-013 App Surface Colors: App settings background is white in Light mode and navy in Dark mode.
- VR-014 App Border Width: Yellow app border accents use 3dp width.

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
- AC-003 Cancelled service text appears in GBR Red (#E60000).
- AC-004 Widget displays correctly in both Light (white background with 6dp navy outer and 3dp red inner border, navy text) and Dark (navy background with 6dp white outer and 3dp red inner border, white text) modes.
- AC-005 Widget behavior remains deterministic when using mock scenarios.
- AC-006 Non-cancelled text appears in contrast theme color for the active mode.
- AC-007 Project build baseline for this feature remains on AGP 9.2.1.
- AC-008 App settings UI displays GBR styling with navy/red buttons, yellow borders, and mode-correct white/navy background.
- AC-009 Yellow app border accents render at 3dp width.
- AC-010 Changing Light/Dark mode updates the widget color palette immediately without closing and reopening the app.

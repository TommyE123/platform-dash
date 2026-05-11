# Widget Rendering Contract: Commute Departures Widget

## Scope
Defines the behavioral contract between widget rendering logic and repository output for feature 001-commute-widget.

## Inputs
- Route: originCode, destinationCode
- Departures aggregate from TrainRepository.getDepartures(route)
- ThemeMode preference: SYSTEM | LIGHT | DARK
- Widget size height for scale selection

## Repository contract
- Interface: TrainRepository
- Method: suspend getDepartures(route: Route): Departures
- Current implementation: MockTrainRepository only
- Determinism:
  - Default route LBG->RDH returns fixture services
  - Non-default route returns empty services list
  - fetchedAt uses fixed fixture timestamp

## Rendering contract
1. Header
- MUST show normalized route in format ORIGIN -> DEST.
- MUST use bold typography and scale by height breakpoint.

2. Service count
- MUST render top 3 rows when height <= 200dp.
- MUST render top 5 rows when height > 200dp.

3. Status mapping
- ON_TIME -> text label On time and success green text color.
- DELAYED -> text label Delayed and default text color.
- CANCELLED -> text label Cancelled and error red text color.

4. Empty state
- If services list is empty, MUST display No departures available.

5. Theme behavior
- SYSTEM mode uses configured widget background/text resources.
- LIGHT mode uses light background with dark text.
- DARK mode uses dark background with light text.

6. Sizing behavior
- MUST use Glance SizeMode.Exact.
- Widget metadata MUST allow horizontal and vertical resize.

## Non-contractual notes (deferred)
- Live API integration is intentionally out-of-scope.
- WorkManager scheduling and stale-data messaging are deferred.

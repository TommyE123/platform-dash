# Widget Rendering Contract: Commute Departures Widget

## Scope
Defines the behavioral contract between widget rendering logic and repository output for feature 001-commute-widget.

## Inputs
- Route: originCode, destinationCode
- Departures aggregate from TrainRepository.getDepartures(route)
- ThemeMode preference: LIGHT | DARK
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
- ON_TIME -> text label On time and contrast theme text color.
- DELAYED -> text label Delayed and contrast theme text color.
- CANCELLED -> text label Cancelled and GBR Red (#E60000) text color.

4. Empty state
- If services list is empty, MUST display No departures available.

5. Theme behavior
- LIGHT mode uses white background with GBR Navy (#071D49) text.
- DARK mode uses GBR Navy (#071D49) background with white text.

6. App UI styling
- Theme controls and primary actions use GBR Navy or GBR Red fill with yellow border accents.
- Yellow app border accents use 3dp width.

7. Theme refresh behavior
- Theme changes MUST refresh the widget colors immediately without requiring app restart.

8. Sizing behavior
- MUST use Glance SizeMode.Exact.
- Widget metadata MUST allow horizontal and vertical resize.

9. Widget border thickness
- Light mode uses outer 6dp GBR Navy (#071D49) and inner 3dp GBR Red (#E60000).
- Dark mode uses outer 6dp GBR White (#FFFFFF) and inner 3dp GBR Red (#E60000).

## Non-contractual notes (deferred)
- Live API integration is intentionally out-of-scope.
- WorkManager scheduling and stale-data messaging are deferred.

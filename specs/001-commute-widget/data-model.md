# Data Model: Commute Departures Widget

## Entity: Route
- Purpose: Identify the journey pair displayed by the widget.
- Fields:
  - originCode: String (required, station code)
  - destinationCode: String (required, station code)
  - normalizedOrigin: String (derived uppercase/trimmed)
  - normalizedDestination: String (derived uppercase/trimmed)
- Validation rules:
  - originCode and destinationCode must be non-blank after trim.
  - Normalized codes use uppercase to keep repository lookups deterministic.
- Relationships:
  - One Route has many Departure records through Departures.services.
- State/behavior:
  - swap(): creates a new Route with origin and destination reversed.
  - DefaultCommute is LBG -> RDH.

## Entity: DepartureStatus
- Purpose: Classify service state for UI rendering and emphasis.
- Values:
  - ON_TIME
  - DELAYED
  - CANCELLED
- Validation rules:
  - Must be one of the enumerated statuses.
- UI contract implications:
  - ON_TIME must render in success green.
  - CANCELLED must render in error red.
  - DELAYED currently uses standard text color unless changed by future spec.

## Entity: Departure
- Purpose: Represent one train service row shown in widget list.
- Fields:
  - serviceId: String (required, unique within scenario)
  - destinationCode: String (required)
  - scheduledDepartureIso: String (required ISO timestamp)
  - expectedDepartureIso: String (required ISO timestamp)
  - platform: String? (nullable)
  - status: DepartureStatus (required)
- Validation rules:
  - expectedDepartureIso and scheduledDepartureIso must be parseable ISO datetime strings.
  - platform may be null for unknown platform.
- Relationships:
  - Belongs to one Route via Departures aggregate.

## Entity: Departures
- Purpose: Aggregate fetch output from repository for one route snapshot.
- Fields:
  - route: Route (required)
  - fetchedAt: Instant (required)
  - services: List<Departure> (required, can be empty)
- Validation rules:
  - services list may be empty and should trigger empty-state UI.
- Relationships:
  - One Departures aggregate contains 0..n Departure items.

## Entity: ThemeMode
- Purpose: Drive widget color scheme selection.
- Values:
  - SYSTEM
  - LIGHT
  - DARK
- Validation rules:
  - Unknown persisted values fall back to SYSTEM.

## Derived View Model: WidgetScaleConfig
- Purpose: Determine responsive typography and max service count by widget height.
- Fields:
  - headerFontSize
  - timeFontSize
  - maxTrains
- Rules:
  - height < 100dp => maxTrains 3
  - 100dp <= height <= 200dp => maxTrains 3
  - height > 200dp => maxTrains 5

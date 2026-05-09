# Contract: Train Departure Data Schema

**Feature**: 002-train-repository
**Date**: 2026-05-09
**Purpose**: Formal specification of the TrainDeparture data structure and JSON serialization format

---

## Kotlin Data Class

See `data-model.md` for authoritative Kotlin definitions.

```kotlin
data class TrainDeparture(
    val aimedDepartureTime: OffsetDateTime,
    val expectedDepartureTime: OffsetDateTime,
    val platform: String,
    val destination: String,
    val status: DepartureStatus,
    val notes: String? = null
)

enum class DepartureStatus {
    ON_TIME,
    DELAYED,
    CANCELLED
}
```

---

## JSON Schema (Phase 2+, for API contracts)

When TrainDeparture is serialized to JSON (for REST APIs or data export), use this schema:

```json
{
  "type": "object",
  "title": "TrainDeparture",
  "properties": {
    "aimedDepartureTime": {
      "type": "string",
      "format": "date-time",
      "description": "Scheduled departure time in ISO 8601 format (RFC 3339)"
    },
    "expectedDepartureTime": {
      "type": "string",
      "format": "date-time",
      "description": "Real-time expected departure in ISO 8601 format"
    },
    "platform": {
      "type": "string",
      "minLength": 1,
      "maxLength": 10,
      "description": "Platform identifier, e.g., '3', 'B', 'North'"
    },
    "destination": {
      "type": "string",
      "minLength": 1,
      "description": "Destination station name or code, e.g., 'Redhill', 'London Bridge'"
    },
    "status": {
      "type": "string",
      "enum": ["ON_TIME", "DELAYED", "CANCELLED"],
      "description": "Current operational status"
    },
    "notes": {
      "type": "string",
      "maxLength": 200,
      "nullable": true,
      "description": "Optional notes, e.g., 'Platform change to 5', 'Cancelled'"
    }
  },
  "required": ["aimedDepartureTime", "expectedDepartureTime", "platform", "destination", "status"]
}
```

### Example JSON Instances

#### On-Time Departure

```json
{
  "aimedDepartureTime": "2026-05-09T14:32:00Z",
  "expectedDepartureTime": "2026-05-09T14:32:00Z",
  "platform": "3",
  "destination": "Redhill",
  "status": "ON_TIME",
  "notes": null
}
```

#### Delayed Departure

```json
{
  "aimedDepartureTime": "2026-05-09T14:47:00Z",
  "expectedDepartureTime": "2026-05-09T14:52:00Z",
  "platform": "5",
  "destination": "Redhill",
  "status": "DELAYED",
  "notes": "Delayed due to preceding train"
}
```

#### Cancelled Departure

```json
{
  "aimedDepartureTime": "2026-05-09T15:02:00Z",
  "expectedDepartureTime": "2026-05-09T15:02:00Z",
  "platform": "2",
  "destination": "Redhill",
  "status": "CANCELLED",
  "notes": "Service cancelled"
}
```

---

## Serialization Libraries (Phase 2+)

### Kotlinx Serialization

```kotlin
@Serializable
data class TrainDeparture(
    @SerialName("aimedDepartureTime")
    val aimedDepartureTime: OffsetDateTime,
    @SerialName("expectedDepartureTime")
    val expectedDepartureTime: OffsetDateTime,
    @SerialName("platform")
    val platform: String,
    @SerialName("destination")
    val destination: String,
    @SerialName("status")
    val status: DepartureStatus,
    @SerialName("notes")
    val notes: String? = null
)

@Serializable
enum class DepartureStatus {
    @SerialName("ON_TIME")
    ON_TIME,
    @SerialName("DELAYED")
    DELAYED,
    @SerialName("CANCELLED")
    CANCELLED
}
```

### Gson (Alternative)

```kotlin
// Gson handles data classes automatically. No additional annotations needed.
val gson = Gson()
val json = gson.toJson(departure)
val departure = gson.fromJson(json, TrainDeparture::class.java)
```

### Jackson (Alternative)

```kotlin
// Jackson handles data classes and enums automatically.
val objectMapper = ObjectMapper()
val json = objectMapper.writeValueAsString(departure)
val departure = objectMapper.readValue(json, TrainDeparture::class.java)
```

---

## Interface Contract

The repository interface guarantees:

1. **Immutability**: Returned lists are unmodifiable or defensive copies.
2. **Consistency**: Multiple calls with the same parameters return identical results (in mock; may vary in live API).
3. **Ordering**: Results sorted by `aimedDepartureTime` (ascending).
4. **No Nulls in Collection**: Never returns `null` as collection elements; always valid TrainDeparture objects.
5. **Handling of Invalid Routes**: Returns empty list (not null, not exception).

```kotlin
interface TrainRepository {
    /**
     * @return Unmodifiable or defensive copy of departure list.
     *         Always a valid list (never null).
     *         Empty list indicates route not found or no services.
     */
    fun getNextDepartures(origin: String, destination: String): List<TrainDeparture>
}
```

---

## Evolution & Versioning

### Phase 1 (Current)

- No API version field (not needed for mock).
- Data structure is stable and unlikely to change.

### Phase 2 (When integrating live API)

Consider adding:

```json
{
  "version": "1.0",
  "timestamp": "2026-05-09T14:32:00Z",
  "departures": [ /* array of TrainDeparture */ ]
}
```

This allows future API versions to increment without breaking clients.

---

## Future Extensions (Not in Phase 1)

- **Arrival Times**: Add `aimedArrivalTime`, `expectedArrivalTime` for multi-leg journeys.
- **Coach Information**: Add `coach` or `coaches: List<Coach>` for seat reservations.
- **Operator**: Add `operator: String` for branding (e.g., "Southern Railway").
- **Fare Information**: Add `fare: Money` for ticket pricing.
- **Alerts**: Add `alerts: List<Alert>` for passenger alerts.

---

**Contract Status**: Finalized for Phase 1 | **Date**: 2026-05-09

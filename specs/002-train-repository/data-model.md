# Data Model: TrainRepository Entities

**Feature**: 002-train-repository
**Date**: 2026-05-09
**Phase**: 1 (Design)

This document formally specifies all data entities in the TrainRepository subsystem.

---

## Entity: TrainDeparture

Represents a single train departure event with timing, platform, and status information.

### Properties

| Property | Type | Required | Description | Constraints |
|----------|------|----------|-------------|------------|
| `aimedDepartureTime` | `OffsetDateTime` | Yes | Scheduled departure time per timetable. | Must be in future (or present for validation). |
| `expectedDepartureTime` | `OffsetDateTime` | Yes | Real-time expected departure, updates as train approaches. | Usually >= aimedDepartureTime. If < aimed, indicates service running early. |
| `platform` | `String` | Yes | Platform number or identifier (e.g., "3", "B", "North"). | Non-empty, max 10 chars. |
| `destination` | `String` | Yes | Station name or code of final destination. | Non-empty, e.g., "Redhill", "London Bridge". |
| `status` | `DepartureStatus` (enum) | Yes | Current operational status. | One of: ON_TIME, DELAYED, CANCELLED. |
| `notes` | `String` | No | Optional notes, e.g., platform changes, service alerts. | Nullable; max 200 chars. |

### Kotlin Definition

```kotlin
package com.platformdash.data.repository

import java.time.OffsetDateTime

data class TrainDeparture(
    val aimedDepartureTime: OffsetDateTime,
    val expectedDepartureTime: OffsetDateTime,
    val platform: String,
    val destination: String,
    val status: DepartureStatus,
    val notes: String? = null
) {
    init {
        require(platform.isNotEmpty()) { "Platform cannot be empty." }
        require(destination.isNotEmpty()) { "Destination cannot be empty." }
        require(notes == null || notes.length <= 200) { "Notes must be <= 200 characters." }
    }
}
```

### Validation Rules

1. **Times**: `expectedDepartureTime` >= `aimedDepartureTime` (or < for early running trains).
2. **Platform**: Non-empty string; examples: "1", "2A", "North", "Central".
3. **Destination**: Non-empty string; station names are not validated at this layer.
4. **Status**: One of three enumerated values (no free-text).
5. **Notes**: Optional; if provided, max 200 characters.

### UI Rules (Derived)

These rules are implemented in the UI/Widget layer, not here:

- **ON_TIME**: Display time in standard color.
- **DELAYED**: Display time in orange; show expected time; indicate delay duration.
- **CANCELLED**: Display "CANCELLED" in Material 3 red; cross out the departure.

---

## Enum: DepartureStatus

Represents the current operational status of a train service.

### Values

```kotlin
package com.platformdash.data.repository

enum class DepartureStatus {
    ON_TIME,      // Service running to schedule. aimed ≈ expected (±2 min).
    DELAYED,      // Service delayed. expected > aimed.
    CANCELLED     // Service cancelled. No train will depart.
}
```

### Mapping

| Status | Meaning | Widget Display | Example |
|--------|---------|----------------|---------|
| `ON_TIME` | Service running on schedule | Green or neutral text | "14:32 Platform 3" |
| `DELAYED` | Service delayed; real time available | Orange text with expected time | "14:32 → 14:45 Platform 3 (13 min delay)" |
| `CANCELLED` | Service withdrawn | Red text, "CANCELLED" overlay | "~~14:32~~ CANCELLED" (Material 3 red) |

---

## Interface: TrainRepository

Abstraction for train departure data access. Enables swapping implementations (MockTrainRepository → LiveTrainRepository).

### Kotlin Definition

```kotlin
package com.platformdash.data.repository

interface TrainRepository {
    /**
     * Fetches the next departures for a given origin-destination pair.
     *
     * @param origin Station code or name (e.g., "LBG" for London Bridge).
     * @param destination Station code or name (e.g., "RDH" for Redhill).
     * @return List of TrainDeparture objects, ordered by aimedDepartureTime (ascending).
     *         Returns empty list if route not found or no services available.
     */
    fun getNextDepartures(origin: String, destination: String): List<TrainDeparture>
}
```

### Contract Notes

- **Immutability**: Returned list is unmodifiable (defensive copy or immutable collection).
- **Ordering**: Results sorted by `aimedDepartureTime` ascending (next departures first).
- **Caching**: Implementation may cache; no guarantee of real-time updates (WorkManager refresh layer handles that).
- **No Exceptions**: Invalid routes return empty list; no exceptions thrown.
- **Null Safety**: Never returns null; always a list (possibly empty).

### Usage Example

```kotlin
val repository: TrainRepository = MockTrainRepository()

// Get next trains from London Bridge to Redhill
val departures = repository.getNextDepartures("LBG", "RDH")

// Display to user
for (departure in departures) {
    val timeStr = departure.expectedDepartureTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    println("$timeStr to ${departure.destination} (Platform ${departure.platform}) - ${departure.status}")
}
```

---

## Implementation: MockTrainRepository

Concrete implementation for development and testing.

### Kotlin Definition (Outline)

```kotlin
package com.platformdash.data.repository.mock

import com.platformdash.data.repository.DepartureStatus
import com.platformdash.data.repository.TrainDeparture
import com.platformdash.data.repository.TrainRepository
import java.time.OffsetDateTime
import java.time.ZoneOffset

class MockTrainRepository : TrainRepository {
    private val lbgToRdh = listOf(
        TrainDeparture(
            aimedDepartureTime = OffsetDateTime.of(2026, 5, 9, 14, 32, 0, 0, ZoneOffset.UTC),
            expectedDepartureTime = OffsetDateTime.of(2026, 5, 9, 14, 32, 0, 0, ZoneOffset.UTC),
            platform = "3",
            destination = "Redhill",
            status = DepartureStatus.ON_TIME
        ),
        TrainDeparture(
            aimedDepartureTime = OffsetDateTime.of(2026, 5, 9, 14, 47, 0, 0, ZoneOffset.UTC),
            expectedDepartureTime = OffsetDateTime.of(2026, 5, 9, 14, 52, 0, 0, ZoneOffset.UTC),
            platform = "5",
            destination = "Redhill",
            status = DepartureStatus.DELAYED,
            notes = "Delayed due to preceding train"
        ),
        TrainDeparture(
            aimedDepartureTime = OffsetDateTime.of(2026, 5, 9, 15, 02, 0, 0, ZoneOffset.UTC),
            expectedDepartureTime = OffsetDateTime.of(2026, 5, 9, 15, 02, 0, 0, ZoneOffset.UTC),
            platform = "2",
            destination = "Redhill",
            status = DepartureStatus.CANCELLED,
            notes = "Cancelled"
        ),
        // ... 2+ more ON_TIME departures
    )

    private val rdhToLbg = listOf(
        TrainDeparture(
            aimedDepartureTime = OffsetDateTime.of(2026, 5, 9, 15, 15, 0, 0, ZoneOffset.UTC),
            expectedDepartureTime = OffsetDateTime.of(2026, 5, 9, 15, 15, 0, 0, ZoneOffset.UTC),
            platform = "1",
            destination = "London Bridge",
            status = DepartureStatus.ON_TIME
        ),
        // ... more departures
    )

    override fun getNextDepartures(origin: String, destination: String): List<TrainDeparture> {
        val key = "$origin-$destination"
        return when (key) {
            "LBG-RDH" -> lbgToRdh.toList() // Defensive copy
            "RDH-LBG" -> rdhToLbg.toList()
            else -> emptyList()
        }
    }
}
```

### Mock Data Characteristics

- **Deterministic**: Same calls always return the same data (for testing).
- **Representative**: Includes ON_TIME, DELAYED, and CANCELLED statuses.
- **Minimal**: ~5 departures per direction sufficient for UI testing.
- **Low Latency**: Returns in <10ms (in-memory).
- **Bidirectional**: Supports both LBG→RDH and RDH→LBG.

---

## Relationships & Dependencies

```
TrainRepository (interface)
    ↑
    └─ MockTrainRepository (Phase 1 implementation)
    └─ LiveTrainRepository (Phase 2 implementation, future)

TrainDeparture (data class)
    └─ DepartureStatus (enum)
```

- **No external dependencies**: Mock implementation is self-contained.
- **Future: API Layer**: LiveTrainRepository will depend on HTTP client (e.g., Retrofit, OkHttp).

---

## Extension Points (Phase 2+)

1. **Coroutine Support**: Add `suspend fun getNextDepartures()` alongside blocking version.
2. **Reactive Streams**: Add `Flow<List<TrainDeparture>>` for real-time updates via WorkManager.
3. **Filtering**: Add methods like `getNextDepartures(origin, destination, filter: (TrainDeparture) -> Boolean)`.
4. **Caching**: Add TTL-based cache in MockTrainRepository or separate CachingRepository decorator.
5. **API Integration**: Implement LiveTrainRepository against chosen UK Rail API (TBD).

---

**Data Model Status**: Complete and ready for implementation | **Date**: 2026-05-09

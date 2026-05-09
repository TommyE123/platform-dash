# Quickstart: Using TrainRepository

**Feature**: 002-train-repository
**Date**: 2026-05-09
**Audience**: Android developers integrating departure data into the widget or app

---

## Overview

The `TrainRepository` is a stable abstraction for fetching train departure data. In Phase 1, you'll use `MockTrainRepository` for development and testing. In Phase 2, it will be swapped for a live API implementation without changing your code.

---

## Setup

### 1. Import the Repository

```kotlin
import com.platformdash.data.repository.TrainRepository
import com.platformdash.data.repository.mock.MockTrainRepository
import com.platformdash.data.repository.TrainDeparture
import com.platformdash.data.repository.DepartureStatus
```

### 2. Create a Repository Instance

```kotlin
val repository: TrainRepository = MockTrainRepository()
```

**Note**: Use the interface type (`TrainRepository`) for dependency injection, not the concrete class. This allows easy swapping to `LiveTrainRepository` later.

---

## Basic Usage

### Get Next Departures for a Route

```kotlin
val repository: TrainRepository = MockTrainRepository()

// Get next departures from London Bridge to Redhill
val departures = repository.getNextDepartures("LBG", "RDH")

// Display results
for (departure in departures) {
    println("Departure: ${departure.destination} at ${departure.expectedDepartureTime} (Platform ${departure.platform})")
}
```

### Handle Empty Results

```kotlin
val departures = repository.getNextDepartures("INVALID", "ROUTE")

if (departures.isEmpty()) {
    println("No departures found for this route.")
} else {
    // Process departures
}
```

---

## Working with Departure Data

### Access Departure Fields

```kotlin
val departure = departures.first()

// Timing
val aimedTime = departure.aimedDepartureTime  // OffsetDateTime
val expectedTime = departure.expectedDepartureTime  // OffsetDateTime

// Location & Status
val platform = departure.platform  // String, e.g., "3"
val destination = departure.destination  // String, e.g., "Redhill"
val status = departure.status  // DepartureStatus enum

// Optional notes
val notes = departure.notes  // String?, e.g., "Platform change to 5"
```

### Format Times for Display

```kotlin
import java.time.format.DateTimeFormatter

val formatter = DateTimeFormatter.ofPattern("HH:mm")

val aimedStr = departure.aimedDepartureTime.format(formatter)  // "14:32"
val expectedStr = departure.expectedDepartureTime.format(formatter)  // "14:45"
```

### Check Delay Duration

```kotlin
import java.time.Duration

val delay = Duration.between(departure.aimedDepartureTime, departure.expectedDepartureTime)
if (delay.toMinutes() > 0) {
    println("Delayed by ${delay.toMinutes()} minutes")
}
```

### Handle Status for UI Display

```kotlin
when (departure.status) {
    DepartureStatus.ON_TIME -> {
        // Display time in standard color
        println("On time: $expectedTime")
    }
    DepartureStatus.DELAYED -> {
        // Display time in orange; show delay reason
        val delayMinutes = Duration.between(
            departure.aimedDepartureTime,
            departure.expectedDepartureTime
        ).toMinutes()
        println("Delayed by $delayMinutes minutes")
    }
    DepartureStatus.CANCELLED -> {
        // Display "CANCELLED" in Material 3 red
        println("CANCELLED")
        if (departure.notes != null) {
            println("Reason: ${departure.notes}")
        }
    }
}
```

---

## Swap Direction (Widget Feature)

The repository supports bidirectional queries for the swap-direction widget action:

```kotlin
val repository: TrainRepository = MockTrainRepository()

// Get London Bridge → Redhill
val lbgToRdh = repository.getNextDepartures("LBG", "RDH")

// User taps "Swap Direction"
// Get Redhill → London Bridge
val rdhToLbg = repository.getNextDepartures("RDH", "LBG")

// Update widget display
displayDepartures(rdhToLbg)
```

---

## Testing with Mock Data

### Verify Mock Data Includes Edge Cases

```kotlin
val repository: TrainRepository = MockTrainRepository()
val departures = repository.getNextDepartures("LBG", "RDH")

// Assert we have expected test cases
assert(departures.size >= 5) { "Should have at least 5 departures" }
assert(departures.any { it.status == DepartureStatus.ON_TIME }) { "Should have at least one ON_TIME" }
assert(departures.any { it.status == DepartureStatus.DELAYED }) { "Should have at least one DELAYED" }
assert(departures.any { it.status == DepartureStatus.CANCELLED }) { "Should have at least one CANCELLED" }

println("Mock data validation: PASSED")
```

### Extract Test Data by Status

```kotlin
val onTime = departures.filter { it.status == DepartureStatus.ON_TIME }
val delayed = departures.filter { it.status == DepartureStatus.DELAYED }
val cancelled = departures.filter { it.status == DepartureStatus.CANCELLED }

println("On-time: ${onTime.size}, Delayed: ${delayed.size}, Cancelled: ${cancelled.size}")
```

---

## Dependency Injection (Android)

### With Hilt (Recommended for Android)

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideTrainRepository(): TrainRepository {
        // Phase 1: Use mock
        return MockTrainRepository()

        // Phase 2: Swap to live API
        // return LiveTrainRepository(apiClient)
    }
}

// In your ViewModel or Activity:
@HiltViewModel
class DepartureViewModel @Inject constructor(
    private val trainRepository: TrainRepository
) : ViewModel() {
    fun loadDepartures(origin: String, destination: String) {
        val departures = trainRepository.getNextDepartures(origin, destination)
        // Update UI
    }
}
```

### Manual Injection (For non-Android or simple setups)

```kotlin
class DepartureService(private val trainRepository: TrainRepository) {
    fun getNextDepartures(origin: String, destination: String): List<TrainDeparture> {
        return trainRepository.getNextDepartures(origin, destination)
    }
}

// Usage:
val repository: TrainRepository = MockTrainRepository()
val service = DepartureService(repository)
val departures = service.getNextDepartures("LBG", "RDH")
```

---

## Common Patterns

### Display All Departures in a LazyColumn (Compose)

```kotlin
@Composable
fun DepartureList(
    origin: String,
    destination: String,
    repository: TrainRepository
) {
    val departures = repository.getNextDepartures(origin, destination)

    LazyColumn {
        items(departures) { departure ->
            DepartureRow(departure)
        }
    }
}

@Composable
fun DepartureRow(departure: TrainDeparture) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(departure.expectedDepartureTime.format(DateTimeFormatter.ofPattern("HH:mm")))
        Spacer(modifier = Modifier.width(8.dp))
        Text(departure.destination)
        Spacer(modifier = Modifier.weight(1f))
        Text("Platform ${departure.platform}")

        // Status color
        val statusColor = when (departure.status) {
            DepartureStatus.ON_TIME -> Color.Green
            DepartureStatus.DELAYED -> Color.Yellow
            DepartureStatus.CANCELLED -> Color.Red
        }
        Text(departure.status.name, color = statusColor)
    }
}
```

### Update Widget Display Periodically

```kotlin
// In a ViewModel or Activity
viewModelScope.launch {
    while (true) {
        val departures = repository.getNextDepartures("LBG", "RDH")
        updateWidgetDisplay(departures)  // Update widget preview
        delay(30_000)  // Refresh every 30 seconds (mock data doesn't change, but pattern is ready for live API)
    }
}
```

---

## Troubleshooting

### "Empty list returned for valid route"

- Verify the route is exact: `"LBG"` (London Bridge), `"RDH"` (Redhill).
- MockTrainRepository only supports LBG↔RDH in Phase 1.
- Check that origin and destination are swapped correctly.

```kotlin
// ✅ Correct
repository.getNextDepartures("LBG", "RDH")
repository.getNextDepartures("RDH", "LBG")

// ❌ Incorrect (will return empty)
repository.getNextDepartures("London Bridge", "Redhill")  // Use codes, not names
repository.getNextDepartures("lbg", "rdh")  // Case-sensitive (may be case-insensitive in Phase 2)
```

### "NullPointerException on departure.notes"

- `notes` is nullable. Always check before accessing:

```kotlin
// ✅ Safe
val note = departure.notes ?: "No additional notes"

// ❌ Unsafe
val note = departure.notes.toString()  // Will throw if null
```

### "Time display shows wrong timezone"

- Remember: `TrainDeparture` times are in `OffsetDateTime` (UK time).
- Convert to user's local timezone when displaying:

```kotlin
val userZone = ZoneId.systemDefault()
val userTime = departure.expectedDepartureTime.atZoneSameInstant(userZone)
println("User local time: ${userTime.format(DateTimeFormatter.ofPattern("HH:mm zzz"))}")
```

---

## Next Steps

1. **Phase 1**: Integrate MockTrainRepository into your widget/app. Test with the provided mock data.
2. **Phase 2**: SwapMockTrainRepository for LiveTrainRepository when the UK Rail API is finalized.
3. **Phase 3**: Add reactive streams (Flow) or async (suspend) variants if needed for performance.

---

**Quickstart Complete** | **Date**: 2026-05-09 | **Status**: Ready for developer integration

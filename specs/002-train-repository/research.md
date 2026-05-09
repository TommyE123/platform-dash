# Research: TrainRepository Design & Contract

**Feature**: 002-train-repository
**Date**: 2026-05-09
**Phase**: 0 (Research & Analysis)

This document consolidates research findings and design decisions made during the planning phase.

## Research Completed

### R1: Technology Stack Decision

**Question**: Kotlin vs Java vs Multiplatform Kotlin (KMP) for the repository layer?

**Research**:
- Kotlin 1.9 is the recommended language for Android development per Google.
- Kotlin provides null-safety, extension functions, and data classes that reduce boilerplate.
- KMP (Multiplatform Kotlin) can target JVM, Android, iOS, Web, but adds complexity (build config, platform-specific implementations).
- For Phase 1, a single-platform Kotlin JVM/Android library is sufficient.

**Decision**: **Kotlin 1.9 targeting JVM 11+**
- Rationale: Standard for Android, safe, modern, minimal dependency overhead.
- Future: KMP can be adopted post-v1 if iOS support is needed.

### R2: API Design (Synchronous vs Reactive)

**Question**: Should `getNextDepartures()` be synchronous (blocking), async (suspend), or reactive (Flow)?

**Research**:
- Synchronous (blocking): Simple contract, easy to test, fits mock data pattern. Best for libraries.
- Coroutines (suspend): Modern Kotlin pattern, non-blocking, but adds coroutine dependency and complicates mock setup.
- RxJava/Reactive: More powerful, harder to learn, overkill for Phase 1.

**Decision**: **Synchronous (blocking) API**
```kotlin
interface TrainRepository {
    fun getNextDepartures(origin: String, destination: String): List<TrainDeparture>
}
```
- Rationale: Simpler contract, testable, composable. UI layer (Widget/App) can wrap with Coroutines or Flow.
- Future: Add `suspend` variants in Phase 2 without breaking existing interface.

### R3: Data Structure

**Question**: How to represent a single train departure?

**Research**:
- Must include: Aimed Departure Time, Expected Departure Time, Platform, Destination, Status.
- Should be immutable (data class in Kotlin).
- Timezone handling: Use `java.time.OffsetDateTime` (thread-safe, unambiguous).

**Decision**: **Kotlin data class with OffsetDateTime**
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
- Rationale: Immutable, thread-safe, serializable, matches UK Rail terminology.
- Null safety: Notes are optional; times are required (non-null).

### R4: Mock Data Strategy

**Question**: Should mock data be hard-coded, loaded from JSON, or generated?

**Research**:
- Hard-coded: Deterministic, zero dependencies, easy to extend.
- JSON files: Adds resource loading logic, fragile file management.
- Generated: Requires faker libraries, unnecessary complexity for transport data.

**Decision**: **Hard-coded in MockTrainRepository class**
```kotlin
class MockTrainRepository : TrainRepository {
    private val lbgToRdh = listOf(
        TrainDeparture(
            aimedDepartureTime = ...,
            expectedDepartureTime = ...,
            platform = "3",
            destination = "Redhill",
            status = DepartureStatus.ON_TIME
        ),
        // More departures...
    )

    override fun getNextDepartures(origin: String, destination: String): List<TrainDeparture> {
        return when ("$origin-$destination") {
            "LBG-RDH" -> lbgToRdh
            "RDH-LBG" -> rdhToLbg
            else -> emptyList()
        }
    }
}
```
- Rationale: Deterministic for testing, easy to modify, no dependencies.
- Flexibility: Can generate new mock data on demand or add variations for edge cases.

### R5: Error Handling

**Question**: What should happen if an invalid route is requested?

**Research**:
- Return empty list: Functional style, caller-friendly, no exceptions.
- Throw exception: Requires try-catch boilerplate, complicates UI code.
- Result<T, E>: Rust-style Result type, adds complexity, doesn't match Kotlin conventions.

**Decision**: **Return empty List on invalid routes**
- Rationale: Follows Kotlin standard library patterns. UI code can check `.isEmpty()` and display "No departures" gracefully.

### R6: Time Zone Handling

**Question**: How to handle UK time zones (GMT/BST) and widget display in other zones?

**Research**:
- Use `OffsetDateTime` (includes timezone offset): Unambiguous, serializable, standard for APIs.
- Mock data: Use UK time (+00:00 GMT, +01:00 BST depending on season).
- Widget: Convert to user's local time on display.

**Decision**: **All times stored as OffsetDateTime with UK zone offset**
```kotlin
val departure = TrainDeparture(
    aimedDepartureTime = OffsetDateTime.of(2026, 5, 9, 14, 32, 0, 0, ZoneOffset.UTC),
    ...
)
```
- Rationale: Matches real-world API standards. Widget can use `.atZoneSameInstant(userTimeZone)` for display.

### R7: Interface Stability (Principle IV - Data Contract)

**Question**: Should the interface be final/sealed to prevent misuse?

**Research**:
- Open interface: Allows users to implement TrainRepository for testing (test doubles).
- Sealed/final: Protects against accidental subclassing, but limits testing flexibility.

**Decision**: **Open interface (non-final)**
```kotlin
interface TrainRepository {
    fun getNextDepartures(origin: String, destination: String): List<TrainDeparture>
}
```
- Rationale: Enables dependency injection and test doubles (e.g., FakeTrainRepository in tests). No need for sealed/final at this stage.

---

## Phase 1 Deliverables

Following Phase 0 research decisions, Phase 1 will produce:

1. **data-model.md**: Detailed entity definitions and relationships.
2. **contracts/train-departure-schema.md**: Optional schema documentation (for future API contracts).
3. **quickstart.md**: Usage guide for developers integrating the repository.
4. **Tasks** (via /speckit.tasks): Implementation, testing, and documentation tasks.

---

## Open Questions (Deferred to Phase 2+)

- Should the repository accept API credentials? (Deferred pending BYOK design.)
- Should we add filtering by time range or arrival time? (Phase 2 enhancement.)
- Should we cache results in MockTrainRepository? (Phase 2 performance tuning.)
- Which UK Rail API will we integrate with? (Phase 2 API selection.)

---

**Research Status**: Complete | **Approval**: Ratified for Phase 1 implementation

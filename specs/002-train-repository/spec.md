# Feature Specification: TrainRepository Interface and MockTrainRepository Implementation

**Feature Branch**: `002-train-repository`
**Created**: 2026-05-09
**Status**: Draft
**Input**: Create a TrainRepository interface and a MockTrainRepository implementation. The repository should provide departure data for London Bridge (LBG) to Redhill (RDH). Include fields for: Aimed Departure Time, Expected Departure Time, Platform, Destination, and Status (On Time/Delayed/Cancelled). Ensure the Mock data includes at least one delayed and one cancelled train to test our UI rules.

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Retrieve departure information for configured route (Priority: P1)

As a commuter using the PlatformDash widget, I want to query train departure data for my configured route (LBG→RDH) so that I can see real-time information about upcoming trains with accurate timing and platform details.

**Why this priority**: This is the foundational data contract that all UI components depend on. No other features can proceed until the repository abstraction and mock data are working.

**Independent Test**: Can be tested by calling `MockTrainRepository.getNextDepartures("LBG", "RDH")` and verifying the returned list contains properly formatted TrainDeparture objects with all required fields populated.

**Acceptance Scenarios**:

1. **Given** the MockTrainRepository is initialized, **When** I call `getNextDepartures("LBG", "RDH")`, **Then** I receive a list of at least 5 TrainDeparture objects.
2. **Given** a list of departures, **When** I inspect each object, **Then** each has: aimedDepartureTime, expectedDepartureTime, platform, destination, status populated.
3. **Given** the mock data, **When** I query departures, **Then** the list includes at least one DELAYED train and one CANCELLED train for UI state testing.
4. **Given** a TrainDeparture with status=CANCELLED, **When** displayed, **Then** the UI rule is testable to highlight it in Material 3 red.

### User Story 2 - Swap departure direction on the fly (Priority: P1)

As a commuter, I want to swap the origin and destination stations on the widget so I can quickly check return journey times without reconfiguring settings.

**Why this priority**: The swap-direction action is a core UX requirement per the constitution and must be designed into the repository contract from the start.

**Independent Test**: Call `getNextDepartures(destination, origin)` with reversed arguments and verify that the repository returns appropriate data for the reverse journey (RDH→LBG).

**Acceptance Scenarios**:

1. **Given** departures for LBG→RDH are available, **When** I reverse the parameters to call `getNextDepartures("RDH", "LBG")`, **Then** I receive a separate list of departures for the return journey.
2. **Given** the mock data for both directions, **When** I compare the lists, **Then** the origin and destination are appropriately swapped in the results.

### User Story 3 - Access repository through stable interface contract (Priority: P1)

As a developer, I want the data access layer to be abstracted behind a TrainRepository interface so that I can swap the MockTrainRepository for a live API implementation without changing UI or widget code.

**Why this priority**: Contract-first design (Principle IV) is foundational to keeping UI development independent of API integration timelines.

**Independent Test**: Verify that both MockTrainRepository and a future LiveTrainRepository (when implemented) can be injected into the same UI component without compilation errors or interface mismatches.

**Acceptance Scenarios**:

1. **Given** the TrainRepository interface is defined, **When** I create a MockTrainRepository that implements it, **Then** I can instantiate and use it to fetch departures.
2. **Given** the interface, **When** a future LiveTrainRepository is implemented against the same interface, **Then** it can be swapped in at runtime (dependency injection) without UI code changes.

---

### Edge Cases

- What happens when no departures are available for a route? (Return empty list or null?)
- How does the mock data handle platform changes between aimed and expected times?
- Does the repository validate origin/destination station codes, or is that the caller's responsibility?

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST expose a `TrainRepository` interface with a method `getNextDepartures(origin: String, destination: String): List<TrainDeparture>`.
- **FR-002**: System MUST provide a `MockTrainRepository` implementation that returns deterministic, test-friendly departure data.
- **FR-003**: Each `TrainDeparture` object MUST contain: `aimedDepartureTime`, `expectedDepartureTime`, `platform`, `destination`, `status`.
- **FR-004**: The mock data MUST include at least 5 departures for the default LBG→RDH route.
- **FR-005**: The mock data MUST include at least one train with status=DELAYED and one with status=CANCELLED for UI testing.
- **FR-006**: The repository MUST support bidirectional queries (LBG→RDH and RDH→LBG) for the swap-direction use case.

### Constitution Alignment *(mandatory)*

- **CA-001 Architecture**: This repository layer does not involve UI; it is a pure data abstraction. Future UI will use Jetpack Compose + Material 3 per Principle I.
- **CA-002 BYOK Security**: The repository interface is designed to eventually accept API credentials (not hardcoded). The mock implementation does not require keys. Future implementations must respect Principle II.
- **CA-003 Commute Focus**: The default route and swap-direction capability are baked into the interface, supporting LBG↔RDH queries directly.
- **CA-004 Data Contract**: This feature IS the data contract (Principle IV). The TrainRepository interface is the contract; MockTrainRepository is the first implementation.
- **CA-005 Refresh Reliability**: The repository provides the data layer; refresh scheduling (WorkManager) will be handled at a higher layer, but the repository must be stable and fast.

### Key Entities

- **TrainDeparture**: Represents a single train departure. Fields: `aimedDepartureTime` (OffsetDateTime), `expectedDepartureTime` (OffsetDateTime), `platform` (String), `destination` (String), `status` (enum: ON_TIME / DELAYED / CANCELLED).
- **TrainRepository**: Interface defining the contract for fetching departures. Single method: `getNextDepartures(origin, destination): List<TrainDeparture>`.
- **MockTrainRepository**: Concrete implementation of TrainRepository. Returns hard-coded, deterministic test data.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: MockTrainRepository returns 5+ departures for both LBG→RDH and RDH→LBG within <10ms response time.
- **SC-002**: At least one DELAYED departure and one CANCELLED departure are present in the mock data for each direction to enable UI state testing.
- **SC-003**: The TrainRepository interface compiles without errors and can be implemented by a second repository class (contract stability).
- **SC-004**: All unit tests for MockTrainRepository pass, including edge cases (empty routes, status transitions, bidirectional queries).

## Assumptions

- The repository is Kotlin-based (standard for Android development) or Java if specified otherwise.
- "On Time" is the default status; "Delayed" and "Cancelled" are exceptions represented in the mock data.
- Station codes follow the National Rail Enquiries standard (LBG = London Bridge, RDH = Redhill).
- The repository is synchronous; async/reactive wrappers (Coroutines, RxJava) will be added in a future task layer.
- Future API integration will be a different rail provider (TBD); mock data is purely fictional and not representative of real train times.

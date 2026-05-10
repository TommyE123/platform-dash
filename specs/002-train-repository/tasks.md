---
description: "Implementation task list for TrainRepository feature"
---

# Tasks: TrainRepository Interface and MockTrainRepository Implementation

**Feature Branch**: `002-train-repository`
**Input**: Design documents from `specs/002-train-repository/` (spec.md, plan.md, research.md, data-model.md, quickstart.md)
**Prerequisites**: All Phase 1 design artifacts complete ✓

**Tests Included**: Full unit test suite as per request, including edge cases for mock data (delayed, cancelled trains, bidirectional routes)

**Organization**: Tasks grouped by user story (US1, US2, US3) to enable independent implementation and testing. All tasks follow Principle IV (contract-first data design).

**Constitution Alignment**: All tasks include explicit work for Data Contract (TrainRepository interface + MockTrainRepository), mock-first architecture (Principle IV), and commute-focused data (LBG↔RDH bidirectional support).

---

## Format: `[ID] [P?] [Story?] Description`

- **[P]**: Can run in parallel (different files, no dependencies within same phase)
- **[Story]**: User story label (US1, US2, US3)
- **File Paths**: Exact locations for all code files
- **Gitmoji**: All commits use [gitmoji.dev](https://gitmoji.dev) convention (🏗️ = architecture, ✅ = test, 📚 = docs, etc.)

---

## Phase 1: Setup (Project Structure & Dependencies)

**Purpose**: Initialize Kotlin project structure and configure build system

**Duration**: ~30 minutes
**Deliverable**: Buildable Kotlin project with test framework configured

- [x] T001 Create Android module directory structure at `app/src/main/kotlin/com/platformdash/data/repository/` and `app/src/test/kotlin/com/platformdash/data/repository/`
- [x] T002 [P] Configure `build.gradle.kts` with Kotlin 1.9, JUnit 4+, and Android test dependencies (Jetpack, AndroidX)
- [x] T003 [P] Defer linting to CI/CD (Mega-Linter); local hooks removed.
- [x] T004 Create empty package structure with `package com.platformdash.data.repository` and `package com.platformdash.data.repository.mock`

**Checkpoint**: Kotlin project compiles, test framework is ready

**Gitmoji Commits**: 🏗️ for project structure, ⚙️ for config

---

## Phase 2: Foundational (Core Data Abstractions)

**Purpose**: Implement foundational data structures and interface that ALL user stories depend on

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

**Duration**: ~1 hour
**Deliverable**: Compilable TrainRepository interface, TrainDeparture data class, DepartureStatus enum

- [ ] T005 [P] Create `DepartureStatus.kt` enum with three values: `ON_TIME`, `DELAYED`, `CANCELLED` in `app/src/main/kotlin/com/platformdash/data/repository/`
- [ ] T006 [P] Create `TrainDeparture.kt` data class with all required fields in `app/src/main/kotlin/com/platformdash/data/repository/`:
  - `aimedDepartureTime: OffsetDateTime` (required)
  - `expectedDepartureTime: OffsetDateTime` (required)
  - `platform: String` (required)
  - `destination: String` (required)
  - `status: DepartureStatus` (required)
  - `notes: String? = null` (optional)
  - Include init block for field validation (non-empty strings, notes max 200 chars)
- [ ] T007 [P] Create `TrainRepository.kt` interface with single method signature:
  ```
  fun getNextDepartures(origin: String, destination: String): List<TrainDeparture>
  ```
  in `app/src/main/kotlin/com/platformdash/data/repository/`
- [ ] T008 Add KDoc comments to all three files explaining purpose, usage, and field constraints

**Checkpoint**: All foundational classes compile; interface is stable and ready for implementation

**Gitmoji Commits**: 🏗️ for architecture/data model

---

## Phase 3: User Story 1 - Retrieve Departure Information (Priority: P1) 🎯 MVP

**Goal**: Implement MockTrainRepository to provide 5+ deterministic train departures for LBG→RDH with both DELAYED and CANCELLED trains for UI testing

**Independent Test**: Call `MockTrainRepository.getNextDepartures("LBG", "RDH")` and verify it returns properly formatted list with all required fields

**Acceptance Criteria** (from spec.md):
1. Returns list of ≥5 TrainDeparture objects
2. Each object has all required fields populated (aimedDepartureTime, expectedDepartureTime, platform, destination, status)
3. Includes ≥1 DELAYED train and ≥1 CANCELLED train for UI state testing
4. Response time <10ms (in-memory, no I/O)

### Tests for User Story 1 ✅

> **Write these tests FIRST, ensure they FAIL before implementation**

- [ ] T009 [P] [US1] Create `MockTrainRepositoryTest.kt` in `app/src/test/kotlin/com/platformdash/data/repository/` with test class and test method stubs:
  - `testGetNextDeparturesReturnsMinimumFiveDepartures()`
  - `testAllDepartureFieldsArePopulated()`
  - `testMockDataIncludesDelayedAndCancelledTrains()`
  - `testGetNextDeparturesPerformance()`
  - `testBidirectionalQueriesWork()`
- [ ] T010 [P] [US1] Implement `testGetNextDeparturesReturnsMinimumFiveDepartures()` to assert `MockTrainRepository.getNextDepartures("LBG", "RDH").size >= 5`
- [ ] T011 [P] [US1] Implement `testAllDepartureFieldsArePopulated()` to verify each TrainDeparture has non-null aimedDepartureTime, expectedDepartureTime, platform, destination, status
- [ ] T012 [P] [US1] Implement `testMockDataIncludesDelayedAndCancelledTrains()` to assert at least one departure has status=DELAYED and at least one has status=CANCELLED
- [ ] T013 [P] [US1] Implement `testGetNextDeparturesPerformance()` to measure response time and assert <10ms
- [ ] T014 [P] [US1] Implement `testBidirectionalQueriesWork()` to verify both `getNextDepartures("LBG", "RDH")` and `getNextDepartures("RDH", "LBG")` work correctly

### Implementation for User Story 1

- [ ] T015 [US1] Create `MockTrainRepository.kt` in `app/src/main/kotlin/com/platformdash/data/repository/mock/` implementing TrainRepository
- [ ] T016 [US1] Add hard-coded `lbgToRdh` property as `List<TrainDeparture>` with ≥5 departures including:
  - ≥3 ON_TIME departures
  - ≥1 DELAYED departure (expectedDepartureTime > aimedDepartureTime, with notes e.g., "Delayed due to preceding train")
  - ≥1 CANCELLED departure (with notes e.g., "Service cancelled")
- [ ] T017 [US1] Add hard-coded `rdhToLbg` property as `List<TrainDeparture>` with ≥5 departures mirroring lbgToRdh (same pattern: ON_TIME, DELAYED, CANCELLED)
- [ ] T018 [US1] Implement `getNextDepartures()` method:
  - Accept `origin` and `destination` as String parameters
  - Return appropriate list based on "$origin-$destination" key ("LBG-RDH" → lbgToRdh, "RDH-LBG" → rdhToLbg)
  - Return empty list for invalid routes (no exceptions)
  - Ensure times use `OffsetDateTime` with UTC or UK timezone (ZoneOffset.UTC or ZoneOffset.of("+01:00"))
- [ ] T019 [US1] Add sorting: Return departures sorted by `aimedDepartureTime` (ascending, next departures first)
- [ ] T020 [US1] Add KDoc comments to class and method explaining mock data determinism, bidirectional support, and test usage

**Checkpoint**: User Story 1 fully functional; all tests pass; MockTrainRepository returns correct data for LBG↔RDH routes

**Gitmoji Commits**: 🏗️ for MockTrainRepository class, ✅ for tests passing

---

## Phase 4: User Story 2 - Swap Departure Direction (Priority: P1)

**Goal**: Ensure MockTrainRepository correctly handles bidirectional queries (RDH→LBG reverse journey)

**Independent Test**: Call `getNextDepartures("RDH", "LBG")` separately from US1 and verify it returns valid data with swapped origin/destination

**Acceptance Criteria** (from spec.md):
1. Supports both LBG→RDH and RDH→LBG queries
2. Return values for reverse journey are properly formatted
3. Can be used independently for swap-direction widget action without side effects

### Tests for User Story 2 ✅

- [ ] T021 [P] [US2] Create `TrainRepositoryContractTest.kt` in `app/src/test/kotlin/com/platformdash/data/repository/` for interface contract validation:
  - `testRepositoryInterfaceCanBeImplemented()`
  - `testBidirectionalRoutesReturnIndependentResults()`
  - `testSwapDirectionDoesNotAffectOriginalRoute()`
- [ ] T022 [P] [US2] Implement `testRepositoryInterfaceCanBeImplemented()` to verify a second repository class can implement the interface without conflicts
- [ ] T023 [P] [US2] Implement `testBidirectionalRoutesReturnIndependentResults()` to query both directions and verify:
  - Both return non-empty lists
  - Destinations are correctly swapped (lbgToRdh has destination="Redhill", rdhToLbg has destination="London Bridge")
  - Origin/destination pairs are logical (not reversed in data)
- [ ] T024 [P] [US2] Implement `testSwapDirectionDoesNotAffectOriginalRoute()` to query LBG→RDH, then RDH→LBG, then LBG→RDH again and verify same data returned (no state mutation)

### Implementation for User Story 2

- [ ] T025 [US2] Add additional test data to `MockTrainRepository.rdhToLbg` if needed to ensure symmetry (if lbgToRdh has platforms 2, 3, 5, ensure rdhToLbg has matching distribution)
- [ ] T026 [US2] Verify `getNextDepartures()` implementation correctly routes both directions; no additional code likely needed (done in T018)
- [ ] T027 [US2] Add integration test scenario: Simulate widget swap-direction action by calling getNextDepartures twice with reversed arguments and verify display data changes appropriately

**Checkpoint**: User Story 2 fully functional; bidirectional queries work; contract tests pass

**Gitmoji Commits**: ✅ for contract tests passing, 🧪 for test scenarios

---

## Phase 5: User Story 3 - Access via Stable Interface Contract (Priority: P1)

**Goal**: Verify TrainRepository interface is stable, testable, and ready for future implementations (LiveTrainRepository)

**Independent Test**: Verify interface signature doesn't require changes; future implementations (e.g., LiveTrainRepository) can be created without modifying the interface or US1/US2 tests

**Acceptance Criteria** (from spec.md):
1. TrainRepository interface compiles and can be implemented by multiple classes
2. Dependency injection pattern works (Hilt, manual injection, test doubles)
3. Interface is "final" in design (not likely to need changes for Phase 2 API integration)

### Tests for User Story 3 ✅

- [ ] T028 [P] [US3] Create `TrainRepositoryDependencyInjectionTest.kt` in `app/src/test/kotlin/com/platformdash/data/repository/` testing:
  - `testTrainRepositoryCanBeInjectedAsInterface()`
  - `testFakeTrainRepositoryImplementation()`
  - `testMultipleImplementationsCoexist()`
- [ ] T029 [P] [US3] Implement `testTrainRepositoryCanBeInjectedAsInterface()` to create a simple service class that accepts TrainRepository in constructor and uses it (demonstrating DI pattern)
- [ ] T030 [P] [US3] Implement `testFakeTrainRepositoryImplementation()` to create a FakeTrainRepository test double that implements the interface with minimal mock data
- [ ] T031 [P] [US3] Implement `testMultipleImplementationsCoexist()` to verify both MockTrainRepository and FakeTrainRepository can be used interchangeably with the same interface type

### Implementation for User Story 3

- [ ] T032 [US3] Create `FakeTrainRepository.kt` test double in `app/src/test/kotlin/com/platformdash/data/repository/` implementing TrainRepository with minimal mock data (1-2 departures per route for fast tests)
- [ ] T033 [US3] Add interface stability documentation: Verify `TrainRepository` interface has no type parameters, no builder patterns, and simple method signatures (future-proof)
- [ ] T034 [US3] Document dependency injection pattern in code comments: Show how to swap MockTrainRepository ↔ FakeTrainRepository ↔ LiveTrainRepository (future) without code changes
- [ ] T035 [US3] Add optional Hilt @Module example in comments (for Android integration later)

**Checkpoint**: User Story 3 fully tested; interface is proven to be stable and injectable; contract tests all pass

**Gitmoji Commits**: 🏗️ for interface design, ✅ for DI tests passing, 📚 for documentation

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Documentation, quality, and finalization

**Duration**: ~30 minutes
**Deliverable**: Complete, documented, committed feature ready for UI integration

- [ ] T036 [P] Add JSDoc/KDoc comments to all public classes, methods, and data classes:
  - TrainRepository interface: Explain purpose, return value guarantees (no nulls, empty list on error), timeout expectations
  - TrainDeparture: Explain each field, constraints, examples
  - DepartureStatus: Explain each enum value and UI representation (ON_TIME=standard, DELAYED=orange, CANCELLED=red)
  - MockTrainRepository: Explain deterministic behavior, test data characteristics, usage patterns
- [ ] T037 [P] Update `quickstart.md` (from Phase 1 artifacts) with tested code examples:
  - Add real package names: `com.platformdash.data.repository`
  - Ensure all code snippets compile (copy from actual implementation, not pseudocode)
  - Add "Running the Tests" section with: `./gradlew test` and expected output
- [ ] T038 [P] Run all unit tests from MockTrainRepositoryTest.kt, TrainRepositoryContractTest.kt, TrainRepositoryDependencyInjectionTest.kt and verify 100% pass rate
- [ ] T039 [P] Run code quality checks:
  - ktlint or Detekt on all source files
  - Fix style violations (naming, line length, complexity)
  - Ensure null-safety warnings are resolved
- [ ] T040 Code cleanup: Remove any debug print statements, unused imports, commented-out code
- [ ] T041 Verify performance: Run T013 (performance test) and confirm all getNextDepartures() calls return in <10ms
- [ ] T042 [P] Verify design decisions against plan.md:
  - Confirm Kotlin 1.9 used (not Java)
  - Confirm synchronous API (not suspend/async in this layer)
  - Confirm hard-coded mock data (no JSON files)
  - Confirm OffsetDateTime used for timezone safety
  - Confirm empty list on invalid routes (no exceptions)

**Gitmoji Commits**:
- 📚 for documentation updates (quickstart.md, KDoc)
- 🧹 for cleanup
- ✅ for test verification
- 🚀 for final release commit (after all above)

---

## Final Checkpoint & Commit

**Before merging to main**:

- [ ] T043 Run final verification:
  - All 5 user story tests pass (T010-T014, T021-T024, T028-T031): ✅
  - All 3 data model classes compile: ✅
  - All 1 interface compiles: ✅
  - All 1 implementation class compiles and works: ✅
  - Code follows PlatformDash Constitution (Principle I-V): ✅
  - No compilation warnings: ✅
  - Performance <10ms verified: ✅

- [ ] T044 Create final commit with gitmoji:
  ```
  🚀 feat(data): Complete TrainRepository Phase 1 implementation with MockTrainRepository and full test suite

  - Implement TrainRepository interface (Principle IV data contract)
  - Implement MockTrainRepository with LBG↔RDH bidirectional support
  - Add 5+ mock departures per direction with DELAYED/CANCELLED test cases
  - Add comprehensive unit tests: MockTrainRepositoryTest, TrainRepositoryContractTest, TrainRepositoryDependencyInjectionTest
  - Add KDoc for all public API
  - Update quickstart.md with working code examples
  - Verify <10ms performance and full Constitution compliance

  Closes feature 002-train-repository Phase 1
  ```

- [ ] T045 Create Pull Request from `002-train-repository` to `main` with:
  - Title: "feat: TrainRepository interface and MockTrainRepository implementation (Phase 1)"
  - Description referencing user stories US1, US2, US3 and all acceptance criteria from spec.md
  - Link to implementation plan: `specs/002-train-repository/plan.md`
  - Checklist: All tests pass, Constitution gates verified, performance targets met

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies → Start immediately
- **Foundational (Phase 2)**: Depends on Setup completion → BLOCKS all user stories  ⚠️ **CRITICAL**
- **User Stories (Phase 3-5)**: ALL depend on Foundational phase completion
  - US1, US2, US3 can run in **parallel** after Phase 2 (if team capacity allows)
  - Or sequentially in priority order (P1: US1 → P1: US2 → P1: US3)
  - Each has independent test criteria and can be verified separately
- **Polish (Phase 6)**: Depends on all user stories being complete
- **Final Commit (Phase 7)**: Depends on Polish being complete

### Parallel Opportunities

**Within Phase 1 (Setup)**:
- T002, T003, T004 can run in parallel (marked [P])

**Within Phase 2 (Foundational)**:
- T005, T006, T007 can run in parallel (marked [P])
- All must complete before Phase 3 starts

**Within Phase 3-5 (User Stories)**:
- Tests for each story (T009-T014, T021-T024, T028-T031) marked [P] can run in parallel
- Implementation tasks generally sequential within each story (tests before implementation per TDD)
- BUT: US1, US2, US3 can be assigned to different team members and worked in parallel

**Within Phase 6 (Polish)**:
- T036, T037, T038, T039, T042 can run in parallel (marked [P])

### Team Execution Scenarios

**Single Developer** (Sequential):
1. Phase 1: Setup (1 person, 30 min)
2. Phase 2: Foundational (1 person, 1 hour)
3. Phase 3: US1 (1 person, 1.5 hours) → MVPR ready here
4. Phase 4: US2 (1 person, 1 hour)
5. Phase 5: US3 (1 person, 1 hour)
6. Phase 6: Polish (1 person, 30 min)
7. **Total time: ~5 hours**

**Two Developers** (Parallel):
- Dev A: Phase 1 Setup + Phase 2 Foundational + Phase 3 US1 (3 hours)
- Dev B: Waits for Phase 2, then does Phase 4 US2 + Phase 5 US3 in parallel with Dev A (2 hours)
- Both: Phase 6 Polish together (30 min)
- **Total time: ~3.5 hours (better resource utilization)**

---

## Success Criteria (from spec.md)

**All must be verified before Phase 6 completion**:

- ✅ **SC-001**: MockTrainRepository returns 5+ departures for both LBG→RDH and RDH→LBG within <10ms response time
  - Verified by: T010, T013, T023, T041
- ✅ **SC-002**: At least one DELAYED departure and one CANCELLED departure present in mock data for each direction
  - Verified by: T012, T024
- ✅ **SC-003**: TrainRepository interface compiles without errors and can be implemented by multiple classes
  - Verified by: T028, T029, T030, T031
- ✅ **SC-004**: All unit tests pass, including edge cases (empty routes, status transitions, bidirectional queries)
  - Verified by: T010-T014, T021-T024, T028-T031, T038

**Constitution Alignment**:
- ✅ **CA-001 Architecture**: Pure data abstraction (no UI) ← Verified by: T001-T008, code structure
- ✅ **CA-002 Security**: Interface designed for future BYOK ← Verified by: T034 documentation
- ✅ **CA-003 Commute Focus**: LBG↔RDH bidirectional support ← Verified by: T016, T017, T023-T024
- ✅ **CA-004 Data Contract**: TrainRepository interface IS the contract ← Verified by: T005-T007, T028-T031
- ✅ **CA-005 Refresh**: Data layer stable; WorkManager concern higher ← Verified by: T041, code review

---

## Ready for Phase 2 (Implementation)

Once all tasks above are complete and all tests pass, the TrainRepository layer is production-ready and the feature is ready for:

1. **UI Integration** (Phase 2+): Widget and app teams can now use MockTrainRepository to develop UI
2. **API Integration** (Phase 2+): Backend team can implement LiveTrainRepository against chosen UK Rail API
3. **Performance Optimization** (Phase 3+): Caching, async variants, reactive streams
4. **Dependency Injection** (Phase 2+): Wire repository into Hilt/DI framework for Android app

**Branch Status**: `002-train-repository` ready to merge to `main` with PR review

---

**Generated**: 2026-05-09 | **Feature**: 002-train-repository | **Phase**: 1 Implementation Ready | **Status**: ✅ Ready for execution

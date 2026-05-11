# Tasks: Commute Departures Widget

**Input**: Design documents from /specs/001-commute-widget/
**Prerequisites**: plan.md (required), spec.md (required), research.md, data-model.md, contracts/, quickstart.md

**Tests**: No explicit TDD request in the specification, so test creation tasks are not included in this task list.

**Organization**: Tasks are grouped by user story to enable independent implementation and verification.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Confirm build and widget metadata baseline before feature-specific changes.

- [x] T001 Upgrade and pin AGP to 9.2.1 in build.gradle.kts and app/build.gradle.kts
- [x] T002 Validate Compose/Glance dependency compatibility after AGP 9.2.1 alignment in app/build.gradle.kts
- [x] T003 Validate widget provider resize metadata in app/src/main/res/xml/commute_widget_info.xml
- [x] T004 [P] Align quickstart verification flow with AGP 9.2.1 build commands in specs/001-commute-widget/quickstart.md

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Shared building blocks that all user stories depend on.

**⚠️ CRITICAL**: Complete this phase before user-story implementation.

- [ ] T005 Add explicit success/error widget status color resources in app/src/main/res/values/colors.xml
- [ ] T006 Refine status and route domain constraints for rendering assumptions in app/src/main/java/com/platformdash/domain/Departures.kt
- [ ] T007 [P] Align repository determinism expectations with contract scenarios in app/src/main/java/com/platformdash/data/MockTrainRepository.kt
- [ ] T008 Confirm Gradle wrapper/toolchain compatibility with AGP 9.2.1 in gradle/wrapper/gradle-wrapper.properties

**Checkpoint**: Foundation ready. User stories can now be delivered independently.

---

## Phase 3: User Story 1 - Read commute status at a glance (Priority: P1) 🎯 MVP

**Goal**: Show route departures with correct empty state and clear status emphasis, including on-time green and cancelled red.

**Independent Test**: With the default mock scenario active, widget shows route header, departure rows, ON_TIME in green, CANCELLED in red, and empty-state text for no-service scenario.

### Implementation for User Story 1

- [ ] T009 [US1] Implement ON_TIME success-green row styling in app/src/main/java/com/platformdash/widget/CommuteWidget.kt
- [ ] T010 [US1] Keep CANCELLED error-red styling and shared row formatting in app/src/main/java/com/platformdash/widget/CommuteWidget.kt
- [ ] T011 [P] [US1] Ensure mixed-status fixture coverage in default scenario in app/src/main/java/com/platformdash/data/MockTrainRepository.kt
- [ ] T012 [US1] Ensure no-services scenario renders No departures available state in app/src/main/java/com/platformdash/widget/CommuteWidget.kt
- [ ] T013 [US1] Update rendering contract with final status-color mapping language in specs/001-commute-widget/contracts/widget-rendering-contract.md

**Checkpoint**: User Story 1 is independently functional and demoable.

---

## Phase 4: User Story 2 - Adapt content to widget size (Priority: P2)

**Goal**: Render responsive typography and service counts across small, medium, and large widget heights.

**Independent Test**: Resizing widget changes typography scale and service count to 3 (small/medium) and 5 (large) while retaining readable spacing.

### Implementation for User Story 2

- [ ] T014 [US2] Implement or finalize height breakpoint scaling config in app/src/main/java/com/platformdash/widget/CommuteWidget.kt
- [ ] T015 [US2] Apply scaling config to header size, row size, and maxTrains in app/src/main/java/com/platformdash/widget/CommuteWidget.kt
- [ ] T016 [US2] Verify launcher resize metadata and target cell hints in app/src/main/res/xml/commute_widget_info.xml
- [ ] T017 [US2] Align responsive scaling rules and acceptance wording in specs/001-commute-widget/spec.md

**Checkpoint**: User Story 2 is independently functional and verifiable by resizing.

---

## Phase 5: User Story 3 - Respect app theme preferences (Priority: P3)

**Goal**: Ensure widget visuals follow shared theme preference for System, Light, and Dark modes.

**Independent Test**: Changing theme mode in app settings updates widget background and text colors without breaking status color emphasis.

### Implementation for User Story 3

- [ ] T018 [US3] Validate theme preference read/fallback behavior in app/src/main/java/com/platformdash/settings/ThemePreferences.kt
- [ ] T019 [US3] Finalize widget palette mapping for SYSTEM, LIGHT, and DARK in app/src/main/java/com/platformdash/widget/CommuteWidget.kt
- [ ] T020 [P] [US3] Add or refine widget base colors used by SYSTEM mode in app/src/main/res/values/colors.xml
- [ ] T021 [US3] Align settings-host theme controls with widget behavior in app/src/main/java/com/platformdash/settings/SettingsHostScreen.kt

**Checkpoint**: User Story 3 is independently functional through theme switching.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Final consistency and handoff quality across all stories.

- [ ] T022 [P] Reconcile acceptance checks against delivered behavior in specs/001-commute-widget/spec.md
- [ ] T023 Run end-to-end quickstart validation and update any mismatches in specs/001-commute-widget/quickstart.md
- [ ] T024 Document final implementation notes and deferred follow-ups in specs/001-commute-widget/plan.md

---

## Dependencies & Execution Order

### Phase Dependencies

- Setup (Phase 1) starts immediately.
- Foundational (Phase 2) depends on Setup and blocks all user stories.
- User stories (Phases 3-5) depend on Foundational completion.
- Polish (Phase 6) depends on completion of all target user stories.

### User Story Dependencies

- US1 (P1) has no dependency on other stories after Foundational.
- US2 (P2) has no dependency on other stories after Foundational.
- US3 (P3) has no dependency on other stories after Foundational.

### Parallel Opportunities

- Phase 1: T004 can run in parallel with T001-T003.
- Phase 2: T007 can run in parallel with T005-T006.
- US1: T011 can run in parallel with T009-T010.
- US3: T020 can run in parallel with T018-T019.
- Polish: T022 can run in parallel with T023.

---

## Parallel Example: User Story 1

- Task: T009 [US1] Implement ON_TIME success-green row styling in app/src/main/java/com/platformdash/widget/CommuteWidget.kt
- Task: T011 [US1] Ensure mixed-status fixture coverage in default scenario in app/src/main/java/com/platformdash/data/MockTrainRepository.kt

## Parallel Example: User Story 2

- Task: T015 [US2] Apply scaling config to header size, row size, and maxTrains in app/src/main/java/com/platformdash/widget/CommuteWidget.kt
- Task: T016 [US2] Verify launcher resize metadata and target cell hints in app/src/main/res/xml/commute_widget_info.xml

## Parallel Example: User Story 3

- Task: T019 [US3] Finalize widget palette mapping for SYSTEM, LIGHT, and DARK in app/src/main/java/com/platformdash/widget/CommuteWidget.kt
- Task: T020 [US3] Add or refine widget base colors used by SYSTEM mode in app/src/main/res/values/colors.xml

---

## Implementation Strategy

### MVP First (US1 only)

1. Complete Phase 1 Setup.
2. Complete Phase 2 Foundational.
3. Complete Phase 3 User Story 1.
4. Validate independent test for US1 before expanding scope.

### Incremental Delivery

1. Deliver US1 for core commuter value.
2. Deliver US2 for responsive scaling quality.
3. Deliver US3 for theme consistency.
4. Finish with polish tasks for acceptance and handoff.

### Format Validation

All tasks in this file follow the checklist format:
- Checkbox prefix
- Sequential Task ID (T001-T024)
- Optional [P] marker only where parallelizable
- [US#] labels on user-story tasks only
- Explicit file path in every task description

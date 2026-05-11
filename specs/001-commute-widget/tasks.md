# Tasks: Commute Departures Widget

**Input**: Design documents from `/specs/001-commute-widget/`
**Prerequisites**: `plan.md` (required), `spec.md` (required for user stories), `research.md`, `data-model.md`, `contracts/`

**Tests**: This feature spec includes verification steps; dedicated test tasks are optional.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and baseline alignment for this feature.

- [x] T001 Verify AGP 9.2.1 pinning remains in build.gradle.kts and app/build.gradle.kts
- [x] T002 Confirm widget resize metadata in app/src/main/res/xml/commute_widget_info.xml
- [x] T003 [P] Align quickstart verification steps with the current app/widget refresh behavior in specs/001-commute-widget/quickstart.md

**Checkpoint**: Project baseline is ready for user-story work.

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Shared infrastructure and styling primitives required by all user stories.

**⚠️ CRITICAL**: Complete this phase before user-story implementation.

- [x] T004 Replace widget palette tokens with fixed GBR colors in app/src/main/res/values/colors.xml
- [x] T005 Refine route/domain constraints in app/src/main/java/com/platformdash/domain/Departures.kt
- [x] T006 [P] Align repository determinism expectations with contract scenarios in app/src/main/java/com/platformdash/data/MockTrainRepository.kt
- [x] T007 Add theme-mode persistence fallback for Light/Dark only in app/src/main/java/com/platformdash/settings/ThemePreferences.kt
- [x] T008 Confirm wrapper/toolchain compatibility notes for AGP 9.2.1 in gradle/wrapper/gradle-wrapper.properties

**Checkpoint**: Foundation ready. User stories can now be delivered independently.

---

## Phase 3: User Story 1 - Read commute status at a glance (Priority: P1) MVP

**Goal**: Show departures with contrast text and visible cancelled emphasis using fixed GBR colors.

**Independent Test**: With the default mock scenario active, widget shows route header and departure rows with contrast text, CANCELLED in GBR Red (#E60000), and empty-state text for no-service scenario.

### Implementation for User Story 1

- [x] T009 Apply contrast text color for ON_TIME and DELAYED rows in app/src/main/java/com/platformdash/widget/CommuteWidget.kt
- [x] T010 Keep CANCELLED styling fixed to GBR Red (#E60000) in app/src/main/java/com/platformdash/widget/CommuteWidget.kt
- [x] T011 [P] Ensure mixed-status fixture coverage in default scenario in app/src/main/java/com/platformdash/data/MockTrainRepository.kt
- [x] T012 Ensure no-services scenario renders No departures available state in app/src/main/java/com/platformdash/widget/CommuteWidget.kt
- [x] T013 Update rendering contract with final status-color mapping language in specs/001-commute-widget/contracts/widget-rendering-contract.md

**Checkpoint**: User Story 1 is independently functional and demoable.

---

## Phase 4: User Story 2 - Adapt content to widget size (Priority: P2)

**Goal**: Render responsive typography and service counts across small, medium, and large widget heights.

**Independent Test**: Resizing widget changes typography scale and service count to 3 (small/medium) and 5 (large) while retaining readable spacing.

### Implementation for User Story 2

- [x] T014 Implement or finalize height breakpoint scaling config in app/src/main/java/com/platformdash/widget/CommuteWidget.kt
- [x] T015 Apply scaling config to header size, row size, and maxTrains in app/src/main/java/com/platformdash/widget/CommuteWidget.kt
- [x] T016 [P] Verify launcher resize metadata and target cell hints in app/src/main/res/xml/commute_widget_info.xml
- [x] T017 Align responsive scaling rules and acceptance wording in specs/001-commute-widget/spec.md

**Checkpoint**: User Story 2 is independently functional and verifiable by resizing.

---

## Phase 5: User Story 3 - Respect app theme preferences (Priority: P3)

**Goal**: Ensure widget visuals follow strict Light/Dark-only behavior and fixed GBR palette values, with immediate refresh after theme change.

**Independent Test**: Changing theme mode in app settings applies Light or Dark palettes only, renders visible double border in both modes, and updates the widget immediately without closing the app.

### Implementation for User Story 3

- [x] T018 Validate theme preference read/fallback behavior in app/src/main/java/com/platformdash/settings/ThemePreferences.kt
- [x] T019 Finalize widget palette mapping for LIGHT and DARK only in app/src/main/java/com/platformdash/widget/CommuteWidget.kt
- [x] T020 [P] Remove System mode assumptions from theme resources in app/src/main/res/values/colors.xml
- [x] T021 Align settings-host theme controls and GBR styling with widget behavior in app/src/main/java/com/platformdash/settings/SettingsHostScreen.kt
- [x] T022 Implement visible dual-border styling on the root widget container in app/src/main/java/com/platformdash/widget/CommuteWidget.kt

**Checkpoint**: User Story 3 is independently functional through theme switching.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Final consistency and handoff quality across all stories.

- [x] T023 [P] Reconcile acceptance checks against delivered behavior in specs/001-commute-widget/spec.md
- [x] T024 Run end-to-end quickstart validation for immediate theme refresh and border visibility in specs/001-commute-widget/quickstart.md
- [x] T025 Document final implementation notes and deferred follow-ups in specs/001-commute-widget/plan.md
- [x] T026 Increase widget dual-border thickness to 3x baseline in app/src/main/java/com/platformdash/widget/CommuteWidget.kt

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

- Phase 1: T003 can run in parallel with T001-T002.
- Phase 2: T006 can run in parallel with T004-T005.
- US1: T011 can run in parallel with T009-T010.
- US3: T020 can run in parallel with T018-T019.
- Polish: T023 can run in parallel with T024.

---

## Parallel Example: User Story 1

- Task: T009 Apply contrast text color for ON_TIME and DELAYED rows in app/src/main/java/com/platformdash/widget/CommuteWidget.kt
- Task: T011 [P] Ensure mixed-status fixture coverage in default scenario in app/src/main/java/com/platformdash/data/MockTrainRepository.kt

## Parallel Example: User Story 2

- Task: T015 Apply scaling config to header size, row size, and maxTrains in app/src/main/java/com/platformdash/widget/CommuteWidget.kt
- Task: T016 [P] Verify launcher resize metadata and target cell hints in app/src/main/res/xml/commute_widget_info.xml

## Parallel Example: User Story 3

- Task: T019 Finalize widget palette mapping for LIGHT and DARK only in app/src/main/java/com/platformdash/widget/CommuteWidget.kt
- Task: T020 [P] Remove System mode assumptions from theme resources in app/src/main/res/values/colors.xml

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
3. Deliver US3 for strict GBR theme consistency and immediate widget refresh.
4. Finish with polish tasks for acceptance and handoff.

### Format Validation

All tasks in this file follow the checklist format:
- Checkbox prefix
- Sequential Task ID (T001-T025)
- Optional [P] marker only where parallelizable
- [US#] labels on user-story tasks only
- Explicit file path in every task description

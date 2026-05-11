# Phase 0 Research: Commute Departures Widget

## Decision 0: Pin build tooling to AGP 9.2.1 for this feature
- Decision: Standardize the feature's Android build baseline on AGP 9.2.1 and align wrapper/toolchain checks in setup tasks.
- Rationale: The feature specification now explicitly requires AGP 9.2.1, so planning artifacts must encode that baseline to avoid drift during implementation.
- Alternatives considered: Keep existing AGP version without pinning; rejected because it conflicts with the technical baseline requirement and weakens reproducibility.

## Decision 1: Status-to-color mapping includes explicit success green for on-time
- Decision: Render CANCELLED as Material error red and ON_TIME as success green in widget rows.
- Rationale: The spec now requires immediate visual distinction for both disrupted and safe services; red/green mapping is fast to parse in commute contexts.
- Alternatives considered: Keep non-cancelled rows in default text color; rejected because ON_TIME would not be explicitly signaled and would fail new acceptance criteria.

## Decision 2: Keep MockTrainRepository as sole data source for this increment
- Decision: Continue using TrainRepository + MockTrainRepository with deterministic scenarios and no live API calls.
- Rationale: Satisfies mock-first architecture, keeps widget behavior testable/deterministic, and avoids introducing credential and network failure paths in this increment.
- Alternatives considered: Integrate a live UK rail API now; rejected because feature scope explicitly excludes live API integration.

## Decision 3: Preserve exact-size responsive scaling by widget height
- Decision: Keep SizeMode.Exact and enforce top-3 (small/medium) and top-5 (large) service limits by height breakpoints.
- Rationale: Aligns with existing implementation and spec requirements; keeps dense but readable glance behavior.
- Alternatives considered: Responsive width-based scaling only; rejected because current widget behavior and requirements are height driven.

## Decision 4: Validate with Android unit/instrumentation stack already declared in Gradle
- Decision: Use JUnit4 for unit logic and AndroidX Compose instrumentation tests for rendering assertions as tasks are implemented.
- Rationale: Tooling already exists in dependencies and matches Android Compose/Glance verification needs.
- Alternatives considered: Add additional test frameworks now; rejected to avoid unnecessary tooling churn during plan phase.

## Decision 5: Defer WorkManager refresh implementation but preserve refresh contract intent
- Decision: Keep widget provider updatePeriodMillis at 0 for this increment and track WorkManager degraded-mode behavior as a follow-on requirement.
- Rationale: Out-of-scope in current spec, while constitution still requires explicit planning for reliable refresh behavior.
- Alternatives considered: Implement WorkManager scheduling in this increment; rejected because this plan targets widget display behavior and mock data flow only.

## Decision 6: Keep theme preference integration as current shared setting source
- Decision: Continue reading shared theme mode (System/Light/Dark) from existing preference path for widget color selection.
- Rationale: Meets current functional requirements and avoids introducing unrelated storage migration risk in this increment.
- Alternatives considered: Migrate theme preferences to DataStore immediately; rejected because this is orthogonal to current widget behavior goals.

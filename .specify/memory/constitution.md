<!--
Sync Impact Report
- Version change: template-placeholder -> 1.0.0
- Modified principles:
	- [PRINCIPLE_1_NAME] -> I. Android Widget Architecture First
	- [PRINCIPLE_2_NAME] -> II. BYOK Security and Secret Handling
	- [PRINCIPLE_3_NAME] -> III. Commute-Centric Experience
	- [PRINCIPLE_4_NAME] -> IV. Mock-First Repository Contract
	- [PRINCIPLE_5_NAME] -> V. Reliable Background Refresh
- Added sections:
	- Technical Standards and Product Constraints
	- Delivery Workflow and Quality Gates
- Removed sections:
	- None
- Templates requiring updates:
	- ✅ updated .specify/templates/plan-template.md
	- ✅ updated .specify/templates/spec-template.md
	- ✅ updated .specify/templates/tasks-template.md
	- ✅ no files found under .specify/templates/commands/*.md (nothing to update)
	- ✅ updated README.md
- Follow-up TODOs:
	- None
-->

# PlatformDash Constitution

## Core Principles

### I. Android Widget Architecture First
All commuter-facing widget functionality MUST be implemented with Jetpack Glance,
and all in-app screens MUST use Jetpack Compose with Material 3 components and
theming. New UI work that bypasses this architecture is non-compliant unless a
documented exception is approved through Governance.
Rationale: A single Android-native stack reduces integration risk and preserves
UI consistency across widget and app surfaces.

### II. BYOK Security and Secret Handling
API credentials MUST NEVER be hardcoded in source code, tests, build scripts,
or sample configuration files. Users MUST provide their own API key through app
settings, and that key MUST be persisted via Jetpack DataStore using platform-
appropriate secure storage patterns. Logs, analytics, and crash reports MUST
exclude raw key values.
Rationale: BYOK protects user-controlled credentials and reduces leakage risk in
repositories, CI logs, and distributed artifacts.

### III. Commute-Centric Experience
The default commute route MUST be LBG to RDH while allowing users to configure
origin and destination stations. The widget MUST expose a direct Swap Direction
action. Primary information hierarchy MUST prioritize Time to Departure and
Platform Number. Cancelled services MUST be visually emphasized in red using
Material 3-compliant contrast.
Rationale: The product succeeds only if critical commute decisions can be made
at a glance under time pressure.

### IV. Mock-First Repository Contract
Data access MUST be abstracted behind a TrainRepository interface before any
external rail API integration. Development MUST ship an operational
MockTrainRepository with deterministic dummy data until the UK rail API choice
is finalized. API-specific logic MUST remain behind the repository boundary.
Rationale: Contract-first design decouples UI progress from provider decisions
and enables stable tests during early iterations.

### V. Reliable Background Refresh
Background updates MUST be scheduled through WorkManager and tuned for battery-
aware execution constraints. Refresh behavior MUST degrade gracefully when
network or key configuration is unavailable, while preserving last known valid
widget state and communicating stale data clearly.
Rationale: Commute data loses value without reliable freshness, but aggressive
polling harms device health and user trust.

## Technical Standards and Product Constraints

- Material 3 is the required design system baseline for typography, spacing,
	components, and state feedback.
- Widget actions MUST remain functional without opening the app when feasible,
	especially route direction swaps.
- API integration scope is intentionally limited to one UK rail provider in the
	first production integration.
- Performance work MUST include measurable refresh latency and battery-impact
	considerations in plan and task artifacts.
- Cancellation, platform changes, and unavailable data states MUST have explicit
	display rules in specs before implementation begins.

## Delivery Workflow and Quality Gates

- Every feature plan MUST include a Constitution Check that explicitly verifies
	compliance with all five core principles.
- Every feature spec MUST include acceptance scenarios for default route,
	configurable stations, swap direction behavior, and cancellation highlighting.
- Implementation tasks MUST include work items for repository abstraction,
	mock data wiring, secure key flow, widget refresh scheduling, and UX state
	handling for degraded data conditions.
- Commits MUST follow the gitmoji.dev convention to maintain a scannable,
	professional history.
- Pull request review MUST reject changes that violate architecture, security,
	or commute-priority rules even if functionality appears correct.

## Governance

This constitution is the highest-priority project policy for product and
engineering decisions in this repository.

Amendment procedure:
- Propose amendments in a pull request that includes impacted principles,
	migration implications, and template sync updates.
- Obtain approval from at least one maintainer responsible for product scope
	and one maintainer responsible for Android implementation quality.
- Update all affected templates and runtime guidance in the same change set.

Versioning policy:
- MAJOR: Removes or fundamentally redefines a principle or governance contract.
- MINOR: Adds a new principle/section or materially expands required practices.
- PATCH: Clarifications, wording improvements, or non-semantic refinements.

Compliance review expectations:
- Constitution compliance MUST be checked at plan creation, task generation,
	and pull request review.
- Non-compliant work MUST be corrected before merge unless an approved,
	time-bounded exception is documented in the relevant spec artifacts.

**Version**: 1.0.0 | **Ratified**: 2026-05-09 | **Last Amended**: 2026-05-09

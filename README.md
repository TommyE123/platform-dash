# platform-dash
A real-time UK rail departure widget for Android, built with Jetpack Glance. Features a Bring-Your-Own-Key (BYOK) model for secure, battery-efficient commute tracking.

## Governance Highlights

- Widget UI is built with Jetpack Glance; app UI uses Jetpack Compose with
	Material 3.
- API keys are BYOK only: users enter keys in app settings; keys are never
	hardcoded and must be persisted securely via Jetpack DataStore patterns.
- Commute UX prioritizes time-to-departure and platform number, supports
	configurable stations, defaults to LBG->RDH, and includes widget direction
	swap.
- Data integration is mock-first through a TrainRepository contract and
	MockTrainRepository until a live UK rail API is finalized.
- Background data refresh is managed with WorkManager.
- Commit messages follow gitmoji.dev conventions.

For the authoritative policy, see .specify/memory/constitution.md.

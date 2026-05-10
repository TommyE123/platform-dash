# Implementation Plan: Commute Departures Widget (LBG to RDH)

**Branch**: 001-lbg-rdh-glance-widget
**Date**: 2026-05-10
**Spec**: spec.md
**Constitution**: .specify/memory/constitution.md

## Summary

Implement an Android commute widget with Jetpack Glance using a mock-first TrainRepository contract, defaulting to London Bridge (LBG) to Redhill (RDH), with configurable stations, widget swap direction action, BYOK key flow, and reliable WorkManager refresh behavior with stale-data fallback.

## Mandatory Build And Toolchain Rules

These are non-negotiable for this feature:

1. Root-first setup is mandatory:
- Create root settings.gradle.kts and root build.gradle.kts before any app-level scaffolding.
- Include :app in settings.gradle.kts during Phase 1.
- Do not create app directory content until Phase 2 begins.

2. Strict versioning is mandatory:
- gradle/wrapper/gradle-wrapper.properties MUST contain:
  distributionUrl=https\://services.gradle.org/distributions/gradle-9.5.0-bin.zip
- Root build.gradle.kts MUST declare:
  - com.android.application version 9.2.0 (apply false)
  - com.android.library version 9.2.0 (apply false)

3. AGP 9.0+ Kotlin rule is mandatory:
- Do NOT apply org.jetbrains.kotlin.android in any build.gradle.kts file.
- Do NOT generate kotlinOptions blocks.
- Java compatibility configuration must rely on compileOptions with Java 17.

## Technical Context

- Language/Version: Kotlin source in app module, Java 17 bytecode target
- Build System: Gradle Kotlin DSL
- Gradle Wrapper: 9.5.0
- Android Gradle Plugin: 9.2.0
- Primary Dependencies: AndroidX Glance, Jetpack Compose, Material 3, WorkManager, DataStore
- Storage: DataStore for route preferences and BYOK key state
- Testing: JUnit, instrumented Android tests, repository contract tests, widget behavior tests
- Target Platform: Android app plus home screen widget

## Constitution Check

- Architecture Gate: Use Jetpack Glance for widget surfaces and Jetpack Compose + Material 3 for app UI.
- Security Gate: Enforce BYOK data flow, persist securely via DataStore, and forbid hardcoded secrets.
- Commute UX Gate: Default LBG to RDH, support configurable origin/destination, include widget swap action, and emphasize cancellations in red.
- Data Contract Gate: Define TrainRepository first and ship MockTrainRepository before any live provider integration.
- Refresh Gate: Use WorkManager for background updates and preserve last known valid state with stale-state communication.
- History Gate: Maintain gitmoji.dev commit convention.

## Step-by-Step Delivery Plan

## Phase 1: Root Environment Setup

Phase objective: establish root build environment and version locks before any app-level creation.

1. Create root settings.gradle.kts.
- Required contents:
  - pluginManagement repositories (google, mavenCentral, gradlePluginPortal)
  - dependencyResolutionManagement repositories (google, mavenCentral)
  - rootProject.name = "platform-dash"
  - include(":app")

2. Create root build.gradle.kts.
- Required contents:
  - plugins block with:
    - id("com.android.application") version "9.2.0" apply false
    - id("com.android.library") version "9.2.0" apply false
- Constraints:
  - No org.jetbrains.kotlin.android plugin
  - No module-level Android configuration in root file

3. Create gradle/wrapper/gradle-wrapper.properties.
- Required contents:
  - distributionUrl=https\://services.gradle.org/distributions/gradle-9.5.0-bin.zip
  - Standard wrapper keys for distributionBase/distributionPath/zipStoreBase/zipStorePath

4. Validate Phase 1 root files.
- Verification checklist:
  - settings.gradle.kts resolves with :app include present
  - root build.gradle.kts has AGP 9.2.0 application/library plugin declarations
  - wrapper distribution URL matches required 9.5.0 value exactly

5. Phase boundary enforcement.
- Rule:
  - No app-level file creation before completion of steps 1 through 4.

## Phase 2: App Module Bootstrap

Phase objective: scaffold app module only after Phase 1 completion.

1. Create app/build.gradle.kts.
- Required configuration:
  - Apply only com.android.application plugin
  - Do not apply org.jetbrains.kotlin.android
  - No kotlinOptions block
  - compileOptions set sourceCompatibility and targetCompatibility to JavaVersion.VERSION_17
  - buildFeatures.compose = true
  - namespace, compileSdk, minSdk, targetSdk, buildTypes

2. Create app/src/main/AndroidManifest.xml.
- Required configuration:
  - Application declaration
  - Launcher activity declaration for baseline settings host

3. Create baseline Compose Material 3 settings host.
- Required files:
  - Main activity entry point
  - Composable settings host screen shell
- Required behavior:
  - Displays route configuration placeholders for origin and destination
  - Displays BYOK API key input placeholder
  - Uses Material 3 components

4. Compile-level sanity check for module bootstrap.
- Verification checklist:
  - No Gradle sync errors introduced by app module scaffold
  - No plugin violations against AGP/Kotlin rule

## Phase 3: Domain Contract And Mock-First Data Layer

1. Define entities aligned to specification.
- RoutePreference
- DepartureService
- DeparturesSnapshot
- WidgetViewState
- ApiKeyConfig

2. Define TrainRepository contract.
- Route-aware departure query API
- Stable interface independent of provider-specific details

3. Implement MockTrainRepository.
- Deterministic fixtures for:
  - on-time services
  - delayed services
  - cancelled services
  - no-service window

4. Add repository contract tests.
- Validate deterministic outputs and status mapping

## Phase 4: Widget MVP (P1)

1. Build Glance widget layout and state rendering.
2. Wire default route behavior to LBG to RDH when preferences are absent.
3. Prioritize time-to-departure and platform in visual hierarchy.
4. Render cancellation status in red with compliant contrast.
5. Add MVP tests for default route and critical fields.

## Phase 5: Route Configuration And Swap Direction (P2)

1. Implement route settings persistence via DataStore.
2. Connect widget state to configured origin/destination.
3. Implement direct swap-direction widget action without app launch.
4. Add tests for configured route override and swap reversibility.

## Phase 6: BYOK Security And Refresh Reliability (P3)

1. Implement BYOK capture and secure key-state persistence.
2. Add hardcoded-secret prevention checks in source and config workflow.
3. Implement WorkManager periodic refresh with battery-aware constraints.
4. Implement stale-data fallback to last known valid snapshot.
5. Add offline and missing-key degradation tests.

## Phase 7: Quality Gates And Release Readiness

1. Run unit, integration, and instrumentation suites.
2. Execute constitution compliance audit against all five principles.
3. Verify measurable outcomes from specification acceptance criteria.
4. Update README and delivery notes with architecture and security behavior.

## Dependencies And Execution Order

1. Phase 1 is mandatory and blocking for all later phases.
2. Phase 2 starts only after Phase 1 verification is complete.
3. Phase 3 must complete before Phase 4 widget integration.
4. Phase 4 (MVP) unlocks demo-ready baseline value.
5. Phase 5 and Phase 6 may run in parallel after Phase 4.
6. Phase 7 closes implementation and readiness.

## Risks And Mitigations

- Risk: Toolchain drift from required AGP/Gradle versions.
- Mitigation: Enforce explicit root file checks in Phase 1 and CI preflight.

- Risk: Kotlin plugin accidentally applied despite AGP 9.2.0 rule.
- Mitigation: Add rule check in review checklist and Gradle file linting.

- Risk: Widget reliability variance under OEM battery constraints.
- Mitigation: Conservative WorkManager policies and stale-state UX coverage.

- Risk: Secret leakage through logs or samples.
- Mitigation: BYOK redaction policy and repository scans for key patterns.

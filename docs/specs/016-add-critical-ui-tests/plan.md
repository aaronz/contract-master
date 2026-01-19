# Implementation Plan: Add Critical UI Test Cases

**Branch**: `016-add-critical-ui-tests` | **Date**: 2025-01-19 | **Spec**: [/specs/016-add-critical-ui-tests/spec.md]
**Input**: Feature specification from `/specs/016-add-critical-ui-tests/spec.md`

## Summary

This feature implements the critical UI test cases defined in the specification, establishing the automated testing baseline for Contract Master. It executes the strategy defined in `010-ui-test-suite` by creating the actual Page Object Models (POMs) and JUnit 5 test scenarios for Contract Lifecycle, Problem Discovery, and Rule Management. The solution leverages Java Playwright bindings to integrate seamlessly with the backend build process.

## Technical Context

**Language/Version**: Java 17
**Primary Dependencies**: Playwright 1.41.2, JUnit 5, AssertJ
**Storage**: N/A (Tests utilize running application state)
**Testing**: Playwright (Browser Automation), JUnit 5 (Test Runner)
**Target Platform**: Linux/MacOS (CI/CD and local development)
**Project Type**: web application
**Performance Goals**: Critical path suite execution < 5 minutes
**Constraints**: Must run in headless mode for CI; Tests must be independent (no shared state)
**Scale/Scope**: 3 prioritized user stories, approx. 15-20 test cases

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] **I. Tenant Isolation**: Tests explicitly use distinct browser contexts to verify isolation.
- [x] **II. AI-Manual Synergy**: Tests verify the display of AI-sourced data.
- [x] **III. Rule Governance**: Tests trigger and verify rule evaluations in the UI.
- [x] **IV. Middleware Std**: N/A (Internal testing).
- [x] **V. Auditability**: Tests verify audit log entries are visible.
- [x] **VI. E2E Testing**: This IS the implementation of E2E testing.
- [x] **VII. Doc Sync**: Plan includes updating `010-ui-test-suite` docs if divergences occur.
- [x] **VIII. UX/Completeness**: Tests cover full user journeys.
- [x] **IX. Manifest Sync**: Features manifest will be updated.

## Project Structure

### Documentation (this feature)

```text
specs/016-add-critical-ui-tests/
├── plan.md              # This file
├── research.md          # Phase 0 output (Inherited from 010)
├── data-model.md        # Phase 1 output (Test Data Factories)
├── quickstart.md        # Phase 1 output (Test Execution Guide)
├── contracts/           # Phase 1 output (Page Object Definitions)
└── tasks.md             # Phase 2 output
```

### Source Code (repository root)

```text
backend/
├── src/
│   └── test/
│       └── java/com/contract/master/
│           ├── e2e/
│           │   ├── pages/       # LoginPO, ContractListPO, ContractDetailPO, ProblemCenterPO
│           │   ├── scenarios/   # ContractLifecycleTest, RiskDiscoveryTest, RuleManagementTest
│           │   └── utils/       # BrowserFactory, TestDataGenerator
│           └── E2ETestBase.java  # Base class for Playwright setup
```

**Structure Decision**: Option 2: Web application. Integrating tests into `backend/src/test` allows sharing DTOs and utilities with the backend while driving the frontend browser interactions.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| None | | |

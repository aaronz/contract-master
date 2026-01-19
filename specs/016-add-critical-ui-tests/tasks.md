# Implementation Tasks: Add Critical UI Test Cases

**Feature**: Add Critical UI Test Cases
**Branch**: `016-add-critical-ui-tests`
**Spec**: [/specs/016-add-critical-ui-tests/spec.md]

## Implementation Strategy

This feature implements the automated UI testing infrastructure for Contract Master using Java Playwright. The strategy follows a strict Page Object Model (POM) pattern to ensure maintainability. We will build the infrastructure layer first, then implement the Page Objects, and finally the Test Scenarios corresponding to the user stories. Tests will be self-contained, using API calls to seed data before verifying UI behavior.

**Key Decisions**:
- **Framework**: Java Playwright + JUnit 5 (matches backend stack)
- **Data Strategy**: API-driven seeding (no shared DB state)
- **Browser**: Headless Chromium (CI-ready)
- **Structure**: Tests reside in `backend/src/test/java/com/contract/master/e2e/`

## Phase 1: Setup & Infrastructure

**Goal**: Establish the testing framework and base classes required for running Playwright tests within the Maven lifecycle.

- [x] T001 [P] Configure Maven dependencies for Playwright and AssertJ in `backend/pom.xml`
- [x] T002 [P] Create E2E test package structure in `backend/src/test/java/com/contract/master/e2e/`
- [x] T003 Create `E2ETestBase.java` in `backend/src/test/java/com/contract/master/e2e/` to handle BrowserContext lifecycle and configuration
- [x] T004 Implement `BrowserFactory` utility in `backend/src/test/java/com/contract/master/e2e/utils/BrowserFactory.java` for managing headless/headed modes
- [x] T005 Implement `TestDataGenerator` utility in `backend/src/test/java/com/contract/master/e2e/utils/TestDataGenerator.java` for API-based data seeding

## Phase 2: Foundational Page Objects

**Goal**: Implement the core Page Objects (POM) that are shared across multiple user stories (Login, Dashboard, Navigation).

- [x] T006 [P] Implement `LoginPO` in `backend/src/test/java/com/contract/master/e2e/pages/LoginPO.java` with login actions
- [x] T007 [P] Implement `DashboardPO` in `backend/src/test/java/com/contract/master/e2e/pages/DashboardPO.java` with navigation methods
- [x] T008 [P] Implement `ContractListPO` in `backend/src/test/java/com/contract/master/e2e/pages/ContractListPO.java` with list filtering/search methods
- [x] T009 [P] Implement `ContractDetailPO` in `backend/src/test/java/com/contract/master/e2e/pages/ContractDetailPO.java` with form interaction methods

## Phase 3: Contract Lifecycle Verification (P1)

**Goal**: Automate the full contract creation and editing workflow (User Story 1).

**Independent Test Criteria**: Run `ContractLifecycleTest` - verified if creates contract, edits amount, and verifies list update.

- [x] T010 [P] [US1] Create `ContractLifecycleTest` class in `backend/src/test/java/com/contract/master/e2e/scenarios/ContractLifecycleTest.java`
- [x] T011 [US1] Implement test case `testContractCreation` using `ContractListPO` and `ContractDetailPO`
- [x] T012 [US1] Implement test case `testContractEditing` to verify data persistence
- [x] T013 [US1] Implement test case `testDraftFiltering` to verify list filtering

## Phase 4: Problem Discovery & Resolution (P2)

**Goal**: Automate the risk evaluation and problem resolution workflow (User Story 2).

**Independent Test Criteria**: Run `RiskDiscoveryTest` - verified if triggers evaluation and finds resulting problem.

- [x] T014 [P] [US2] Implement `ProblemCenterPO` in `backend/src/test/java/com/contract/master/e2e/pages/ProblemCenterPO.java`
- [x] T015 [US2] Update `ContractDetailPO` to include `triggerAIAnalysis` and `runEvaluation` methods if missing
- [x] T016 [US2] Create `RiskDiscoveryTest` class in `backend/src/test/java/com/contract/master/e2e/scenarios/RiskDiscoveryTest.java`
- [x] T017 [US2] Implement test case `testRuleViolationDetection` (Seed risky contract -> Trigger Eval -> Verify Problem)
- [x] T018 [US2] Implement test case `testProblemResolution` (Resolve problem -> Verify Status)

## Phase 5: Rule Management Integrity (P3)

**Goal**: Automate the rule definition workflow (User Story 3).

**Independent Test Criteria**: Run `RuleManagementTest` - verified if new rule appears in library.

- [x] T019 [P] [US3] Implement `RuleManagementPO` in `backend/src/test/java/com/contract/master/e2e/pages/RuleManagementPO.java`
- [x] T020 [US3] Create `RuleManagementTest` class in `backend/src/test/java/com/contract/master/e2e/scenarios/RuleManagementTest.java`
- [x] T021 [US3] Implement test case `testRuleCreation` (Create Rule -> Verify List)
- [x] T022 [US3] Implement test case `testRuleUpdate` (Update Rule -> Verify Changes)

## Phase 6: Polish & CI Integration

**Goal**: Finalize the suite for CI execution and ensure robustness.

- [x] T023 Configure `maven-surefire-plugin` to support `PLAYWRIGHT_HEADLESS` environment variable
- [x] T024 Create `run-e2e-tests.sh` script in project root for easy execution
- [x] T025 Verify all tests pass in a clean environment (full regression run)

## Dependencies

- **Phase 1 (Setup)** is required for ALL subsequent phases.
- **Phase 2 (POM)** is required for all Test Scenarios.
- **Phase 3, 4, 5** can be implemented in parallel once Phase 2 is complete.

## Parallel Execution Examples

- **Developer A**: Implements T006-T009 (Page Objects)
- **Developer B**: Implements T010-T013 (Contract Lifecycle Tests) once POMs are stubbed
- **Developer C**: Implements T014-T018 (Risk Discovery Tests) once POMs are stubbed

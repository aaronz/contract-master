# Tasks: Comprehensive UI Test Suite

**Input**: Design documents from `/specs/001-ui-test-suite/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/selectors.md

**Tests**: This project *is* the test suite implementation. It uses Playwright with Java bindings.

**Organization**: Tasks are grouped by user story to ensure incremental, testable progress.

## Phase 1: Setup (Infrastructure & POM Base)

**Purpose**: Establish the testing framework and base Page Objects.

- [x] T001 Initialize testing directory structure in backend/src/test/java/com/contract/master/e2e/
- [x] T002 Implement `E2ETestBase` with multi-context browser management in backend/src/test/java/com/contract/master/e2e/E2ETestBase.java
- [x] T003 [P] Implement `LoginPage` object using POM pattern in backend/src/test/java/com/contract/master/e2e/pages/LoginPage.java
- [x] T004 [P] Implement `DashboardPage` object in backend/src/test/java/com/contract/master/e2e/pages/DashboardPage.java
- [x] T005 Create `TestDataClient` for API-driven state setup in backend/src/test/java/com/contract/master/e2e/utils/TestDataClient.java

## Phase 2: User Story 1 - Authentication and Tenant Access (Priority: P1)

**Goal**: Validate secure login and tenant isolation entry.

- [x] T006 [US1] Implement `US1AuthTest` class in backend/src/test/java/com/contract/master/e2e/scenarios/US1AuthTest.java
- [x] T007 [US1] Test Case: Successful login redirects to Dashboard with correct tenant context
- [x] T008 [US1] Test Case: Invalid credentials display error message
- [x] T009 [US1] Test Case: Cross-tenant login attempt failure

## Phase 3: User Story 2 - Contract Management Lifecycle (Priority: P1)

**Goal**: Validate contract navigation, search, and detail viewing.

- [x] T010 [P] [US2] Implement `ContractListPage` and `ContractDetailPage` objects in backend/src/test/java/com/contract/master/e2e/pages/
- [x] T011 [US2] Implement `US2ContractManagementTest` in backend/src/test/java/com/contract/master/e2e/scenarios/US2ContractManagementTest.java
- [x] T012 [US2] Test Case: Search contracts by number filters table correctly
- [x] T013 [US2] Test Case: View contract detail displays all base fields (Party A, Party B, Amount)
- [ ] T014 [US2] Test Case: Verify data masking principle applies to UI (e.g., hidden sensitive info)

## Phase 4: User Story 3 - AI Extraction and Field Verification (Priority: P2)

**Goal**: Validate AI-triggered updates and manual overrides.

- [x] T015 [US3] Implement `US3AiExtractionTest` in backend/src/test/java/com/contract/master/e2e/scenarios/US3AiExtractionTest.java
- [x] T016 [US3] Test Case: Trigger AI Analysis shows progress bar and completes with field updates
- [ ] T017 [US3] Test Case: Manual override of AI-extracted field persists after save
- [ ] T018 [US3] Test Case: Verify audit history tab displays the modification record

## Phase 5: User Story 4 - Multi-tenant Configuration (Priority: P3)

**Goal**: Validate dynamic field rendering and rule-based alerts.

- [x] T019 [P] [US4] Implement `SettingsPage` object in backend/src/test/java/com/contract/master/e2e/pages/SettingsPage.java
- [x] T020 [US4] Implement `US4ConfigurationTest` in backend/src/test/java/com/contract/master/e2e/scenarios/US4ConfigurationTest.java
- [x] T021 [US4] Test Case: Adding custom field in Settings makes it appear in Contract Detail
- [ ] T022 [US4] Test Case: Enabling Drools rule triggers visual alert on violating contracts

## Phase 6: Polish & CI/CD Integration

**Purpose**: Finalize suite stability and execution performance.

- [x] T023 [P] Implement screenshot capture on test failure in `E2ETestBase.java`
- [x] T024 Configure Maven `failsafe` plugin for clean E2E test execution in backend/pom.xml
- [x] T025 [VII] Synchronize design documents (spec.md, plan.md) with implemented test coverage
- [x] T026 [VIII] Final review of test logging and readability

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: BLOCKS all user stories. Must establish POM base first.
- **US1 & US2 (Phases 2-3)**: High priority. Can run in parallel once Setup is complete.
- **US3 & US4 (Phases 4-5)**: Depends on stability of US2 page objects.
- **Polish (Phase 6)**: Final optimization.

### Parallel Opportunities

- T003 and T004 (Basic Page Objects) can be implemented simultaneously.
- T010 (Advanced Page Objects) can be done in parallel with US1 tests.
- Different US test classes (T006, T011, T015, T020) can be developed by different members if infrastructure is stable.

## Implementation Strategy

### MVP First (User Story 1 & 2)

1. Complete Infrastructure setup.
2. Implement Auth and Navigation tests.
3. **STOP and VALIDATE**: Run `mvn test` to ensure the core journey passes in headless mode.

### Incremental Delivery

1. Add AI Extraction tests (US3).
2. Add Configuration and Dynamic UI tests (US4).
3. Finalize Polish and failure reporting.

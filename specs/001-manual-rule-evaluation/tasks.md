# Tasks: Manual Rule Evaluation

**Branch**: `001-manual-rule-evaluation` | **Date**: 2026-01-16
**Source Plan**: [plan.md](plan.md)

This task list is generated from the implementation plan and feature specification. Tasks are organized into phases, with each user story treated as an independently testable increment.

---

## Phase 1: Setup

*Goal: Create the basic directory structure for the new feature.*

- [X] T001 Create backend directories for evaluation feature in `backend/src/main/java/com/contractmaster/evaluation/`
- [X] T002 Create backend directories for problem center in `backend/src/main/java/com/contractmaster/problemcenter/`
- [X] T003 Create frontend page for Problem Center at `frontend/src/pages/ProblemCenter.vue`
- [X] T004 Create frontend component for Contract Selector at `frontend/src/components/ContractSelectorModal.vue`
- [X] T005 Create frontend service file for evaluation API at `frontend/src/services/evaluationApi.js`

---

## Phase 2: Foundational (Backend)

*Goal: Implement the core data models and services required for all user stories.*

- [X] T006 [P] Define `EvaluationJob` entity in `backend/src/main/java/com/contractmaster/evaluation/model/EvaluationJob.java` based on `data-model.md`.
- [X] T007 [P] Define `EvaluationResult` entity in `backend/src/main/java/com/contractmaster/evaluation/model/EvaluationResult.java` based on `data-model.md`.
- [X] T008 [P] Create `EvaluationJobRepository` in `backend/src/main/java/com/contractmaster/evaluation/repository/EvaluationJobRepository.java`.
- [X] T009 [P] Create `EvaluationResultRepository` in `backend/src/main/java/com/contractmaster/evaluation/repository/EvaluationResultRepository.java`.
- [X] T010 Setup Kafka producer for evaluation jobs in a new `KafkaProducerService.java`.
- [X] T011 Setup Kafka consumer to process evaluation jobs in `EvaluationService.java`.

---

## Phase 3: User Story 1 (Trigger Single Rule)

*Goal: Implement the workflow for triggering a single rule and viewing its results.*
*Independent Test: A user can select a single rule, choose contracts, trigger evaluation, and see the result in the Problem Center.*

- [X] T012 [US1] Backend: Implement `POST /api/evaluations` endpoint in `EvaluationController.java` to handle a single rule ID.
- [X] T013 [US1] Backend: Implement `EvaluationService.java` to receive a single rule job, and publish it to Kafka.
- [X] T014 [US1] Backend: Implement `GET /api/contracts` endpoint to list contracts for selection.
- [X] T015 [P] [US1] Backend: Implement `GET /api/problem-center/evaluation-jobs` in `ProblemCenterController.java`.
- [X] T016 [P] [US1] Backend: Implement `GET /api/problem-center/evaluation-jobs/{jobId}/results` in `ProblemCenterController.java`.
- [X] T017 [P] [US1] Frontend: Add "Trigger Evaluation" button to `frontend/src/pages/RuleEditor.vue`.
- [X] T018 [US1] Frontend: Implement `ContractSelectorModal.vue` to fetch and display contracts from the API.
- [X] T019 [US1] Frontend: Connect the "Trigger Evaluation" button to show the modal and call the `POST /api/evaluations` endpoint on submission.
- [X] T020 [US1] Frontend: Implement `ProblemCenter.vue` to fetch and display the list of evaluation jobs.
- [X] T021 [US1] Frontend: Implement the view in `ProblemCenter.vue` to show detailed results when a job is selected.

---

## Phase 4: User Story 2 (Trigger Batch of Rules)

*Goal: Extend the feature to support triggering multiple rules at once.*
*Independent Test: A user can select multiple rules, choose contracts, trigger evaluation, and see all results in the Problem Center.*

- [X] T022 [US2] Backend: Extend `POST /api/evaluations` endpoint in `EvaluationController.java` to accept an array of rule IDs.
- [X] T023 [US2] Backend: Update `EvaluationService.java` to handle creating jobs for multiple rules from a single request.
- [X] T024 [P] [US2] Frontend: Add checkboxes to the rule list page to allow multi-selection.
- [X] T025 [P] [US2] Frontend: Add a "Trigger Batch Evaluation" button that appears when rules are selected.
- [X] T026 [US2] Frontend: Connect the batch button to show `ContractSelectorModal.vue` and call the `POST /api/evaluations` endpoint with multiple rule IDs.

---

## Phase 5: User Story 3 (View Automatic Triggers)

*Goal: Display information about automatic rule triggers.*
*Independent Test: A user can find and read the documentation on automatic triggers within the UI.*

- [X] T027 [US3] Backend: Implement `GET /api/rules/trigger-scenarios` endpoint. This can return a static list of scenarios for now.
- [X] T028 [US3] Frontend: Add a "Help" or "Info" icon/button on the rule list page.
- [X] T029 [US3] Frontend: On click, have the button display the trigger scenarios fetched from the API in a modal or popover.

---

## Phase 6: Polish & Cross-Cutting Concerns

*Goal: Add tests, update documentation, and ensure compliance with project standards.*

- [X] T030 [P] Add unit tests for `EvaluationService.java`.
- [X] T031 [P] Add unit tests for the new components in `frontend/`.
- [X] T032 Add E2E tests for the user journeys defined in `quickstart.md`.
- [X] T033 Implement audit logging for the manual trigger event in `EvaluationService.java`.
- [X] T034 Update global manifest `features.md` with the new feature.
- [X] T035 Update global manifest `api.md` with the new endpoints.
- [X] T036 Update global manifest `table.md` with the new `EvaluationJob` and `EvaluationResult` tables.

---

## Dependency Graph

- **Phase 1 (Setup)** -> **Phase 2 (Foundational)**
- **Phase 2 (Foundational)** -> **Phase 3 (US1)**
- **Phase 3 (US1)** -> **Phase 4 (US2)** (US2 depends on the UI and services built for US1)
- **Phase 2 (Foundational)** -> **Phase 5 (US3)** (US3 is largely independent but benefits from the established structure)
- **All Phases** -> **Phase 6 (Polish)**

## Parallel Execution

- Within each phase, tasks marked with **[P]** can be worked on in parallel.
- **Phase 3 (US1)** and **Phase 5 (US3)** can be worked on in parallel by different developers (one on the core feature, one on the documentation part).

## Implementation Strategy

The recommended approach is to implement one user story at a time, following the phase order. This ensures that a testable, valuable increment is delivered with each phase. The MVP (Minimum Viable Product) for this feature is the completion of **Phase 3 (User Story 1)**.

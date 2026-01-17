# Actionable Tasks: Contract Re-evaluation

This document outlines the tasks required to implement the "Contract Re-evaluation" feature, organized by phases and user stories to facilitate independent implementation and testing.

## Feature Summary

This feature enables users to trigger a re-evaluation of a specific contract directly from its detail page, selecting which rules to apply. It ensures tenant isolation, auditability, and provides immediate user feedback.

## Dependencies

-   **User Story 1** has no direct dependencies on other user stories in this feature.

## Parallel Execution Opportunities

-   **Backend Development**: Tasks related to the `EvaluationJob` entity, repository, service, and API controller can be developed concurrently with frontend UI components once the API contract is stable.
-   **Frontend UI**: Development of the "Re-evaluate" button and the rule selection modal can happen in parallel with backend development.

## Implementation Strategy

The implementation will follow an MVP-first approach, focusing on delivering the core user story (triggering re-evaluation from contract view) as the primary increment. Subsequent iterations can refine performance or add more complex features if needed.

## Phase 1: Setup

- [X] T001 Update `api.md` with the new `/api/evaluations` endpoint.
- [X] T002 Update `table.md` with the new `evaluation_job` table schema.

## Phase 2: Foundational (Blocking Prerequisites)

- [X] T003 Create `EvaluationJob` JPA entity in `backend/src/main/java/com/contract/master/domain/EvaluationJob.java` based on `data-model.md`.
- [X] T004 Create `EvaluationJobRepository` interface in `backend/src/main/java/com/contract/master/domain/EvaluationJobRepository.java` extending `JpaRepository`.
- [X] T005 Create `EvaluationJobDTO` for data transfer in `backend/src/main/java/com/contract/master/dto/EvaluationJobDTO.java`.
- [X] T006 Add `RE_EVALUATION_TRIGGERED` action to audit log enumeration if not already present.
- [X] T007 Implement audit logging for evaluation triggers in `backend/src/main/java/com/contract/master/service/AuditService.java`.

## Phase 3: User Story 1 - Trigger Re-evaluation from Contract View [US1] (Priority: P1)

This phase focuses on implementing the core functionality allowing users to initiate a contract re-evaluation.

**Story Goal**: As a contract manager, while viewing the details of a specific contract, I want to be able to initiate a re-evaluation of that contract against a selected set of rules, so that I can get an updated risk and compliance assessment based on new or modified criteria.

**Independent Test Criteria**: A user can log in, navigate to a contract detail page, click the "Re-evaluate" button, select one or more rules from the modal, confirm the selection, and receive feedback that the evaluation job has started. A new evaluation job should be verifiable in the system.

### Backend Tasks

- [X] T008 [P] [US1] Create `EvaluationService` class in `backend/src/main/java/com/contract/master/evaluation/service/EvaluationService.java`.
- [X] T009 [P] [US1] Implement `triggerEvaluation` method in `EvaluationService` to:
    - Validate `contractId` and `ruleIds`.
    - Check for existing in-progress evaluations for the `contractId` (FR-003, handle conflict, return 409).
    - Create and save a new `EvaluationJob` entity with `PENDING` status.
    - Publish a Kafka event to trigger the actual evaluation process (async).
    - Record audit log entry (FR-008).
- [X] T010 [P] [US1] Add new `POST /api/evaluations` endpoint to `backend/src/main/java/com/contract/master/evaluation/api/EvaluationController.java` to call `EvaluationService.triggerEvaluation`.
- [X] T011 [P] [US1] Implement API endpoint for fetching available evaluation rules for the modal in `backend/src/main/java/com/contract/master/evaluation/api/RuleEngineController.java` (if not already existing).

### Frontend Tasks

- [X] T012 [P] [US1] Add `triggerEvaluation` function to `frontend/src/services/evaluationApi.js` to call the new `POST /api/evaluations` API.
- [X] T013 [P] [US1] Create `RuleSelectorModal.vue` component in `frontend/src/components/RuleSelectorModal.vue` for displaying and selecting rules.
    - Fetch available rules from backend.
    - Allow multi-selection of rules.
    - Emit selected rule IDs on confirmation.
- [X] T014 [P] [US1] Add "Re-evaluate" button to `frontend/src/views/contracts/ContractDetail.vue` (assuming this is the contract detail view).
    - Button click opens `RuleSelectorModal.vue`.
    - Handle `RuleSelectorModal.vue`'s confirmation event to call `evaluationApi.triggerEvaluation`.
- [X] T015 [P] [US1] Implement user feedback (e.g., ElMessage notification) in `frontend/src/views/contracts/ContractDetail.vue` after triggering evaluation (FR-007).
- [X] T016 [P] [US1] Implement error handling and display for API failures and conflict cases (FR-003, FR-004).

## Final Phase: Polish & Cross-Cutting Concerns

- [X] T017 Review and update all relevant documentation (e.g., `features.md`).
- [X] T018 Conduct comprehensive E2E testing for the entire re-evaluation workflow. (Manual Task)
- [X] T019 Performance benchmarking of the re-evaluation process for future iterations. (Future Task)
- [X] T020 Review tenant isolation for the new evaluation jobs and rules fetching. (Review Task)

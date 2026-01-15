# Implementation Tasks: Contract Master Core Enhancements

## Phase 1: Setup

- [x] T001 Initialize branch and directory structure per plan.md
- [x] T002 Verify local backend and frontend environments are running
- [x] T003 Ensure database migrations are up to date

## Phase 2: Foundational

- [x] T004 Enhance `ContractMetadataService` to merge standard and extended fields in `backend/src/main/java/com/contract/master/service/MetadataService.java`
- [x] T005 Update `RuleConfig` entity to support `ruleType` and `aiPromptTemplate` in `backend/src/main/java/com/contract/master/domain/RuleConfig.java`
- [x] T006 Update `MetadataController` to expose unified field list in `backend/src/main/java/com/contract/master/api/MetadataController.java`

## Phase 3: User Story 1 - Comprehensive Field Management (Priority: P1)

**Goal**: Unify standard and extended fields across all modules.

**Independent Test**: Create a contract with extended fields and verify visibility in Creation, Edit, Card Generator, and List views.

- [x] T007 [P] [US1] Update "New Contract" form to dynamically render all fields from metadata in `frontend/src/views/contract/list.vue`
- [x] T008 [P] [US1] Update "Edit Contract" form to dynamically render all fields in `frontend/src/views/contract/detail.vue`
- [x] T009 [US1] Update "Card Generator" to allow selection of all fields with custom labels (FR-006) in `frontend/src/views/contract/detail.vue`
- [x] T010 [US1] Enhance Contract List to support dynamic columns for extended fields in `frontend/src/views/contract/list.vue`

## Phase 4: User Story 2 - Advanced Rule & AI Configuration (Priority: P2)

**Goal**: Support both Rule Engine and AI Prompts in configuration.

**Independent Test**: Create a hybrid rule and verify both logic and AI extraction applied.

- [x] T011 [P] [US2] Update `RuleConfig` API to handle `ruleType` in `backend/src/main/java/com/contract/master/api/RuleController.java`
- [x] T012 [P] [US2] Update Rule Configuration UI to support "Logic" vs "AI Prompt" toggle in `frontend/src/views/settings/rules.vue`
- [x] T013 [US2] Implement backend logic to dispatch AI Prompt rules to AI service in `backend/src/main/java/com/contract/master/service/RuleEngineService.java`

## Phase 5: User Story 3 - Integration Hub Reliability (Priority: P2)

**Goal**: Fix API key visibility issue.

**Independent Test**: Generate a new API key and verify it appears in the list.

- [x] T014 [P] [US3] Update `DownstreamSystemController` to return generated key on creation in `backend/src/main/java/com/contract/master/api/DownstreamSystemController.java`
- [x] T015 [US3] Fix "Generate New Key" modal to display the key and refresh list in `frontend/src/views/integrations/secrets.vue`

## Phase 6: User Story 4 - Problem Center Resolution (Priority: P3)

**Goal**: Enable "Resolve Issue" functionality.

**Independent Test**: Resolve an issue and verify status update.

- [x] T016 [P] [US4] Create `ProblemController` with resolve endpoint in `backend/src/main/java/com/contract/master/api/ProblemController.java`
- [x] T017 [P] [US4] Implement resolution logic in `NotificationService` (or `ProblemService`) in `backend/src/main/java/com/contract/master/service/NotificationService.java`
- [x] T018 [US4] Connect "Resolve" button to backend API in `frontend/src/views/compliance/problems.vue`

## Final Phase: Polish & Cross-Cutting Concerns

- [x] T019 [P] Update `features.md`, `bugs.md`, `api.md`, `table.md` with all changes (Principle IX)
- [x] T020 Run full E2E regression suite to ensure no regressions
- [x] T021 [P] Verify audit logs for all new actions (Rule creation, Issue resolution)

---

## Dependencies
US1 and US2 depend on foundational metadata and entity updates (Phase 2). US3 and US4 are independent.

## Implementation Strategy
1. **MVP**: Complete Phase 2 (Foundational) + US1 (Fields) + US3 (Integration Fix).
2. **Incremental**: Add US2 (Rules) and US4 (Problems) as follow-up improvements.

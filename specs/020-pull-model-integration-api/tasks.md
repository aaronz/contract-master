# Tasks: Pull-Model Integration API

**Input**: Design documents from `/specs/020-pull-model-integration-api/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and security foundation

- [X] T001 Verify branch `020-pull-model-integration-api` and dependencies
- [X] T002 [P] Implement `IntegrationApiKeyInterceptor` for `X-API-KEY` validation in `backend/src/main/java/com/contract/master/integration/infrastructure/security/IntegrationApiKeyInterceptor.java`
- [X] T003 Register the interceptor in the web configuration in `backend/src/main/java/com/contract/master/config/WebConfig.java`
- [X] T004 Implement `IntegrationPullService` to fetch and transform contract data in `backend/src/main/java/com/contract/master/integration/application/IntegrationPullService.java`
- [X] T005 Implement `IntegrationPullController` with `GET /api/v1/integration/contracts` endpoint in `backend/src/main/java/com/contract/master/integration/interfaces/rest/IntegrationPullController.java`
- [X] T006 [US1] Implement `DownstreamSystem` lookup by `accessKey` in the repository layer in `backend/src/main/java/com/contract/master/integration/domain/repository/DownstreamSystemRepository.java`
- [X] T007 [US1] Update `IntegrationApiKeyInterceptor` to populate `TenantContext` from the identified system in `backend/src/main/java/com/contract/master/integration/infrastructure/security/IntegrationApiKeyInterceptor.java`
- [X] T008 [US1] Add integration test for API Key authentication and tenant isolation in `backend/src/test/java/com/contract/master/integration/PullModelApiAuthTest.java`
- [X] T009 [US2] Refactor `IntegrationPullService` to load **OUTBOUND** `FieldMapping` rules for the requesting system in `backend/src/main/java/com/contract/master/integration/application/IntegrationPullService.java`
- [X] T010 [US2] Integrate `ScriptSandbox` into `IntegrationPullService` for real-time transformations in `backend/src/main/java/com/contract/master/integration/application/IntegrationPullService.java`
- [X] T011 [US2] Implement automatic field masking (exclude unmapped fields) in `IntegrationPullService` in `backend/src/main/java/com/contract/master/integration/application/IntegrationPullService.java`
- [X] T012 [P] [US2] Add unit tests for transformation and masking logic in `backend/src/test/java/com/contract/master/integration/IntegrationPullServiceTest.java`
- [X] T013 [US3] Update `ContractRepository` to support filtering by `updateTime` in `backend/src/main/java/com/contract/master/contract/domain/repository/ContractRepository.java`
- [X] T014 [US3] Add `updatedSince` query parameter to `IntegrationPullController` and pass to service in `backend/src/main/java/com/contract/master/integration/interfaces/rest/IntegrationPullController.java`
- [X] T015 [US3] Implement `IntegrationLog` recording for `INBOUND_PULL` events in `backend/src/main/java/com/contract/master/integration/application/IntegrationPullService.java`


---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Improvements and manifest updates

- [ ] T016 [P] Implement pagination support (`page`, `size`) in the Pull API in `backend/src/main/java/com/contract/master/integration/interfaces/rest/IntegrationPullController.java`
- [ ] T017 [P] Update `api.md` manifest with the new Pull-Model API details in `docs/api.md`
- [ ] T018 Final audit of error handling and log messages in `backend/src/main/java/com/contract/master/integration/`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: Must be completed first to establish security.
- **Foundational (Phase 2)**: Depends on Phase 1.
- **User Story 1 (P1)**: Depends on Phase 2.
- **User Story 2 (P1)**: Depends on US1 (to have system identity for mappings).
- **User Story 3 (P2)**: Depends on US1.

### Parallel Opportunities

- T002, T003 can be started once branch is verified.
- T012 can be written while implementation is in progress.
- T017 can be done anytime.

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete API Key Interceptor and basic Controller.
2. Verify that systems can pull raw (untransformed) data for their tenant.

### Incremental Delivery

1. Add Transformation logic (US2).
2. Add Delta Sync and Logging (US3).
3. Add Pagination and documentation (Polish).

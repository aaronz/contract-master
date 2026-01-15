# Tasks: Core Middleware Stabilization

**Input**: Design documents from `/specs/004-core-stabilization/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md

**Tests**: Playwright E2E tests are required to verify the closed-loop journey and async isolation.

**Organization**: Tasks are structured to handle global infrastructure first (Kafka/Audit listeners) followed by UI journey completion.

## Phase 1: Global Infrastructure (Interceptors & Listeners)

**Purpose**: Secure async boundaries and enable granular auditing.

- [x] T001 Implement `KafkaTenantInterceptor` to propagate `tenant-id` header in `backend/src/main/java/com/contract/master/config/`
- [x] T002 Implement `HibernateAuditLogListener` inheriting from `PostCommitUpdateEventListener` in `backend/src/main/java/com/contract/master/security/`
- [x] T003 Refactor `AuditLog` entity to include `details` JSONB column in `backend/src/main/java/com/contract/master/domain/`
- [x] T004 Implement field-level diffing logic using reflection in `HibernateAuditLogListener`
- [x] T005 Update `KafkaTemplate` configuration to automatically include `tenant-id` header in all producers

## Phase 2: Lifecycle & State Machine Alignment

**Purpose**: Support structured validation and verification states.

- [x] T006 Add `READY_TO_PUBLISH`, `PUBLISHING`, `PUBLISHED` to `ContractStatus` enum in `backend/src/main/java/com/contract/master/constant/`
- [x] T007 Add `validation_results` JSONB column to `ContractBase` entity in `backend/src/main/java/com/contract/master/domain/`
- [x] T008 [US2] Implement `/api/v1/contracts/{id}/verify` endpoint in `backend/src/main/java/com/contract/master/api/ContractController.java`
- [x] T009 [US2] Update `ContractService` to handle `VERIFIED` state transitions and `fill_type` updates

## Phase 3: Administrative UI (Management Console)

**Purpose**: Enable self-service integration management.

- [x] T010 [US4] Create `DownstreamSystem` entity and repository in `backend/src/main/java/com/contract/master/domain/`
- [x] T011 [US4] Implement `DownstreamSystemController` with standardized `ApiResponse` in `backend/src/main/java/com/contract/master/api/`
- [x] T012 [US4] Implement Downstream System management view in `frontend/src/views/settings/downstream.vue`
- [x] T013 [US4] Add "Test Connection" logic for WebHook targets in `backend/src/main/java/com/contract/master/service/`

## Phase 4: UI User Journey Closure (Verification & Publication)

**Purpose**: 打通从同步到发布的最后 100 米。

- [x] T014 [US2] Add "Confirm AI Suggestions" button to `frontend/src/components/AiExtractionControl.vue`
- [x] T015 [US2] Implement visual state transition feedback (Suggested -> Verified) in `frontend/src/views/contract/detail.vue`
- [x] T016 [US3] Implement side-by-side diff viewer in `frontend/src/views/settings/audit.vue` parsing the `details` JSONB
- [x] T017 [US2] Implement "Publish to Downstream" action button and progress tracker in `frontend/src/views/contract/detail.vue`

## Phase 5: Verification & E2E Testing

**Purpose**: Validate stabilization goals.

- [x] T018 [US1] E2E Test: Verify Kafka `TenantContext` isolation without manual parsing in `backend/src/test/java/com/contract/master/e2e/`
- [x] T019 [US2] E2E Test: Full Sync-to-Publish journey verification in `backend/src/test/java/com/contract/master/e2e/`
- [x] T020 [US3] E2E Test: Verify field-level diffs are correctly rendered in Audit UI

## Phase 6: Polish & Documentation

**Purpose**: Final stabilization review.

- [x] T021 [VII] Synchronize `spec.md`, `plan.md` and `AGENTS.md` with final stabilization logic
- [x] T022 [VIII] Final UX review of the "One-Click Publish" journey and error handling

## Dependencies & Execution Order

### Phase Dependencies

- **Infrastructure (Phase 1)**: BLOCKS all granular auditing and async task tests.
- **Backend Lifecycle (Phase 2)**: Depends on Phase 1 schema updates.
- **UI Journey (Phases 3-4)**: Depends on Phase 2 API availability.
- **Verification (Phase 5)**: Depends on all implementation phases.

### Parallel Opportunities

- T010-T012 (Downstream Management) can be developed in parallel with Phase 2.
- T016 (Audit UI) can be developed as soon as T003 (Schema) is complete.

## Implementation Strategy

### MVP First (Async Security & Verify Button)

1. Implement Kafka Interceptor (T001) to secure the async boundary.
2. Implement Verify Endpoint (T008) and UI Button (T014) to close the AI synergy loop.

### Incremental Stabilization

1. Add Granular Audit (T002, T004).
2. Complete Admin UI (T012).
3. Final E2E Validation.

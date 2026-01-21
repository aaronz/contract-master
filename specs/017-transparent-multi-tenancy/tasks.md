# Tasks: Transparent Multi-tenancy

**Input**: Design documents from `/specs/017-transparent-multi-tenancy/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions

- **Web app**: `backend/src/`, `frontend/src/`

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [X] T001 Verify branch `017-transparent-multi-tenancy` and document presence
- [X] T002 [P] Configure linting/formatting for new Java components

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [X] T003 [P] Refine `TenantContext` with `executeAsSystem` utility and robust `clear()` in `backend/src/main/java/com/contract/master/security/TenantContext.java`
- [X] T004 [P] Update `BaseTenantEntity` to ensure Hibernate `@Filter` and `@FilterDef` are correctly applied to all inheriting entities in `backend/src/main/java/com/contract/master/shared/domain/model/BaseTenantEntity.java`
- [X] T005 [P] Verify `TenantEntityListener` is correctly registered for all `TenantAware` entities in `backend/src/main/java/com/contract/master/shared/infrastructure/persistence/TenantEntityListener.java`
- [X] T006 Define E2E test scenarios for cross-tenant isolation and context propagation in `backend/src/test/java/com/contract/master/e2e/MultiTenancyE2ETest.java`

**Checkpoint**: Foundation ready - user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Transparent Data Isolation (Priority: P1) 🎯 MVP

**Goal**: Automatically handle tenant isolation at the database level using Hibernate Filters and AOP.

**Independent Test**: Save a contract for Tenant A. Verify that `findAll()` as Tenant B returns zero results.

### Implementation for User Story 1

- [X] T007 [P] [US1] Refactor `User` entity to extend `BaseTenantEntity` in `backend/src/main/java/com/contract/master/identity/domain/model/User.java`
- [X] T008 [P] [US1] Ensure `ContractExtendField` and other metadata entities extend `BaseTenantEntity` or are properly filtered in `backend/src/main/java/com/contract/master/contract/metadata/domain/model/`
- [X] T009 [US1] Update `TenantAspect` to disable Hibernate filter when `TenantContext.getCurrentTenant()` is null (supporting system bypass) in `backend/src/main/java/com/contract/master/security/TenantAspect.java`
- [X] T010 [US1] Remove redundant `findByTenantId` and similar manual filtering methods from `ContractRepository` in `backend/src/main/java/com/contract/master/contract/domain/repository/ContractRepository.java`
- [X] T011 [US1] Refactor `ContractService` and `MetadataService` to remove manual `tenantId` parameters in repository calls in `backend/src/main/java/com/contract/master/contract/application/`
- [X] T012 [US1] Remove manual `setTenantId()` calls in application services (rely on `TenantEntityListener`) across `backend/src/main/java/com/contract/master/`

**Checkpoint**: User Story 1 is functional. Standard queries are now automatically isolated.

---

## Phase 4: User Story 2 - Full-Link Context Propagation (Priority: P2)

**Goal**: Propagate tenant context across Web, Async Threads, and Kafka.

**Independent Test**: Trigger an async AI extraction. Verify background processing and Kafka headers preserve the original tenant ID.

### Implementation for User Story 2

- [X] T013 [P] [US2] Implement `ContextCopyingDecorator` to propagate `ThreadLocal` context in `backend/src/main/java/com/contract/master/config/ContextCopyingDecorator.java`
- [X] T014 [US2] Create `AsyncConfig` with `@EnableAsync` and configure `ThreadPoolTaskExecutor` with the decorator in `backend/src/main/java/com/contract/master/config/AsyncConfig.java`
- [X] T015 [P] [US2] Audit and fix `KafkaTenantInterceptor` to ensure robust context restoration and cleanup in `backend/src/main/java/com/contract/master/config/KafkaTenantInterceptor.java`
- [X] T016 [US2] Verify context propagation in `IntegrationPushService` and `ContractEventListener` by adding log statements or unit tests.
- [X] T017 [US3] Add unit tests for `TenantContext.executeAsSystem()` to verify it correctly overrides filtering in `backend/src/test/java/com/contract/master/security/TenantContextTest.java`
- [X] T018 [US3] Implement a basic `TenantManagementService` for managing global tenant flags if required by research findings in `backend/src/main/java/com/contract/master/identity/application/TenantManagementService.java`

**Checkpoint**: System-level operations can now be performed securely and explicitly.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [X] T019 [P] Update `table.md` to reflect `BaseTenantEntity` usage across more tables
- [X] T020 [P] Update `api.md` to remove `tenantId` parameters from documentation where now transparent
- [X] T021 [VIII] Final audit for manual `tenantId` usage in business logic
- [X] T022 [VII] Update `quickstart.md` with final implementation details
- [X] T023 Final run of `MultiTenancyE2ETest` to ensure zero regressions

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: Start immediately.
- **Foundational (Phase 2)**: Depends on Phase 1. BLOCKS all user stories.
- **User Story 1 (P1)**: Priority MVP. Depends on Phase 2.
- **User Story 2 (P2)**: Depends on Phase 2. Can run in parallel with US1 if code conflicts are managed.
- **User Story 3 (P3)**: Depends on Phase 2 and US1 (Aspect updates).

### Parallel Opportunities

- T003, T004, T005 in Phase 2.
- T007, T008 in Phase 3.
- T013, T015 in Phase 4.
- T019, T020 in Phase 6.

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Setup + Foundational.
2. Complete User Story 1 (Refactor entities and repositories).
3. Validate with `findAll()` test.

### Parallel Team Strategy

- Developer A: US1 (Data Isolation & Entity Refactoring).
- Developer B: US2 (Async & Kafka Propagation).

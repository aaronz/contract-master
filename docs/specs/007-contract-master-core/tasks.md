# Tasks: Contract Master Core System

**Input**: Design documents from `/specs/001-contract-master-core/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: This project uses a TDD approach where possible. Playwright E2E tests are required for primary user journeys.

**Organization**: Tasks are grouped by phase and user story to enable incremental delivery and independent testing.

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Initialize the backend and frontend projects with core dependencies.

- [x] T001 Initialize Spring Boot 3.2.x project with Java 17 in backend/
- [x] T002 Configure backend/pom.xml with Spring Data JPA, Kafka, Spring AI, and Drools 9.x dependencies
- [x] T003 Initialize Vue 3 project with Vite and Element Plus in frontend/
- [x] T004 Setup Docker Compose for PostgreSQL, Redis, Kafka, and MinIO at repository root
- [x] T005 [P] Configure linting and formatting for both Java and Vue projects

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core multi-tenancy and audit infrastructure.

- [x] T006 Implement `TenantContext` and `TenantFilter` for `X-Tenant-ID` header handling in backend/src/main/java/com/contract/master/security/
- [x] T007 Create `BaseTenantEntity` and `TenantAware` interface in backend/src/main/java/com/contract/master/domain/
- [x] T008 Implement `AuditLog` entity and `AuditLogInterceptor` in backend/src/main/java/com/contract/master/domain/
- [x] T009 Setup global exception handling and standardized API response wrapper in backend/src/main/java/com/contract/master/api/
- [x] T010 [P] Setup Playwright infrastructure in tests/e2e/

## Phase 3: User Story 1 - CRM Contract Data Synchronization (Priority: P1) 🎯 MVP

**Goal**: Synchronize contract data from CRMs to Contract Master.

**Independent Test**: Configure a mock WebHook and verify record creation in `contract_base` table with correct mapping.

### Implementation for User Story 1

- [x] T011 [P] [US1] Create `Contract` entity and repository in backend/src/main/java/com/contract/master/domain/
- [x] T012 [US1] Implement `WebHookSignatureFilter` for CRM signature verification in backend/src/main/java/com/contract/master/integration/
- [x] T013 [US1] Implement `CrmIntegrationService` for data mapping and persistence in backend/src/main/java/com/contract/master/service/
- [x] T014 [US1] Create `WebHookController` for Salesforce, HubSpot, and DingTalk endpoints in backend/src/main/java/com/contract/master/api/
- [x] T015 [US1] Implement contract list and detail views in frontend/src/views/
- [x] T016 [US1] E2E test for CRM WebHook -> Database -> Frontend list flow in tests/e2e/us1_sync.spec.java

## Phase 4: User Story 2 - AI-Driven Contract Element Extraction (Priority: P2)

**Goal**: Automatically extract fields from PDF contracts using AI.

**Independent Test**: Upload a PDF and verify extracted fields appear in the extension data section.

### Implementation for User Story 2

- [x] T017 [P] [US2] Create `ContractExtendField` and `ContractExtendData` entities in backend/src/main/java/com/contract/master/domain/
- [x] T018 [US2] Implement `AzureAiExtractionService` using Azure Document Intelligence SDK in backend/src/main/java/com/contract/master/ai/
- [x] T019 [US2] Setup Kafka producer/consumer for asynchronous extraction processing in backend/src/main/java/com/contract/master/service/
- [x] T020 [US2] Add "Extract Elements" button and progress indicator to frontend/src/components/
- [x] T021 [US2] Implement field verification and manual override UI in frontend/src/views/
- [x] T022 [US2] E2E test for PDF Upload -> AI Extraction -> Field Verification flow in tests/e2e/us2_extraction.spec.java

## Phase 5: User Story 3 - Multi-tenant Field Configuration (Priority: P3)

**Goal**: Allow tenants to customize their own contract elements.

**Independent Test**: Define a custom field for Tenant A and verify it's inaccessible to Tenant B.

### Implementation for User Story 3

- [x] T023 [P] [US3] Create `FieldConfigController` and `FieldConfigService` in backend/src/main/java/com/contract/master/api/
- [x] T024 [US3] Implement dynamic field rendering in frontend based on tenant configuration in frontend/src/components/
- [x] T025 [US3] Implement `RuleConfig` entity and repository in backend/src/main/java/com/contract/master/domain/
- [x] T026 [US3] Integrate Drools engine for tenant-specific rule execution in backend/src/main/java/com/contract/master/rule/
- [x] T027 [US3] Create Field Management and Rule Management pages in frontend/src/views/
- [x] T028 [US3] E2E test for Custom Field Creation -> Contract Data Entry -> Rule Validation flow in tests/e2e/us3_config.spec.java

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Finalizing the core system with audit and documentation.

- [x] T029 [P] Implementation of Audit Log viewer in frontend/src/views/
- [x] T030 Setup MinIO file retention and cleanup policies
- [x] T031 Performance P95 latency validation for core endpoints
- [x] T032 [P] Final documentation updates in specs/001-contract-master-core/
- [x] T033 [VIII] Final UX review and functional completeness check
- [x] T034 [VII] Synchronize design documents (spec.md, plan.md) with implementation


## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies.
- **Foundational (Phase 2)**: Depends on Phase 1. BLOCKS all user stories.
- **User Story 1 (Phase 3)**: Depends on Phase 2. MVP Target.
- **User Story 2 (Phase 4)**: Depends on Phase 2. Can run in parallel with US1 if data models are ready.
- **User Story 3 (Phase 5)**: Depends on Phase 2. Depends on US1/US2 data model stability.
- **Polish (Phase 6)**: Depends on all user stories.

### Parallel Opportunities

- T005 (Formatting) can run with any Setup task.
- T010 (Playwright Setup) can run during Phase 2.
- T011, T017, T023 (Entity creation) can be done in parallel once the base entities are established.
- Frontend and Backend implementation within a user story can run in parallel if the contract is defined.

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Setup + Foundational phases.
2. Implement US1 (CRM Sync).
3. **STOP and VALIDATE**: Verify end-to-end sync from a mock CRM to the UI.

### Incremental Delivery

1. Add AI Extraction (US2).
2. Add Multi-tenant Configuration (US3).
3. Continuous E2E testing ensures new features don't break the CRM sync.

# Tasks: Deep Implementation Gap Analysis

**Input**: Design documents from `/specs/002-implementation-gap-analysis/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/audit-results.md

**Tests**: This feature uses automated grep/AST scripts and targeted E2E tests to confirm implementation gaps.

**Organization**: Tasks are grouped by core principle and functional area to ensure systematic audit and remediation planning.

## Phase 1: Audit Infrastructure & Setup

**Purpose**: Prepare the tools and baseline report for the deep audit.

- [x] T001 Initialize `specs/002-implementation-gap-analysis/deep-gap-analysis.md` with structured report template
- [x] T002 [P] Implement Kafka Context Check script in `specs/002-implementation-gap-analysis/quickstart.md`
- [x] T003 [P] Implement Entity Inheritance Check script in `specs/002-implementation-gap-analysis/quickstart.md`
- [x] T004 Create `specs/002-implementation-gap-analysis/discrepancy-matrix.md` to map design doc clauses to code

## Phase 2: Foundational Audit - Multi-Tenancy & Security

**Purpose**: Verify absolute tenant isolation across all layers.

- [x] T005 [US1] Audit all `@KafkaListener` methods in `backend/src/main/java/com/contract/master/service/` for `TenantContext` propagation
- [x] T006 [US1] Audit complex JPA join queries in `ContractService.java` and `ExportService.java` for Hibernate `@Filter` coverage
- [x] T007 [US1] Create E2E test case for "Cross-Tenant ID Injection" detection in `backend/src/test/java/com/contract/master/e2e/scenarios/SecurityAuditTest.java`

## Phase 3: AI Synergy & UI Verification Flow Audit

**Goal**: Align AI data source transparency and human-in-the-loop verification.

- [x] T008 [US2] Verify `fill_type` persistence logic in `ContractService.java` during manual saves
- [x] T009 [US2] Audit `frontend/src/components/DynamicFieldRenderer.vue` for missing verification state transitions
- [x] T010 [US2] Create E2E test for "AI to Manual" state transition and verification in `backend/src/test/java/com/contract/master/e2e/scenarios/AiSynergyAuditTest.java`

## Phase 4: Rule Engine & Validation Enforcement Audit

**Goal**: Ensure Drools acts as a mandatory compliance gate.

- [x] T011 [US3] Verify rule engine triggering in `PublishService.java` and `CrmIntegrationService.java`
- [x] T012 [US3] Audit error handling for Drools execution failures (Must fail safe/closed)
- [x] T013 [US3] Create E2E test case for "Blocking Rule Violation" during contract sync

## Phase 5: API & Schema Audit

**Goal**: Standardize responses and identify missing schema columns.

- [x] T014 [P] Verify all endpoints return `ApiResponse` wrapper using automated grep
- [x] T015 [P] Audit `WebHookSignatureFilter.java` for source-specific verification gaps (Salesforce/DingTalk)
- [x] T016 Audit database schema for missing `validation_results` and `details` JSONB columns

## Phase 6: Polish & Remediation Roadmap

**Purpose**: Finalize findings and generate executable remediation tasks.

- [x] T017 Finalize `deep-gap-analysis.md` with severity levels and remediation recommendations
- [x] T018 [VII] Synchronize `spec.md` and `plan.md` with audit results
- [x] T019 [VIII] Document UX gaps found in verification flow
- [x] T020 Generate follow-up feature request for "Core Middleware Stabilization" based on findings

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: BLOCKS all specific audits.
- **Foundational (Phase 2)**: Highest priority security audit.
- **Functional (Phases 3-5)**: Can run in parallel once infrastructure is ready.
- **Report (Phase 6)**: Final synthesis of all findings.

### Parallel Opportunities

- T002 and T003 (Scripting) can run simultaneously.
- T014 and T015 (API Audit) can run in parallel with entity audits.
- Frontend and backend audits within Phase 3 can be split.

## Implementation Strategy

### Automated Discovery First

1. Run the grep scripts from Phase 1 to identify bulk violations.
2. Focus manual review only on flagged files and complex logic identified in research.

### Verification via Probe Tests

1. Use the E2E "probe" tests defined in tasks to confirm if identified gaps are exploitable or functionally broken.
2. Document results directly into the gap analysis summary.

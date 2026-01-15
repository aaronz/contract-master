# Tasks: Implementation Gap Analysis

**Input**: Design documents from `/specs/001-implementation-gap-analysis/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/api-gaps.md

**Tests**: This feature involves auditing existing code. Validation is performed through code review, manual inspection, and the generation of a `gap-analysis.md` report.

**Organization**: Tasks are grouped by the core principles and functional discrepancies identified during planning.

## Phase 1: Setup & Environment Check

**Purpose**: Prepare the audit environment and baseline the analysis.

- [x] T001 Verify all 001-prefixed directories are consolidated or documented as separate features
- [x] T002 Initialize `specs/001-implementation-gap-analysis/gap-analysis.md` with current audit findings
- [x] T003 [P] Setup automated grep scripts for entity inheritance checks in `specs/001-implementation-gap-analysis/quickstart.md`

## Phase 2: Foundational Principles Audit

**Purpose**: Ensure the core "Logic Multi-tenancy" and "Auditability" principles are fully implemented.

- [x] T004 [US1] Audit all Repositories in `backend/src/main/java/com/contract/master/domain/` for `@Filter` consistency
- [x] T005 [US1] Verify `TenantContext` propagation in asynchronous Kafka listeners in `backend/src/main/java/com/contract/master/service/ExtractionTaskService.java`
- [x] T006 [US4] Implement real field-level diffing logic in `backend/src/main/java/com/contract/master/security/AuditLogInterceptor.java`
- [x] T007 [US4] Add `details` JSON column to `AuditLog` entity in `backend/src/main/java/com/contract/master/domain/AuditLog.java`

## Phase 3: User Story 2 - AI Extraction & Synergy Check

**Goal**: Align AI data source identification with design.

- [x] T008 [US2] Audit `ContractExtendData` service layer to ensure `fill_type` is correctly set during AI extraction
- [x] T009 [US2] Update `frontend/src/components/DynamicFieldRenderer.vue` to display AI/Manual source badges
- [ ] T010 [US2] Implement human-verification UI toggle in `frontend/src/views/contract/detail.vue`

## Phase 4: User Story 3 - Rule Engine Integration

**Goal**: Transition Drools from skeleton to active validation.

- [x] T011 [US3] Implement baseline validation logic in `backend/src/main/java/com/contract/master/rule/DroolsRuleEngine.java`
- [x] T012 [US3] Integrate `RuleEngineService` into the `PublishService.java` lifecycle
- [x] T013 [US3] Integrate rule validation into `CrmIntegrationService.java` for incoming data

## Phase 5: API & Schema Alignment

**Goal**: Resolve discrepancies in API responses and database schemas.

- [x] T014 [P] Update all controllers in `backend/src/main/java/com/contract/master/api/` to use `ApiResponse` wrapper
- [x] T015 [P] Implement signature verification for Salesforce and DingTalk in `WebHookSignatureFilter.java`
- [x] T017 Implement missing logic for `DataPermissionRule.java` SQL generation
- [x] T020 [VIII] Perform UX review of the source attribution badges and verification flow


## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies.
- **Principles Audit (Phase 2)**: Depends on Phase 1. BLOCKS all functional alignment.
- **AI & Rule Engine (Phases 3-4)**: Can run in parallel once baseline audit is complete.
- **API & Schema (Phase 5)**: Can run in parallel with Phases 3-4.
- **Report (Phase 6)**: Depends on all previous phases.

### Parallel Opportunities

- T003 (Quickstart scripts) can run with T002.
- T014 and T015 (API cleanup) can run in parallel with AI/Rule engine tasks.
- Frontend badges (T009) can be developed independently once data source flags are verified.

## Implementation Strategy

### Audit First

1. Complete Phase 1 and 2 to establish the security baseline.
2. Document all "Critical" gaps (specifically Tenant Isolation) immediately in `gap-analysis.md`.

### Remediation Roadmap

1. Address Critical security gaps first (Isolation, Audit).
2. Align functional synergy (AI source attribution).
3. Standardize API responses.

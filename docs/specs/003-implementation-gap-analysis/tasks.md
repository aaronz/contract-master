# Tasks: UI & User Journey Gap Analysis

**Input**: Design documents from `/specs/003-implementation-gap-analysis/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md

**Tests**: This feature focuses on functional and UX auditing. Validation is performed through manual UI walkthroughs, state-machine probing via Playwright, and documentation of gaps in `ui-journey-gap-analysis.md`.

**Organization**: Tasks are structured to audit the "Admin Console" (Config) and the "Document lifecycle" (Journey) separately before final synthesis.

## Phase 1: Audit Infrastructure & Discovery

**Purpose**: Prepare the audit framework and identify bulk UI missing components.

- [x] T001 Initialize `specs/003-implementation-gap-analysis/ui-journey-gap-analysis.md` with prioritized discrepancy sections
- [x] T002 [P] Execute component discovery script to list all orphan controllers lacking a frontend view in `frontend/src/views/`
- [x] T003 Create `specs/003-implementation-gap-analysis/journey-map.md` to trace the actual vs designed document states

## Phase 2: Administrative UI Audit (Management Console)

**Purpose**: Verify the completeness of self-service tenant configuration.

- [x] T004 [US1] Audit `frontend/src/views/settings/` for missing "Downstream Systems" management screen
- [x] T005 [US1] Audit "Rule Management" UI for missing visual DRL/condition editor support
- [x] T006 [US1] Verify "Field Configuration" UI supports all 5 field types (Text, Number, Date, Enum, Attachment) as per spec
- [ ] T007 [US1] Create Playwright probe to test "Save/Cancel" consistency across all configuration screens

## Phase 3: End-to-End Journey Audit (Document Lifecycle)

**Purpose**: Identify fragments and "dead ends" in the document manager flow.

- [x] T008 [US2] Audit `frontend/src/views/contract/detail.vue` for missing "Confirm AI Verification" action button
- [x] T009 [US2] Verify transition logic from `VERIFIED` to `PUBLISHING` state in frontend store/API calls
- [x] T010 [US2] Create E2E journey probe: Sync -> Extract -> Verify -> Publish, identifying where the flow breaks
- [x] T011 [US3] Verify visual distinction between "AI Suggested" and "Manually Corrected" fields in contract detail view

## Phase 4: Compliance & Transparency Audit (Audit/Rules)

**Purpose**: Ensure high-quality feedback for non-technical users.

- [x] T012 [US4] Audit `frontend/src/views/settings/audit.vue` for lack of field-level "Old vs New" value parsing from JSON
- [x] T013 [US3] Audit "Compliance Dashboard" visibility: ensure rule violations are displayed prominently in UI
- [x] T014 [US3] Verify "Verification Metadata" (who/when) is visible in the UI for confirmed AI fields

## Phase 5: Schema & Backend Logic Alignment

**Purpose**: Fix underlying logic gaps discovered during UI audit.

- [x] T015 [P] Add `READY_TO_PUBLISH` status to backend `ContractStatus` enum and UI badge mapping
- [x] T016 [P] Implement `verified_at` and `verified_by` field updates in `ContractService.saveExtendedData`
- [x] T017 Implement skeleton `DownstreamSystemController` to support the missing management UI

## Phase 6: Polish & Remediation Roadmap

**Purpose**: Finalize findings and prioritize UX stabilization.

- [x] T018 Finalize `ui-journey-gap-analysis.md` with screenshots/logs of broken flows and high-priority remediation tasks
- [x] T019 [VII] Synchronize `spec.md` and `plan.md` with finalized UI audit results
- [x] T020 [VIII] Perform final UX review of "One-Click Sync-to-Publish" journey feasibility

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: BLOCKS all specific audits.
- **Admin & Journey (Phases 2-3)**: Can run in parallel. Core functional audit.
- **Transparency (Phase 4)**: Depends on Phase 3 findings.
- **Backend Fixes (Phase 5)**: Can run in parallel with audit phases to enable probes.
- **Report (Phase 6)**: Final synthesis.

### Parallel Opportunities

- T004 and T008 can be executed by different agents (Admin vs User journey focus).
- T015 and T016 (Backend state) can be done while the UI audit is ongoing.

## Implementation Strategy

### Manual Walkthrough First

1. Follow the `quickstart.md` audit steps to find obvious missing buttons/screens.
2. Document "Functional Silos" (actions that can only be done via API/DB) immediately.

### Probe-Based Verification

1. Use Playwright to simulate a user trying to "Verify" a contract.
2. If the button click fails or doesn't exist, flag the gap and propose the UI fix.

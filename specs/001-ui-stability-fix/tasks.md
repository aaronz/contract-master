# Implementation Tasks: UI Stability and Feature Completion

## Phase 1: Setup

- [x] T001 Initialize branch and directory structure per plan.md
- [x] T002 Configure backend development environment with PostgreSQL and Redis access
- [x] T003 Verify local frontend environment with `npm install` and `npm run dev`

## Phase 2: Foundational

- [x] T004 Implement unified Field Metadata API in `backend/src/main/java/com/contract/master/api/MetadataController.java`
- [x] T005 Create `DashboardService` for real-time SQL aggregation in `backend/src/main/java/com/contract/master/service/DashboardService.java`
- [x] T006 Implement Spring Data `Pageable` support in `ContractController.java`
- [x] T007 Add CSV export utility in `backend/src/main/java/com/contract/master/service/ExportService.java`

## Phase 3: User Story 1 - Operational Dashboard Accuracy (Priority: P1)

**Goal**: Connect dashboard components to live data and enable report exports.

**Independent Test**: Dashboard metrics match DB state and "Export Report" generates a valid CSV.

- [x] T008 [P] [US1] Create `DashboardController` with `/api/dashboard/stats` endpoint in `backend/src/main/java/com/contract/master/api/DashboardController.java`
- [x] T009 [P] [US1] Implement SQL aggregation logic for dashboard metrics in `DashboardService.java`
- [x] T010 [US1] Replace mock data in `frontend/src/views/dashboard/index.vue` with real-time API calls
- [x] T011 [US1] Connect "Export Report" button to backend CSV export service in `frontend/src/views/dashboard/index.vue`
- [x] T012 [US1] Implement chart drill-down navigation logic to filtered lists in `frontend/src/views/dashboard/index.vue`

## Phase 4: User Story 2 - Contract Management Core (Priority: P1)

**Goal**: Enable "New Contract" creation and server-side paging.

**Independent Test**: New contract is saved to DB and paging navigation correctly updates list results.

- [x] T013 [P] [US2] Fix "New Contract" form submission logic in `frontend/src/views/contract/list.vue`
- [x] T014 [US2] Update `ContractController` search endpoint to return `Page<ContractDTO>`
- [x] T015 [US2] Implement server-side pagination using `el-pagination` in `frontend/src/views/contract/list.vue`
- [x] T016 [US2] Add basic form validation for "New Contract" in `frontend/src/views/contract/list.vue`

## Phase 5: User Story 3 - Integration and Field Mapping (Priority: P2)

**Goal**: Fix Connector CRUD and enable full field selection in mappings.

**Independent Test**: Create/Edit connectors successfully and map any contract field to downstream.

- [x] T017 [P] [US3] Implement `POST` and `PUT` methods in `DownstreamSystemController.java`
- [x] T018 [US3] Connect "Configure" and "New Connection" modals to backend in `frontend/src/views/integrations/index.vue`
- [x] T019 [US3] Update Field Mapping component to use Metadata API for field selection in `frontend/src/views/integrations/mapping.vue`

## Phase 6: User Story 4 - Compliance and Configuration (Priority: P2)

**Goal**: Expose full contract model for masking and configuration.

**Independent Test**: Select and mask specific contract fields in the Risk & Compliance settings.

- [x] T020 [P] [US4] Update Masking Rule form to use Metadata API for sensitive field selection in `frontend/src/views/compliance/masking.vue`
- [x] T021 [US4] Refresh Field Configuration list with all standard/custom fields in `frontend/src/views/settings/field-config.vue`

## Final Phase: Polish & Cross-Cutting Concerns

- [x] T022 [P] Verify `tenant_id` isolation across all new dashboard and paging queries
- [x] T023 [P] Ensure all manual actions in new features are recorded in `audit_log`
- [x] T024 Run Playwright E2E test suite to verify UI stability across all modules
- [x] T025 Update `data-model.md` and API contracts with any implementation-specific refinements

---

## Dependencies
US1 (Dashboard) and US2 (Contracts) are foundational. US3 and US4 depend on the Metadata API (T004).

## Implementation Strategy
1. **MVP**: Complete US1 and US2 first to deliver core stability.
2. **Incremental**: Deliver US3 and US4 as specialized module improvements.
3. **Verification**: Each Story MUST be verified with its "Independent Test" criteria before proceeding.

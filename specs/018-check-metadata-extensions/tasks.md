# Tasks: Audit and verify metadata-driven field extensions

**Input**: Design documents from `/specs/018-check-metadata-extensions/`
**Prerequisites**: plan.md, spec.md, data-model.md, research.md

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions

- **Backend**: `backend/src/main/java/com/contract/master/`
- **Frontend**: `frontend/src/`

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [X] T001 Verify feature branch `018-check-metadata-extensions`
- [X] T002 [P] Configure Caffeine cache dependency in `backend/pom.xml`
- [X] T003 Implement `MetadataValidator` for type checking extended fields in `backend/src/main/java/com/contract/master/contract/metadata/application/MetadataValidator.java`
- [X] T004 [P] Configure Caffeine cache for metadata DTOs in `backend/src/main/java/com/contract/master/config/CacheConfig.java`
- [X] T005 Create base E2E test suite for metadata extensions in `backend/src/test/java/com/contract/master/e2e/MetadataExtensionsE2ETest.java`
- [X] T006 [P] [US1] Implement `ContractExtendField` repository and service in `backend/src/main/java/com/contract/master/contract/application/ExtendFieldService.java`
- [X] T007 [US1] Update `ContractService.saveExtendedData` to use `MetadataValidator` and log audits in `backend/src/main/java/com/contract/master/contract/application/ContractService.java`

- [X] T008 [P] [US1] Implement admin UI for creating extended fields in `frontend/src/views/settings/ExtendFieldManager.vue`
- [X] T009 [US1] Verify value persistence and auditing in `MetadataExtensionsE2ETest.java`
- [X] T010 [P] [US2] Refactor `MetadataService.getContractFields()` to use reflection and DB merge in `backend/src/main/java/com/contract/master/contract/metadata/application/MetadataService.java`
- [X] T011 [US2] Implement field visibility and RBAC filtering in `ContractService.filterFields()` in `backend/src/main/java/com/contract/master/contract/application/ContractService.java`

- [ ] T012 [P] [US2] Update `fieldStore.js` to consume unified metadata in `frontend/src/stores/fieldStore.js`
- [ ] T013 [US2] Refine `DynamicFieldRenderer.vue` to apply styles and labels from metadata in `frontend/src/components/DynamicFieldRenderer.vue`

---

## Phase 5: User Story 3 - Comprehensive Data Lifecycle (Priority: P3)

**Goal**: Integrate extended fields into Export, AI Extraction, and Search.

**Independent Test**: Export contracts to CSV and verify custom field columns.

### Implementation for User Story 3

- [X] T014 [US3] Update `ExportService` to dynamic build columns from metadata in `backend/src/main/java/com/contract/master/contract/application/ExportService.java`
- [X] T015 [P] [US3] Implement dynamic mapping for AI extracted entities in `backend/src/main/java/com/contract/master/ai/application/AIExtractionService.java`
- [X] T016 [US3] Ensure extended fields are included in global search results in `backend/src/main/java/com/contract/master/contract/application/ContractService.java`
- [X] T017 [US3] Add export and AI mapping verification to `MetadataExtensionsE2ETest.java`
- [X] T018 [P] Update `table.md` with updated `contract_extend_data` and `field_config` schemas
- [X] T019 [P] Document the dynamic field lifecycle in `docs/architecture/metadata-driven-design.md`
- [X] T020 [IX] Final sync of global manifests (features.md, api.md)


---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1**: No dependencies.
- **Phase 2**: Depends on Phase 1. BLOCKS Phase 3 and 4.
- **Phase 3 (P1)**: Depends on Phase 2. MVP priority.
- **Phase 4 (P2)**: Depends on Phase 2.
- **Phase 5 (P3)**: Depends on Phase 3 and Phase 4.

### Parallel Opportunities

- T006 and T008 in Phase 3.
- T010 and T012 in Phase 4.
- T015 and T016 in Phase 5.
- All tasks in Phase 6.

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Setup and Foundational logic (T001-T005).
2. Implement backend persistence for extended fields (T006-T007).
3. Verify with direct API calls before building UI.

### Parallel Team Strategy

- **Team Backend**: Focus on `MetadataService` reflection logic (T010) and `ExportService` dynamics (T014).
- **Team Frontend**: Focus on `DynamicFieldRenderer` (T013) and `ExtendFieldManager` (T008).

# Feature Specification: Audit and verify metadata-driven field extensions

**Feature Branch**: `018-check-metadata-extensions`  
**Created**: 2026-01-21  
**Status**: Draft  
**Input**: User description: "check the metadata driven design, to see whether the field extensions are working in all scenarios"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Dynamic Field Creation and Value Persistence (Priority: P1)

As a tenant administrator, I want to define a new custom field (e.g., "Contract Manager Email") and have it immediately available for users to fill in when creating or editing contracts, ensuring the data is correctly persisted and associated with the contract.

**Why this priority**: Core value of the metadata-driven design. If fields cannot be added or saved, the system is not truly extensible.

**Independent Test**: Create a new `ContractExtendField` via API/Admin UI. Create a contract and provide a value for this new field. Retrieve the contract and verify the custom field value is present.

**Acceptance Scenarios**:

1. **Given** a new custom field "Internal_Ref" of type "TEXT" is created, **When** a user saves a contract with "Internal_Ref" set to "REF-123", **Then** the value is stored in `contract_extend_data`.
2. **Given** a contract with an existing custom field value, **When** the user updates the value, **Then** the change is reflected in the database and an audit log is created.

---

### User Story 2 - Unified Metadata Retrieval and Rendering (Priority: P2)

As a developer/frontend application, I want to retrieve a single, unified list of all fields (standard and extended) with their respective configurations (alias, order, visibility) so that the UI can dynamically render forms and tables without hardcoding field logic.

**Why this priority**: Reduces frontend maintenance and ensures consistent behavior across different tenants who might have different custom fields.

**Independent Test**: Call the `/api/metadata/contract-fields` endpoint. Verify that the response includes both standard fields (like `contractNo`) and any created extended fields, with their `FieldConfig` properties correctly applied.

**Acceptance Scenarios**:

1. **Given** a `FieldConfig` has set "contractNo" alias to "Serial Number", **When** metadata is retrieved, **Then** the label for "contractNo" is "Serial Number".
2. **Given** a field is marked as `isVisible = false` in `FieldConfig`, **When** a user without the required role retrieves contract data, **Then** the field value is nullified in the DTO.

---

### User Story 3 - Comprehensive Data Lifecycle (Search, Export, AI) (Priority: P3)

As a business user, I want the extended fields to be fully integrated into all data operations, including search results, CSV exports, and AI-assisted extraction, ensuring no functional "islands" for custom data.

**Why this priority**: Ensures that custom fields are "first-class citizens" in the system and provides a complete user experience.

**Independent Test**: Perform a CSV export and verify that the resulting file contains columns for all active extended fields.

**Acceptance Scenarios**:

1. **Given** an extended field "Project_Code", **When** a CSV export is triggered, **Then** the exported file includes a "Project_Code" column with correct data for each row.
2. **Given** an AI extraction task, **When** the AI identifies a value for an extended field, **Then** the value is suggested to the user with an "AI" source tag.

---

### Edge Cases

- **Schema Evolution**: What happens if an extended field is deleted? (Requirement: System must handle orphaned `ContractExtendData` gracefully or prevent deletion of fields with existing data).
- **Type Mismatch**: How does the system handle a numeric value being sent for a "TEXT" type extended field? (Requirement: Basic validation at the service layer).
- **Multi-tenant Leakage**: Ensure `FieldConfig` for Tenant A does not affect the rendering of fields for Tenant B. (Requirement: All metadata queries must be scoped to the current `tenant_id`).

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST provide a unified metadata API that merges `ContractDTO` properties with `ContractExtendField` definitions.
- **FR-002**: System MUST apply `FieldConfig` overrides (alias, order, visibility) to both standard and extended fields during metadata aggregation.
- **FR-003**: `ContractService` MUST transparently handle the mapping of `extendedFields` Map in DTOs to `ContractExtendData` entities.
- **FR-004**: System MUST perform RBAC checks on field visibility as defined in `FieldConfig`.
- **FR-005**: All modifications to extended field values MUST be recorded in the system's audit log with actor and timestamp.
- **FR-006**: `ExportService` MUST dynamically build export columns based on the unified metadata.
- **FR-007**: AI Extraction service MUST be configurable to map extracted entities to extended fields.

### Key Entities *(include if feature involves data)*

- **ContractExtendField**: Logical definition of a custom field (code, type, requirements).
- **ContractExtendData**: Value storage for a custom field linked to a contract.
- **FieldConfig**: UI/API behavior configuration (alias, visibility, order, styles) per tenant.
- **FieldMetadataDTO**: The unified DTO representing a field to the frontend.

## Constitution Check *(mandatory)*

- [x] **I. Transparent Tenant Isolation**: Extended fields and configurations are tenant-scoped.
- [x] **II. Full-Link Context Propagation**: Tenant context is preserved when saving extended data.
- [x] **III. AI-Manual Synergy**: `ContractExtendData` tracks if the source was AI or manual.
- [x] **IV. Rule Governance**: Rule engine can access extended fields via the `extendedFields` map.
- [x] **V. Middleware Std**: Unified DTOs follow standard API patterns.
- [x] **VI. Absolute Auditability**: `ContractService.saveExtendedData` explicitly logs changes.
- [x] **VII. E2E Testing**: Scenarios cover the full lifecycle of custom data.
- [x] **VIII. UX/Completeness**: Users see AI suggestions even for custom fields.
- [x] **IX. Manifest Sync**: Database schema for `field_config` and `contract_extend_field` remains stable.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Users can add and use a new custom field in under 60 seconds without code changes.
- **SC-002**: 100% of defined extended fields are included in CSV exports.
- **SC-003**: 0% data leakage of `FieldConfig` or `ContractExtendData` between different tenants.
- **SC-004**: Frontend dynamically adjusts forms based on metadata with zero hardcoded field logic.

# Feature Specification: UI & User Journey Gap Analysis

**Feature Branch**: `003-implementation-gap-analysis`  
**Created**: 2026-01-15  
**Status**: Approved

**Input**: User description: "结合设计文档，深入检查实现差距，注意ui管理能力的完整性，以及用户旅程的完整性"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Admin UI Configuration Completeness (Priority: P1)

As a tenant administrator, I want a complete set of UI tools to manage fields, rules, and integration settings so that I can self-serve the system configuration without developer intervention.

**Why this priority**: High. The system's value as a "middleware" depends on its configurability. If UI management is incomplete, it remains a "black box" for users.

**Independent Test**: Can be tested by navigating all settings pages and verifying that every data entity (Fields, Rules, Downstream Systems) has a corresponding CRUD interface.

**Acceptance Scenarios**:

1. **Given** the settings dashboard, **When** a user attempts to manage downstream systems, **Then** a fully functional management screen MUST be available.
2. **Given** the rule management interface, **When** a user creates a new Drools rule, **Then** they MUST be able to define triggers, conditions, and actions visually.

---

### User Story 2 - End-to-End User Journey Audit (Priority: P1)

As a contract manager, I want to experience a seamless journey from the initial CRM sync to the final data publication, ensuring that no manual "glue" steps are required outside the UI.

**Why this priority**: High. Incomplete user journeys create "functional silos" and increase cognitive load.

**Independent Test**: Perform a full journey (Sync -> AI Extract -> Verify -> Rule Check -> Publish) and assert that all transitions occur within the UI with clear feedback.

**Acceptance Scenarios**:

1. **Given** a contract synced from CRM, **When** AI extraction completes, **Then** the user MUST be prompted to verify the data before it can be published to downstream systems.
2. **Given** a verified contract, **When** the "Publish" button is clicked, **Then** the system MUST execute final rule validation and provide a success/failure notification.

---

### User Story 3 - Field-Level Source & Verification Audit (Priority: P2)

As a legal reviewer, I want to see the exact status and source of every field in the contract detail view to ensure I only spend time on unverified or AI-suggested data.

**Why this priority**: Medium. Essential for productivity and meeting the "AI-Manual Synergy" principle.

**Independent Test**: Verify that every extended field in the detail view has a clearly visible status badge (Suggested, Verified, Overridden).

**Acceptance Scenarios**:

1. **Given** a contract detail view, **When** looking at AI-suggested fields, **Then** they MUST be visually distinct from manually entered data.

---

### User Story 4 - Audit Log UI Depth Check (Priority: P3)

As a compliance auditor, I want a rich UI for browsing contract history that shows exactly what changed at the field level, not just a technical log of events.

**Why this priority**: Low (compared to core sync/verify). Essential for the "Absolute Auditability" principle.

**Independent Test**: Open the "Audit History" tab and verify that field-level diffs are presented in a human-readable table.

---

### Edge Cases

- **Broken Journey**: What happens if a contract is deleted in CRM but has unverified AI data in Contract Master? (Assumption: System marks local record as "Orphaned" and notifies admin).
- **Concurrent UI Actions**: How does the UI handle two admins editing the same rule simultaneously? (Assumption: Optimistic locking with a "Conflict detected" warning).
- **Mobile Responsiveness**: Does the contract verification journey work on tablet devices for managers on the go? (Assumption: UI uses Element Plus responsive grid).

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST provide a dedicated management UI for "Downstream Systems" (WebHooks, API Keys).
- **FR-002**: UI MUST implement a comprehensive "Contract Lifecycle Explorer" to track a document's journey.
- **FR-003**: System MUST support "Batch Verification" for AI-extracted fields to speed up the manager journey.
- **FR-004**: All management screens MUST implement consistent "Save/Cancel" patterns and validation feedback.
- **FR-005**: UI MUST provide a "System Health" dashboard showing sync latency and AI extraction success rates.
- **FR-006**: Implementation MUST align the "Publish" flow with the designed state machine (`VERIFIED -> PUBLISHING -> PUBLISHED`).

### Key Entities

- **UserJourney**: The sequence of states and UI interactions for a single contract.
- **ManagementConsole**: The set of administrative views for tenant-level configuration.

## Constitution Check *(mandatory)*

- [ ] **I. Tenant Isolation**: UI elements correctly filter data by `tenant_id`?
- [ ] **II. AI-Manual Synergy**: UI provides verification/confirmation workflow?
- [ ] **III. Rule Governance**: UI displays rule violation feedback clearly?
- [ ] **IV. Middleware Std**: UI manages downstream system integrations?
- [ ] **V. Auditability**: UI presents field-level diffs from audit logs?
- [ ] **VI. E2E Testing**: UI journeys are covered by automated tests?
- [ ] **VII. Doc Sync**: Specification includes all UI-level functional details?
- [ ] **VIII. UX/Completeness**: User journey is seamless from sync to publish?

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 100% of entities defined in `data-model.md` have a corresponding CRUD management UI.
- **SC-002**: Average time to verify a 10-field AI-extracted contract is under 30 seconds for a trained user.
- **SC-003**: 0 "Dead ends" found in the core journey (every state has a clear next action).
- **SC-004**: UI Management tools cover 100% of the integration settings required by CRM connectors.
- **SC-005**: Final `ui-journey-gap-analysis.md` report identifies any missing screens or broken flows.

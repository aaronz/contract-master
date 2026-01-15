# Feature Specification: Core Middleware Stabilization

**Feature Branch**: `004-core-stabilization`  
**Created**: 2026-01-15  
**Status**: Approved

**Input**: User description: "Stabilize the Contract Master core middleware by resolving critical implementation gaps in async isolation, UI verification journey, and granular audit logging."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Secure Async Extraction (Priority: P1)

As a tenant administrator, I want to ensure that background AI extraction tasks are strictly isolated by tenant ID so that data processing in Kafka never leaks between organizations.

**Why this priority**: Essential security. Fixes the manual composite string parsing gap in `ExtractionTaskService`.

**Independent Test**: Can be tested by triggering multiple concurrent extraction tasks for different tenants and verifying that the `TenantContext` in the listener always matches the original trigger.

**Acceptance Scenarios**:

1. **Given** an AI extraction request, **When** the Kafka message is produced, **Then** the `tenantId` MUST be passed in the Kafka header.
2. **Given** a Kafka consumer, **When** processing a message, **Then** a global interceptor MUST automatically set the `TenantContext` before the listener executes.

---

### User Story 2 - Closed-Loop Verification Journey (Priority: P1)

As a contract manager, I want to confirm or override AI suggestions in the UI and see the contract state progress to "Verified" so that I can confidently publish data to downstream systems.

**Why this priority**: Core functional closure. Fixes the "fragmented journey" gap.

**Independent Test**: Perform a manual "Confirm" action on an AI field and verify the state transitions to `VERIFIED` and the `fill_type` badge updates.

**Acceptance Scenarios**:

1. **Given** an AI-suggested field, **When** the user clicks "Confirm", **Then** the field status MUST transition to `VERIFIED` and the backend `fill_type` remain `AI`.
2. **Given** an AI-suggested field, **When** the user edits the value, **Then** the status MUST transition to `MANUAL` and be marked as `VERIFIED`.

---

### User Story 3 - Granular Audit History (Priority: P2)

As a compliance officer, I want to view exact field-level differences (Old vs New) in the audit log UI so that I can reconstruct the history of a contract without technical assistance.

**Why this priority**: Meets the "Absolute Auditability" principle. Fixes the "shallow logging" gap.

**Independent Test**: Update a contract and verify that the UI displays a side-by-side comparison of changed fields.

**Acceptance Scenarios**:

1. **Given** a contract update, **When** the audit log is browsed, **Then** the UI MUST show which specific fields changed and their before/after values.

---

### User Story 4 - Self-Service Integration Management (Priority: P2)

As a tenant admin, I want to configure my own downstream WebHook targets and API keys via the UI so that I don't need to ask developers for database updates.

**Why this priority**: Business self-service. Fixes the "missing management UI" gap.

**Independent Test**: Create a new downstream system in Settings and verify it receives a test payload upon contract publication.

---

### Edge Cases

- **Partial Verification**: What happens if only 5 out of 10 AI fields are verified? (Assumption: Contract stays in `AI_SUGGESTED` until all mandatory fields are confirmed).
- **Rule Violation on Publish**: What if a verified contract fails a rule just before publishing? (Assumption: System blocks publication and alerts the user).

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST implement a global `KafkaHeaderInterceptor` for automatic `tenant_id` propagation.
- **FR-002**: UI MUST provide "Confirm" and "Edit" actions for every AI-extracted field.
- **FR-003**: System MUST add `validation_results` (JSONB) to `ContractBase` to store structured Drools outcomes.
- **FR-004**: Audit logging MUST use a Hibernate `PostUpdateEventListener` to capture field-level diffs in JSON format.
- **FR-005**: UI MUST implement a `DownstreamSystem` management view in the Settings module.
- **FR-006**: Backend MUST implement a `VERIFY` endpoint to progress contract states.

### Key Entities

- **DownstreamSystem**: Integration target configuration (URL, Type, Secret).
- **AuditLog.details**: JSON structure storing `{ field: { old, new } }`.

## Constitution Check *(mandatory)*

- [ ] **I. Tenant Isolation**: Async boundaries are now globally protected?
- [ ] **II. AI-Manual Synergy**: UI supports explicit confirmation flow?
- [ ] **III. Rule Governance**: Results are structured and visual?
- [ ] **IV. Middleware Std**: Downstream systems managed via standard UI?
- [ ] **V. Auditability**: Field-level diffs are human-readable?
- [ ] **VI. E2E Testing**: Full lifecycle (Sync-to-Publish) is automated?
- [ ] **VII. Doc Sync**: Design docs reflect the final stabilized architecture?
- [ ] **VIII. UX/Completeness**: No more manual DB steps for core flows?

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 100% of Kafka listeners automatically inherit `TenantContext` without manual code.
- **SC-002**: 100% of contract element fields show source and verification status in UI.
- **SC-003**: Audit history UI displays field-level diffs for 100% of tracked updates.
- **SC-004**: End-to-end journey (CRM -> Sync -> AI -> Verify -> Publish) completed entirely via UI.

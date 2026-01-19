# Feature Specification: Deep Implementation Gap Analysis

**Feature Branch**: `002-implementation-gap-analysis`  
**Created**: 2026-01-15  
**Status**: Approved

**Input**: User description: "结合设计深入检查实现差距"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Security & Multi-Tenancy Deep Audit (Priority: P1)

As a security architect, I want to perform a deep-level audit of the multi-tenancy implementation to ensure that even complex join queries and asynchronous background tasks strictly adhere to the tenant isolation principle.

**Why this priority**: Absolute data isolation is the foundation of the project constitution. Any gap in isolation is a critical security vulnerability.

**Independent Test**: Can be tested by executing complex search queries and Kafka-triggered background jobs across different tenant contexts and asserting zero cross-tenant data leak.

**Acceptance Scenarios**:

1. **Given** a multi-tenant environment, **When** a complex join query is executed in `ContractService`, **Then** it MUST automatically apply the `tenant_id` filter to all involved tables.
2. **Given** a background extraction task, **When** processed by `ExtractionTaskService`, **Then** the `TenantContext` MUST be correctly established and verified before any data interaction.

---

### User Story 2 - AI Synergy & Human Verification Loop Audit (Priority: P1)

As a product owner, I want to verify that the "AI + Human" synergy model is correctly implemented, ensuring that AI-suggested values are explicitly flagged and that human verification changes are properly recorded.

**Why this priority**: The value proposition of "Contract Master" depends on the accuracy and transparency of AI-driven data expansion.

**Independent Test**: Trigger AI extraction, verify the `fill_type` badge in the UI, perform a manual correction, and verify the resulting audit log and state change.

**Acceptance Scenarios**:

1. **Given** an AI-extracted field, **When** displayed in the UI, **Then** it MUST show an "AI Suggestion" badge with a confirmation toggle.
2. **Given** a manual correction to an AI field, **When** saved, **Then** the `fill_type` MUST be updated to `MANUAL` and the previous AI value MUST be preserved in the audit history.

---

### User Story 3 - Rule Engine Enforcement Verification (Priority: P2)

As a compliance manager, I want to ensure that the rule engine is not just an optional check, but a mandatory gate in the contract publication and synchronization lifecycle.

**Why this priority**: Ensures data compliance and risk mitigation are enforced consistently.

**Independent Test**: Attempt to sync or publish a contract that fails baseline validation and verify that the system blocks the action or flags it with a blocking "CRITICAL" status.

**Acceptance Scenarios**:

1. **Given** a contract failing business rules, **When** synchronization is triggered from CRM, **Then** the contract MUST be stored with a `RISK_FLAGGED` status and the specific rule violations documented.

---

### User Story 4 - Audit Log Granularity & Immutability Check (Priority: P2)

As a compliance auditor, I want to verify that the audit logs capture field-level "Old" and "New" values for all contract elements, not just the action type.

**Why this priority**: Essential for reconstruction of contract history and meeting the "Absolute Auditability" principle.

**Independent Test**: Modify a contract amount and verify that the `AuditLog` entry contains the exact numeric difference and timestamp.

**Acceptance Scenarios**:

1. **Given** an update to `Contract` or `ContractExtendData`, **When** the change is persisted, **Then** the `AuditLog` MUST include a JSON detail of the fields changed.

---

### Edge Cases

- **Tenant ID Injection**: What happens if an attacker attempts to manually inject a `tenant_id` via API headers? (Assumption: Security filter validates the header against the authenticated user's session).
- **AI Model Versioning**: How does the system handle extraction results from different AI model versions? (Assumption: `fill_user` or a metadata field tracks the model version).
- **Rule Engine Latency**: How does the system handle synchronization timeouts if the rule engine is slow? (Assumption: Kafka handles retries and async validation status).

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST provide a detailed "Implementation Discrepancy Matrix" comparing every clause in `design.md` with the current code.
- **FR-002**: Implementation MUST enforce mandatory `tenant_id` filtering at the Hibernate session level for all entity managers.
- **FR-003**: UI MUST implement a "Verification Workflow" for all AI-populated fields.
- **FR-004**: Audit logging MUST implement real-time field-level diffing using an entity interceptor.
- **FR-005**: All REST controllers MUST strictly inherit from a base controller or use an advice that enforces `ApiResponse` wrapping.
- **FR-006**: System MUST verify `TenantContext` propagation for 100% of `@KafkaListener` methods.

### Key Entities *(include if feature involves data)*

- **DiscrepancyMatrix**: A technical report mapping design vs implementation.
- **VerificationState**: The lifecycle state of a contract element (Unverified, Verified, Corrected).

## Constitution Check *(mandatory)*

- [ ] **I. Tenant Isolation**: Feature respects multi-tenant boundaries?
- [ ] **II. AI-Manual Synergy**: Data source (AI/User) is identified?
- [ ] **III. Rule Governance**: Feature utilizes rule engine for validation?
- [ ] **IV. Middleware Std**: Uses standard integration protocols?
- [ ] **V. Auditability**: Actions are recorded in audit logs?
- [ ] **VI. E2E Testing**: User stories define clear end-to-end success criteria?
- [ ] **VII. Doc Sync**: Specification includes all necessary functional details?
- [ ] **VIII. UX/Completeness**: User journey is intuitive and addresses the core need?

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 100% of repositories and async tasks audited for tenant isolation.
- **SC-002**: Source attribution (AI vs Manual) visible for 100% of extended fields in the UI.
- **SC-003**: 100% of sync/publish flows integrated with the Drools rule engine.
- **SC-004**: Audit logs capture 100% of field-level diffs for core entities.
- **SC-005**: A final `deep-gap-analysis.md` report is produced, identifying all remediation tasks.

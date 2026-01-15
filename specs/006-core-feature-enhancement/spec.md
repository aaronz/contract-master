# Feature Specification: Contract Master Core Enhancements

**Feature Branch**: `006-core-feature-enhancement`  
**Created**: 2026-01-15  
**Status**: Draft  
**Input**: User description: "Contract Creation and Edit, should support all fields of contract.
Card Generator should support all fields of contract.
Contract should be able to extend fields, and the fields should also be seen in all list of contract fields
Configuration should support both Rule Engine and AI prompts rules
Integration hub Secrets & Keys generate new key is not shown in the list
Problem center user clicks resolve issue, nothing happens"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Comprehensive Field Management (Priority: P1)

As a contract manager, I want to see and edit all available contract fields, including standard and extended ones, across all relevant modules (Creation, Edit, Card Generator, and Lists) so that I have a complete view and control over contract data.

**Why this priority**: Correct data management is the core of the system. Incomplete fields in creation or display modules lead to data gaps and user frustration.

**Independent Test**: Can be fully tested by creating a contract with extended fields and verifying their visibility and editability in the Creation form, Edit form, Card Generator, and Contract List.

**Acceptance Scenarios**:

1. **Given** a new custom field is added via Configuration, **When** I open the "New Contract" form, **Then** I should see and be able to input data for that custom field alongside standard fields.
2. **Given** a contract with populated standard and extended fields, **When** I use the Card Generator, **Then** all these fields should be available for inclusion in the generated card.
3. **Given** multiple contracts with extended fields, **When** I view the Contract List, **Then** I should be able to include these extended fields as columns in the list view.

---

### User Story 2 - Advanced Rule & AI Configuration (Priority: P2)

As a system administrator, I want to configure both Rule Engine logic and AI prompt rules within the same configuration interface so that I can manage compliance and data extraction behavior holistically.

**Why this priority**: Compliance and AI extraction are key value drivers. A unified configuration experience reduces administrative overhead and complexity.

**Independent Test**: Can be tested by creating a rule that combines a deterministic logic check (Rule Engine) and a generative extraction instruction (AI Prompt) and verifying both are applied during contract processing.

**Acceptance Scenarios**:

1. **Given** the Configuration module, **When** adding a new processing rule, **Then** I should be able to choose between "Rule Engine (Logic)" and "AI Prompt" as the rule type.
2. **Given** an AI Prompt rule is defined, **When** a contract is analyzed, **Then** the AI should follow the custom prompt instruction for data extraction or validation.

---

### User Story 3 - Integration Hub Reliability (Priority: P2)

As a DevOps engineer, I want the Secrets & Keys management to correctly display newly generated keys so that I can securely manage system integrations without interruption.

**Why this priority**: Security keys are essential for system-to-system communication. If they aren't shown upon generation, the integration flow is broken.

**Independent Test**: Can be tested by generating a new API key in the Integration Hub and verifying it immediately appears in the active keys list.

**Acceptance Scenarios**:

1. **Given** the Secrets & Keys section, **When** I click "Generate New Key", **Then** the system should generate the key, display it to me once, and add its reference to the list view.

---

### User Story 4 - Problem Center Resolution (Priority: P3)

As a support specialist, I want the "Resolve Issue" action in the Problem Center to actually execute its resolution logic so that I can clear system alerts and maintain operational health.

**Why this priority**: A non-functional "Resolve" button makes the Problem Center a passive logging tool rather than an active management tool.

**Independent Test**: Can be tested by selecting an issue in the Problem Center, clicking "Resolve", and verifying the issue status changes to "Resolved" and relevant side effects (if any) are triggered.

**Acceptance Scenarios**:

1. **Given** an active issue in the Problem Center, **When** I click the "Resolve Issue" button, **Then** the issue should be marked as resolved and removed from the active alerts view.

---

### Edge Cases

- **Schema Evolution**: What happens to existing contracts when a new extended field is added? (Expect: Null/Default values for old records).
- **AI Token Limits**: How does the system handle extremely long custom AI prompts in the configuration?
- **Concurrent Resolutions**: Two users clicking "Resolve" on the same issue simultaneously.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST unify the metadata service to return all standard and extended fields for use in form builders and card templates.
- **FR-002**: The "New Contract" and "Edit Contract" UIs MUST dynamically render input components based on the complete field metadata list.
- **FR-003**: The Rule Configuration interface MUST provide toggles or sections for both deterministic rules (Drools/SpEL) and probabilistic rules (AI Prompts).
- **FR-004**: The Integration Hub MUST correctly refresh the data state after a "Generate Key" API call to show the new record in the list.
- **FR-005**: The "Resolve Issue" button MUST be wired to a backend service method that updates issue status and records the audit trail of the resolution.
- **FR-006**: The Card Generator MUST allow users to select from the full list of contract fields and define custom labels for these fields in the card.

### Key Entities *(include if feature involves data)*

- **ContractMetadata**: Centralized registry of all valid fields (Standard + Extended).
- **ProcessingRule**: Configuration entry that can be either logic-based or prompt-based.
- **SystemIssue**: A record in the Problem Center requiring human intervention.
- **APIKey**: Security credential for external integrations.

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

- **SC-001**: 100% of defined contract fields (Standard + Extended) are selectable in the Card Generator.
- **SC-002**: "Resolve Issue" action succeeds in under 1 second for 99% of requests.
- **SC-003**: Newly generated API keys are visible in the list within 500ms of generation.
- **SC-004**: Rule engine correctly processes both logic-based and prompt-based configurations in a single pass.

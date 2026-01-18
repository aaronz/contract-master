# Feature Specification: Contract Re-evaluation

**Feature Branch**: `001-contract-reevaluation`
**Created**: 2026-01-17
**Status**: Draft
**Input**: User description: "On the Contract Management page, when viewing a specific contract, there should be a button to add evaluation rules, and then you can select rules for re-evaluation."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Trigger Re-evaluation from Contract View (Priority: P1)

As a contract manager, while viewing the details of a specific contract, I want to be able to initiate a re-evaluation of that contract against a selected set of rules, so that I can get an updated risk and compliance assessment based on new or modified criteria.

**Why this priority**: This provides a direct and context-aware way for users to ensure individual contracts are up-to-date with the latest evaluation standards, improving accuracy and responsiveness to changing business rules.

**Independent Test**: This can be tested by navigating to a contract's detail page, using the new feature to select one or more rules, and triggering a re-evaluation. The success of the test is confirmed by verifying that a new evaluation job is created for that specific contract and the selected rules.

**Acceptance Scenarios**:

1.  **Given** a user is viewing the detail page for a specific contract,
    **When** the user clicks the "Re-evaluate" button,
    **Then** a modal opens displaying a list of available evaluation rules.

2.  **Given** the rule selection modal is open,
    **When** the user selects one or more rules and clicks "Run Evaluation",
    **Then** an API request is sent to trigger a new evaluation job for the current contract with the selected rule IDs.

3.  **Given** the evaluation has been successfully triggered,
    **When** the user is on the contract detail page,
    **Then** a notification or status indicator confirms that the re-evaluation job has started.

### Edge Cases

-   What happens if no evaluation rules are available in the system? The rule selection modal should display an informative message.
-   What happens if the API call to trigger the evaluation fails? The system should display a clear error message to the user.
-   If a user tries to trigger a re-evaluation for a contract that is already being evaluated, the system MUST prevent the new evaluation from starting and inform the user with a notification (e.g., "An evaluation is already in progress for this contract.").

## Requirements *(mandatory)*

### Functional Requirements

-   **FR-001**: The system MUST display a "Re-evaluate" or similarly-named button on the contract detail view.
-   **FR-002**: Clicking the "Re-evaluate" button MUST open a modal or dedicated interface for rule selection.
-   **FR-003**: The rule selection interface MUST list all active and available evaluation rules for the user's tenant.
-   **FR-004**: Users MUST be able to select one or more rules from the list.
-   **FR-005**: The system MUST provide a confirmation action (e.g., "Run Evaluation" button) to initiate the process.
-   **FR-006**: The system MUST trigger a new evaluation job for the specific contract using the selected rules upon confirmation.
-   **FR-007**: The system MUST provide immediate feedback (e.g., a toast notification) to the user confirming that the evaluation process has begun.
-   **FR-008**: The system MUST record an audit log entry when a user triggers a re-evaluation, including the user, contract, and selected rules.

### Key Entities *(include if feature involves data)*

-   **Contract**: The specific contract being re-evaluated.
-   **Evaluation Rule**: The criteria against which the contract is assessed.
-   **Evaluation Job**: A record representing the execution of an evaluation run for a specific contract and set of rules.

## Constitution Check *(mandatory)*

- [X] **I. Tenant Isolation**: Feature respects multi-tenant boundaries? (The rules and contracts are tenant-specific).
- [ ] **II. AI-Manual Synergy**: Data source (AI/User) is identified? (N/A)
- [X] **III. Rule Governance**: Feature utilizes rule engine for validation? (Core to the feature).
- [X] **IV. Middleware Std**: Uses standard integration protocols?
- [X] **V. Auditability**: Actions are recorded in audit logs? (Triggering a re-evaluation must be an auditable event).
- [X] **VI. E2E Testing**: User stories define clear end-to-end success criteria?
- [X] **VII. Doc Sync**: Specification includes all necessary functional details?
- [X] **VIII. UX/Completeness**: User journey is intuitive and addresses the core need?
- [ ] **IX. Manifest Sync**: Impact on global manifests (features, bugs, api, table) is analyzed?

## Success Criteria *(mandatory)*

### Measurable Outcomes

-   **SC-001**: A user can trigger a re-evaluation for a contract with at least one rule in under 45 seconds from the contract detail page.
-   **SC-002**: The success rate for creating a new evaluation job via this feature is greater than 99.5%.
-   **SC-003**: After implementation, user satisfaction surveys show a >20% increase in scores related to "ease of keeping contract assessments current".
-   **SC-004**: Performance targets for re-evaluation will be benchmarked and defined in a future iteration after the initial implementation.
# Feature Specification: Manual Rule Evaluation

**Feature Branch**: `001-manual-rule-evaluation`  
**Created**: 2026-01-16
**Status**: Draft  
**Input**: User description: "ruleengine里面的规则现在是在什么场景下触发评估的？需要在规则编辑后能够有个手工触发规则评估的方法，可以单条触发或批量触发"

## Key Decisions



Based on user feedback received on 2026-01-16:



1.  **Batch Scope**: A "batch" is a group of rules manually selected by the user.

2.  **Evaluation Context**: The context for a manual evaluation is a list of contracts selected by the user at trigger time.

3.  **Result Destination**: Evaluation results are not displayed on the trigger page but are sent to a "Problem Center" page for review. This may represent a significant scope increase and the "Problem Center" may need to be specified as a separate feature.



## User Scenarios & Testing *(mandatory)*



### User Story 1 - Manually Trigger a Single Rule (Priority: P1)



As a rule administrator, after editing a rule, I want to manually trigger its evaluation against a list of contracts so that I can verify its correctness without waiting for the standard trigger.



**Why this priority**: This is the core functionality requested and provides the most immediate value for testing and debugging individual rules.



**Independent Test**: Can be fully tested by selecting a single rule, selecting one or more contracts, triggering the evaluation, and then verifying the results appear in the Problem Center.



**Acceptance Scenarios**:



1. **Given** a user is viewing an editable rule, **When** they click "Trigger Evaluation" and select a list of contracts, **Then** the system evaluates the rule against the selected contracts and sends the results to the Problem Center page.

2. **Given** a rule has been manually evaluated, **When** the evaluation is complete, **Then** the user can navigate to the Problem Center page to see the results.



---



### User Story 2 - Manually Trigger a Batch of Rules (Priority: P2)



As a rule administrator, I want to select a group of rules and a list of contracts, then trigger their evaluation simultaneously, so that I can efficiently validate a set of related rules.



**Why this priority**: This improves efficiency when working with multiple rules that may have dependencies or are part of the same logical group.



**Independent Test**: Can be tested by selecting several rules, selecting one or more contracts, initiating a batch evaluation, and checking the results in the Problem Center.



**Acceptance Scenarios**:



1. **Given** a user is viewing a list of rules, **When** they select multiple rules, click "Trigger Batch Evaluation", and select a list of contracts, **Then** the system queues all selected rules for evaluation against the selected contracts.

2. **Given** a batch evaluation is in progress, **When** it completes, **Then** the user can view a summary report of the outcomes in the Problem Center.



---



### User Story 3 - View Automatic Trigger Scenarios (Priority: P3)



As a rule administrator, I need to understand when rules are currently triggered automatically so that I can have full context on the rule engine's behavior.



**Why this priority**: This addresses the user's initial question and provides necessary context for understanding the impact of their rules, but is a lower priority than the primary request for manual triggering.



**Independent Test**: Can be tested by navigating to a designated help or documentation section and verifying the information is present and clear.



**Acceptance Scenarios**:



1. **Given** a user is in the rule management interface, **When** they click a "Help" or "Documentation" link regarding triggers, **Then** they are shown a clear explanation of the events or conditions that cause rules to be evaluated automatically.



---



### Edge Cases



- What happens if a manually triggered rule is already running due to an automatic trigger?

- How does the system handle a rule that times out during manual evaluation?

- What happens if a user tries to trigger an evaluation with an invalid or empty selection of rules or contracts?



## Requirements *(mandatory)*



### Functional Requirements



- **FR-001**: System MUST provide a user interface element (e.g., a button) on the rule editing screen to initiate a single rule evaluation.

- **FR-002**: System MUST provide a mechanism in the rule list view to select multiple rules for a batch evaluation.

- **FR-003**: System MUST provide a UI for the user to select one or more contracts to serve as the data context for an evaluation.

- **FR-004**: System MUST send the results of a manual evaluation to a "Problem Center" page for user review. The results should be available for review after the evaluation is complete.

- **FR-005**: A "batch" evaluation consists of a group of rules manually selected by the user from a list.

- **FR-006**: System MUST provide accessible documentation explaining the automatic trigger scenarios for the rule engine.



### Key Entities *(include if feature involves data)*



- **Rule**: Represents a single rule with conditions and actions. Attributes: ID, Name, Definition, Version.

- **Contract**: Represents the data entity that rules are evaluated against.

- **Evaluation Job**: Represents a request to evaluate one or more rules against a set of contracts. Attributes: Job ID, Status (Pending, In-Progress, Completed), Trigger Type (Manual, Automatic), Results.

- **Evaluation Result**: Represents the outcome of a single rule's evaluation. Attributes: Rule ID, Contract ID, Status (Pass, Fail, Error), Details, Timestamp.



## Constitution Check *(mandatory)*



- [X] **I. Tenant Isolation**: Feature respects multi-tenant boundaries? (Assuming evaluations are per-tenant)

- [ ] **II. AI-Manual Synergy**: Data source (AI/User) is identified?

- [X] **III. Rule Governance**: Feature utilizes rule engine for validation? (This feature is *about* the rule engine)

- [ ] **IV. Middleware Std**: Uses standard integration protocols?

- [X] **V. Auditability**: Actions are recorded in audit logs? (Manual trigger actions should be logged)

- [X] **VI. E2E Testing**: User stories define clear end-to-end success criteria?

- [X] **VII. Doc Sync**: Specification includes all necessary functional details?

- [X] **VIII. UX/Completeness**: User journey is intuitive and addresses the core need?

- [ ] **IX. Manifest Sync**: Impact on global manifests (features, bugs, api, table) is analyzed?



## Success Criteria *(mandatory)*



### Measurable Outcomes



- **SC-001**: A user can trigger a single rule evaluation against selected contracts in under 20 seconds (including time to select contracts).

- **SC-002**: A batch evaluation of 50 rules against 10 contracts completes, and results are available in the Problem Center within 5 minutes.

- **SC-003**: 95% of users can successfully find and interpret the results of a manually triggered evaluation in the Problem Center.

- **SC-004**: Users can find and understand the automatic rule trigger scenarios within 60 seconds of looking for the information.

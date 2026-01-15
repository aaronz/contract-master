# Research: Contract Master Core Enhancements

## Field Metadata Unification Strategy

**Decision**: Implement a unified `ContractMetadataService` that aggregates standard JPA fields (from `ContractBase`) and extended fields (from `ContractExtendField`).

**Rationale**:
- Currently, standard fields are hardcoded or managed separately from custom fields.
- User stories require a unified view for Creation, Edit, Card Generator, and Lists.
- A single source of truth prevents inconsistencies across modules.

**Alternatives considered**:
- Maintaining separate lists in frontend: Rejected due to maintenance overhead and risk of desync.
- Database view: Rejected to keep logic application-side for multi-tenant flexibility.

## AI Prompt vs Rule Engine Configuration

**Decision**: Enhance `RuleConfig` entity to include a `ruleType` discriminator (LOGIC vs AI_PROMPT) and specific configuration payloads.

**Rationale**:
- Users need to manage both deterministic logic (Drools/SpEL) and probabilistic AI instructions in one place.
- Reusing the existing `RuleConfig` table avoids schema bloat while supporting the new requirement.

**Alternatives considered**:
- Separate `AiPromptConfig` table: Rejected as it duplicates much of the rule management infrastructure (enable/disable, tenant scope).

## Card Generator Data Source

**Decision**: The Card Generator will consume the `ContractDTO` which already includes a map of extended fields (`extendedFields`). Frontend will iterate over the unified metadata list to render values.

**Rationale**:
- `ContractDTO` structure is already extensible.
- No need for a separate "Card Data" API; reusing the detail DTO ensures data consistency.

## Problem Center Resolution Action

**Decision**: Implement a `resolveIssue` method in `NotificationService` (or a new `IssueService` if scope grows) that updates the status and logs the action.

**Rationale**:
- "Nothing happens" implies a missing backend handler or disconnected frontend event.
- Connecting the "Resolve" button to a status update API is the direct fix.

# Data Model: Gap Analysis Results

## Identfied Discrepancies

### 1. Missing Entity Implementation
- **DownstreamSystem**: Defined in design but implementation is just a skeleton repository.
- **DataPermissionRule**: Present but logic for dynamic SQL generation is missing.

### 2. Schema Gaps
- **ContractBase**: Lacks the `group_id` for higher-level corporate hierarchy support mentioned in the design doc.
- **AuditLog**: The `details` JSON column is missing, which is required for storing field-level diffs.

### 3. State Machine Discrepancy
- The design specifies a state transition: `SYNCED -> AI_PROCESSING -> VERIFIED -> PUBLISHED`.
- **Current Implementation**: The status transitions are handled inconsistently across `ContractService` and `CrmIntegrationService`.

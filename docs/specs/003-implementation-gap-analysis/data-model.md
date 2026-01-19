# Data Model: UI & Journey State Gaps

## Identified Discrepancies in State & Logic

### 1. Contract State Machine
- **Design**: `SYNCED -> AI_SUGGESTED -> HUMAN_VERIFIED -> READY_TO_PUBLISH -> PUBLISHED`.
- **Current Gaps**:
  - `READY_TO_PUBLISH` status is missing from the `ContractStatus` enum.
  - UI `status-badge` only supports a subset of these states.

### 2. Downstream System Mapping
- **Missing Entity fields**:
  - `system_type` (SALESFORCE, HUBSPOT, INTERNAL).
  - `auth_config` (JSONB for API keys/Secrets).
  - `sync_interval` (Cron syntax).

### 3. Verification Metadata
- **Missing in UI state**:
  - `verified_at` (Timestamp).
  - `verified_by` (User ID).
  - `is_overridden` (Boolean flag for manual vs AI).

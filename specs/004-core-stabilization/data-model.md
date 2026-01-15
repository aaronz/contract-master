# Data Model: Stabilization Updates

## Schema Updates

### 1. AuditLog (Enhanced)
- **New Field**: `details` (JSONB)
- **Structure**: `{ "field_name": { "old": "X", "new": "Y" } }`
- **Rationale**: Support granular history reconstruction.

### 2. ContractBase (Lifecycle Support)
- **New Field**: `validation_results` (JSONB)
- **Structure**: `[{ "rule_id": "R001", "level": "SEVERE", "message": "..." }]`
- **New Statuses**: `READY_TO_PUBLISH`, `PUBLISHING`, `PUBLISHED`.

### 3. DownstreamSystem (New Management Entity)
- **Fields**:
  - `system_id` (String, PK)
  - `tenant_id` (String, FK)
  - `system_name` (String)
  - `endpoint_url` (String)
  - `auth_type` (Enum: API_KEY, OAUTH2)
  - `auth_config` (JSONB)
  - `is_enabled` (Boolean)

## State Machine
- **Journey**: `SYNCED -> AI_SUGGESTED -> HUMAN_VERIFIED -> READY_TO_PUBLISH -> PUBLISHED`.
- **Logic**: Transition to `VERIFIED` requires a user confirm action. Transition to `PUBLISHED` requires valid downstream configuration.

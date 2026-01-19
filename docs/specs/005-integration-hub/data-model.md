# Data Model: Integration Hub

## Entities

### 1. IntegrationLog (Extends BaseTenantEntity)
- **Description**: Immutable record of every outbound synchronization attempt.
- **Fields**:
  - `log_id` (Long, PK)
  - `contract_id` (String, FK)
  - `target_system_id` (String, FK)
  - `target_url` (String)
  - `request_payload` (Text/JSONB)
  - `response_body` (Text/JSONB)
  - `http_status` (Integer)
  - `delivery_status` (Enum: SUCCESS, FAILED, RETRYING)
  - `execution_time_ms` (Long)
  - `attempt_number` (Integer)

## Relationships
- One **Contract** has many **IntegrationLog** entries.
- One **DownstreamSystem** has many **IntegrationLog** entries.

# Data Model: UI Stability and Feature Completion

## Existing Entities (Refined)

### Contract (`contract_base`)
- `contract_id` (PK): UUID/String
- `contract_no`: Unique identifier
- `amount`: Numeric(18,2)
- `status`: String (DRAFT, AI_EXTRACTED, VERIFIED, PUBLISHED, RISK_FLAGGED)
- `tenant_id`: String (FK to Tenant)
- *Includes many other standard fields (party names, dates, etc.)*

### Connector (`downstream_system`)
- `system_id` (PK): String
- `system_name`: String
- `access_key`: String (Unique)
- `is_enabled`: Boolean
- `endpoint_url`: String
- `auth_type`: String
- `auth_config`: Text (JSON)

### Field Mapping (`field_mapping`)
- `id` (PK): BigInt
- `internal_field`: String (Target field in `contract_base` or custom code)
- `external_field`: String (Target field in downstream system)
- `transformation`: String (NONE, UPPERCASE, DATE_ISO)
- `is_enabled`: Boolean

### Masking Rule (`data_permission_rule` - reused for masking)
- `rule_id` (PK): String
- `rule_name`: String
- `data_type`: String (CONTRACT)
- `filter_condition`: Text (SpEL or field name)
- `is_enabled`: Boolean

## Virtual Entities (API only)

### DashboardStats
- `totalContracts`: Long
- `pendingApprovals`: Long
- `activeValue`: BigDecimal
- `riskAlerts`: Long
- `volumeTrend`: List<DailyCount>
- `riskRadar`: Map<String, Integer>

## State Transitions

### Contract Status
1. `DRAFT` -> User creates manually or syncs from CRM.
2. `AI_EXTRACTED` -> AI service finishes analysis.
3. `VERIFIED` -> User confirms AI suggestions.
4. `PUBLISHED` -> Data pushed to downstream systems.
5. `RISK_FLAGGED` -> Rule engine detects violation.

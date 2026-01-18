# Data Model: Contract Master Core System

## Entities

### 1. Tenant
- **Description**: Represents an isolated organization using the system.
- **Fields**:
  - `tenant_id` (String, PK): Unique identifier.
  - `tenant_name` (String): Display name.
  - `config` (JSON): Tenant-specific integration settings (API keys, etc.).
  - `status` (Enum): ACTIVE, DISABLED.

### 2. Contract (Extends BaseTenantEntity)
- **Description**: Core contract information synchronized from CRM.
- **Fields**:
  - `contract_id` (String, PK): System-generated unique ID.
  - `crm_id` (String): ID in the source CRM system.
  - `crm_source` (Enum): SALESFORCE, HUBSPOT, DINGTALK.
  - `contract_no` (String): Business contract number.
  - `contract_name` (String): Title of the contract.
  - `party_a_name` (String): Primary signer.
  - `party_b_name` (String): Secondary signer.
  - `amount` (BigDecimal): Contract value.
  - `sign_date` (LocalDate): Date signed.
  - `status` (String): Current lifecycle stage.

### 3. ContractExtendField (Extends BaseTenantEntity)
- **Description**: Definition of custom fields per tenant.
- **Fields**:
  - `field_id` (String, PK): Unique identifier.
  - `field_name` (String): UI Label.
  - `field_type` (Enum): TEXT, NUMBER, DATE, ENUM, ATTACHMENT.
  - `is_required` (Boolean): Validation rule.
  - `default_value` (String): Initial value.

### 4. ContractExtendData (Extends BaseTenantEntity)
- **Description**: Values for extension fields linked to a contract.
- **Fields**:
  - `id` (Long, PK): Internal ID.
  - `contract_id` (String, FK): Linked contract.
  - `field_id` (String, FK): Linked field definition.
  - `field_value` (Text): The actual data.
  - `fill_type` (Enum): AI, MANUAL, SYSTEM.
  - `fill_user` (String): User or AI version identifier.

### 5. RuleConfig (Extends BaseTenantEntity)
- **Description**: Business rules for data validation.
- **Fields**:
  - `rule_id` (String, PK): Unique ID.
  - `rule_name` (String): Display name.
  - `drl_content` (Text): Drools rule syntax.
  - `priority` (Integer): Execution order.
  - `is_enabled` (Boolean): Toggle.

### 6. AuditLog (Extends BaseTenantEntity)
- **Description**: Immutable record of all data modifications.
- **Fields**:
  - `id` (Long, PK): Unique ID.
  - `entity_id` (String): ID of the changed record.
  - `entity_type` (String): Class name of the record.
  - `field_name` (String): Specific field changed.
  - `old_value` (Text): Value before change.
  - `new_value` (Text): Value after change.
  - `action_type` (Enum): CREATE, UPDATE, DELETE.

## Relationships
- One **Tenant** has many **Contract** records.
- One **Tenant** has many **ContractExtendField** definitions.
- One **Contract** has many **ContractExtendData** records (one per field).
- One **Contract** has many **AuditLog** entries.

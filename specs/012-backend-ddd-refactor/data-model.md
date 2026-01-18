# Data Model: Backend DDD Refactor

## Bounded Contexts

### 1. Contract Management Context
Responsible for the lifecycle and core elements of contracts.
- **Aggregates**: Contract
- **Entities**: ContractElement, Attachment
- **Value Objects**: ContractId, Money, PartyInfo, TenantId

### 2. Rule & Compliance Context
Handles the validation and compliance rules.
- **Aggregates**: ValidationRule
- **Entities**: RuleCondition
- **Value Objects**: RuleId, Severity

### 3. Identity & Access Context
Manages users and tenants.
- **Aggregates**: Tenant, User
- **Value Objects**: UserId, Permission

## Entities & Value Objects (DDD)

### **Aggregate Root: Contract**
- **Id**: `ContractId` (UUID)
- **TenantId**: `TenantId` (Value Object)
- **ContractNo**: `String` (Unique business key)
- **Amount**: `Money` (Value Object: amount, currency)
- **Status**: `ContractStatus` (Enum: DRAFT, SIGNED, ACTIVE, TERMINATED)
- **Parties**: `List<PartyInfo>` (Value Object: name, contact, address)
- **Attachments**: `List<Attachment>` (Entity)
- **Validation Results**: `List<RuleResult>` (Internal state)

### **Value Object: Money**
- `BigDecimal amount`
- `String currency`
- Behavior: `add(Money)`, `subtract(Money)`, `validate()`

### **Value Object: TenantId**
- `UUID value`
- Behavior: `equals()`, `toString()`

## Persistence Mapping

The DDD domain models will be mapped to the existing PostgreSQL schema:

| Domain Model | SQL Table | Notes |
|--------------|-----------|-------|
| `Contract` | `contract_base` | Primary mapping |
| `Attachment` | `contract_attachment` | One-to-Many |
| `Rule` | `rule_config` | Rule definitions |
| `Tenant` | `tenant_info` | Isolation root |

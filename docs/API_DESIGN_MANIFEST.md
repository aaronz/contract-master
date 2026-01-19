# API Design Manifest (Synchronized with Backend Implementation)

**Last Updated**: 2025-01-19
**Scope**: Contract Lifecycle, Rule Engine, Problem Center

---

## 1. Contract API (`/api/contracts`)

| Field | Type | Required | Source | Description |
|-------|------|----------|--------|-------------|
| `contractNo` | String | **YES** | Domain | Unique identifier for the contract |
| `contractName` | String | **YES** | Domain | Name/Title of the contract |
| `partyAName` | String | **YES** | Domain | Entity name for Party A |
| `partyBName` | String | **YES** | Domain | Entity name for Party B |
| `contractType` | String | No | DTO | Framework, NDA, etc. |
| `contractAmount` | BigDecimal | No | DTO | Total value |
| `currencyType` | String | No | DTO | USD, CNY, etc. |
| `contractStatus` | String | No | DTO | Defaults to 'DRAFT' on creation |

**Validation Rules**: 
- Backend `Contract` domain constructor requires `contractNo`.
- Frontend `list.vue` enforces `contractNo`, `contractName`, `partyAName`, `partyBName`.

---

## 2. Rule Engine API (`/api/rules`)

| Field | Type | Required | Source | Description |
|-------|------|----------|--------|-------------|
| `name` | String | **YES** | DB/Entity | Display name of the rule |
| `logicType` | Enum | **YES** | DB/Entity | LOGIC, GROOVY, REGEX |
| `logicContent` | String | **YES** | DB/Entity | The actual logic/script/pattern |
| `severity` | Enum | **YES** | DB/Entity | INFO, WARNING, SEVERE |
| `status` | Enum | No | Entity | Defaults to 'DRAFT' |
| `version` | Integer | No | Entity | Defaults to 1 |

**Validation Rules**:
- Backend `Rule.java` has `@Column(nullable = false)` for `name`, `logicType`, `logicContent`, `severity`.

---

## 3. Problem Center Evaluation (`/api/problem-center/evaluations`)

| Field | Type | Required | Source | Description |
|-------|------|----------|--------|-------------|
| `contractId` | UUID | **YES** | Request | The contract to evaluate |

---

## 4. Problem Center Management (`/api/problems`)

### Listing (`GET /api/problems`)
- Parameters: `contractId` (Optional), `status` (Optional)

### Resolution (`POST /api/problems/{id}/resolve`)
- Path variable: `id` (Required)

---

## Implementation Gaps Found
1. **DTO Validation**: Backend DTOs (`ContractDTO`, `EvaluationTriggerRequest`) lack JSR-303 annotations (`@NotNull`, etc.), relying on domain-level or manual checks.
2. **Frontend-Backend Sync**: The `RuleEditorForm.vue` correctly identifies that `name`, `ruleType`, `level`, and `trigger` (mapped to logic/status context) are essential for a functional rule.

# Data Model: Metadata-Driven Extensions

## Entities

### ContractExtendField
- **id**: Long (PK)
- **fieldCode**: String (Unique, e.g., 'contract_manager_email')
- **fieldName**: String (Default label)
- **fieldType**: Enum (TEXT, NUMBER, DATE, SELECT)
- **isRequired**: Boolean
- **tenantId**: String (Isolation)

### ContractExtendData
- **id**: Long (PK)
- **contractId**: String (FK to contract_base)
- **fieldId**: String (FK to contract_extend_field)
- **fieldValue**: TEXT (Raw value)
- **fillType**: Enum (AI, MANUAL)
- **verificationStatus**: Enum (UNVERIFIED, VERIFIED)

### FieldConfig
- **id**: Long (PK)
- **fieldCode**: String (Refers to standard or extended field)
- **fieldAlias**: String (Tenant-specific label)
- **isVisible**: Boolean
- **displayOrder**: Integer
- **requiredRole**: String (RBAC)
- **fieldStyles**: JSON (Colors, fonts)
- **tenantId**: String (Isolation)

## Relationships
- One `Contract` has many `ContractExtendData`.
- One `ContractExtendField` can have values in many `ContractExtendData` (across different contracts).
- `FieldConfig` maps 1:1 to either a hardcoded field in `ContractDTO` or a dynamic `ContractExtendField`.

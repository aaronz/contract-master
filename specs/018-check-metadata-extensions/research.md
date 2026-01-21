# Research: Metadata-Driven Field Extensions

## 1. Metadata Aggregation Logic
- **Decision**: Use Java Reflection on `ContractDTO` to identify core fields and merge them with `ContractExtendField` records from the database.
- **Rationale**: This "Hybrid" approach allows core business logic to remain strongly typed while providing flexibility for tenant-specific data requirements.
- **Alternatives Considered**: 
    - Full Dynamic Schema (JSONB): Rejected because core fields like `contractNo` need structured indexing and validation.
    - Table-per-Tenant: Rejected due to maintenance overhead and migration complexity.

## 2. UI Configuration Overlays
- **Decision**: `FieldConfig` settings are applied as a post-processing step in `MetadataService`.
- **Rationale**: Decoupling the "Definition" (`ContractExtendField`) from "Behavior" (`FieldConfig`) allows for more granular control (e.g., hiding a standard field for a specific tenant without changing the DTO).

## 3. Data Integrity & Type Safety
- **Decision**: Implement a `MetadataValidator` component that checks `ContractExtendData` values against the `field_type` defined in `ContractExtendField`.
- **Rationale**: Prevents dirty data from entering the `TEXT` or `JSON` columns that house the extended data.

## 4. Performance Optimization
- **Decision**: Use a Caffeine cache for unified metadata DTOs, invalidated on any change to `FieldConfig` or `ContractExtendField`.
- **Rationale**: Reflection and multi-table joins for every contract-related request would otherwise introduce significant latency.

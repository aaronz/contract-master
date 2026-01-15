# Research: Implementation Gap Analysis

## Research Items

### 1. Multi-Tenant Isolation Audit
- **Context**: Need to verify if all database interactions are filtered by `tenant_id`.
- **Findings**:
  - The project uses a `TenantFilter` and `TenantContext` to capture `X-Tenant-ID`.
  - Core entities inherit from `BaseTenantEntity`.
  - **Gap**: Some manual JPA queries in `ContractService` and `ExportService` may skip the Hibernate `@Filter` if not explicitly enabled in the session context.
  - **Decision**: Standardize all repository calls to use a custom `@TenantFiltered` annotation or ensure the aspect is globally applied.

### 2. AI-Manual Synergy Implementation
- **Context**: Design requires clear identification of data source (AI vs Manual).
- **Findings**:
  - `ContractExtendData` entity has a `fill_type` field.
  - **Gap**: The frontend UI (`DynamicFieldRenderer.vue`) currently does not display the `fill_type` badge, making it impossible for users to know if a value was AI-suggested or manually entered.
  - **Decision**: Update Vue components to include a source indicator.

### 3. Rule Engine Integration Check
- **Context**: Rules must be executed before contract finalization.
- **Findings**:
  - `DroolsRuleEngine` is present but the `validate` method is empty (placeholder implementation).
  - **Gap**: The `CrmIntegrationService` saves contracts directly without calling the rule engine.
  - **Decision**: Integrate `RuleEngineService` into the `PublishService` and `CrmIntegrationService` flows.

### 4. Audit Log Traceability
- **Context**: Every change must be recorded with field-level diffs.
- **Findings**:
  - `AuditLogInterceptor` is implemented but the `logAction` method is currently a placeholder.
  - **Gap**: Field-level changes are not actually being captured; only the action type is tracked conceptually.
  - **Decision**: Use `EntityListener` or Hibernate Envers to capture real field diffs.

# Research: Deep Implementation Gap Analysis

## Research Items

### 1. Asynchronous Tenant Context Propagation
- **Context**: Verify if `TenantContext` correctly crosses thread boundaries in Kafka listeners and `@Async` methods.
- **Findings**:
  - `ExtractionTaskService.java` currently uses a composite message string `tenantId:contractId`.
  - **Gap**: There is no global `KafkaInterceptor` or `TaskDecorator` to handle this systematically, making it prone to developer error in new listeners.
  - **Decision**: Research implementing a `CustomKafkaPrincipal` or header-based propagation to standardize async isolation.

### 2. UI Data Source Attribution (Vue 3)
- **Context**: Design requires clear "AI vs Manual" badges for all contract element fields.
- **Findings**:
  - `DynamicFieldRenderer.vue` has initial logic for `fill_type` but lacks verification "Accept/Reject" controls.
  - **Gap**: Users cannot currently "lock in" an AI suggestion, leaving the data in a permanent unverified state in the UI.
  - **Decision**: Update Vue components to support a `VERIFIED` state transition.

### 3. Rule Engine Integration mandatory gates
- **Context**: Business rules must block or flag publication.
- **Findings**:
  - `DroolsRuleEngine.java` has a basic large-amount check.
  - **Gap**: The rule engine results are stored in a `remark` field instead of a structured `validation_results` JSON column, making multi-rule violation displays difficult.
  - **Decision**: Propose adding a structured `validation_results` field to `Contract`.

### 4. Field-Level Audit Diffing
- **Context**: Capturing "Old" and "New" values for every change.
- **Findings**:
  - `AuditLogInterceptor.java` creates an entry but requires reflection to capture field diffs.
  - **Gap**: Deep hierarchies (extended fields) are not being diffed accurately.
  - **Decision**: Research using Hibernate `PostUpdateEventListener` which provides access to `Object[] state` and `Object[] oldState`.

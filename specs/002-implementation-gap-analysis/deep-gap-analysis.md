# Deep Gap Analysis Report: Contract Master (Second Audit)

## 1. Executive Summary
This report follows the second, deeper audit of the Contract Master middleware. While the core features are functional, this deep-level inspection has uncovered significant technical gaps in asynchronous tenant isolation and the lifecycle of AI-extracted data.

## 2. Deep Audit Gaps

### 2.1 Asynchronous Boundary Isolation (Critical)
- **Problem**: Kafka listeners do not automatically inherit `TenantContext`.
- **Finding**: `ExtractionTaskService.java` relies on manual string parsing. New listeners could easily bypass isolation.
- **Remediation**: Implement a header-based propagation filter for all messaging boundaries.

### 2.2 AI Verification Lifecycle (High)
- **Problem**: The UI has no "Verification" status for AI data.
- **Finding**: `DynamicFieldRenderer.vue` shows the source but lacks a confirmation toggle.
- **Remediation**: Add a `VERIFIED` state to the `fill_type` enum and update the UI.

### 2.3 Rule Engine Enforceability (Medium)
- **Problem**: Drools results are non-structured.
- **Finding**: Validation errors are appended to a flat `remark` string.
- **Remediation**: Add a `validation_results` JSONB column to the database schema.

### 2.4 Audit Log Diffing (Medium)
- **Problem**: No actual value diffing in logs.
- **Finding**: `AuditLogInterceptor.java` is a concept-only implementation.
- **Remediation**: Use Hibernate Event Listeners to capture real property changes.

# Contract Master Core Principles

Based on the [Project Constitution](../../.specify/memory/constitution.md).

## 1. Transparent Tenant Isolation
- **Foundation**: Logic multi-tenancy is critical.
- **Rule**: Every DB query, service call, and task MUST automatically respect `tenant_id`.
- **Constraint**: Manual `tenant_id` handling in business logic is **FORBIDDEN**. Use Hibernate Filters & AOP.

## 2. Full-Link Context Propagation
- **Requirement**: Tenant context MUST propagate across HTTP Headers, Async Threads (TaskDecorator), and Kafka Headers.
- **Goal**: Preserve tenant identity throughout the request lifecycle without manual intervention.

## 3. Unified Metadata-Driven Design
- **Requirement**: Standard and Extended fields MUST be unified at the service layer.
- **Dynamic Generation**: Validation, UI, Exports, and AI prompts MUST be generated from metadata.

## 4. AI-Manual Synergy & Traceability
- **Suggestive AI**: AI data is "suggested" until human/rule verification.
- **Traceability**: Every modification MUST be traceable to an actor (User or AI model version).

## 5. Absolute Auditability
- **Requirement**: Immutable audit logs for every change (data, config, permissions).
- **Implementation**: Automated capture via `TenantEntityListener` or `AuditLogInterceptor`.

## 6. Continuous Documentation
- **Rule**: Design artifacts (spec.md, plan.md, tasks.md) are living documents.
- **Sync**: Divergence must be resolved in the same feature branch.

## 7. Global Manifest Maintenance
- **Requirement**: Always refresh `features.md`, `bugs.md`, `api.md`, and `table.md` with relevant changes.

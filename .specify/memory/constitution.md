# Contract Master (合同全量要素系统) Constitution

## Core Principles

### I. Transparent Tenant Isolation
Logic multi-tenancy is the foundation of the system. Every database query, service call, and
background task MUST automatically respect the current `tenant_id` context. Isolation is enforced
transparently at the infrastructure level (Hibernate Filters & AOP); manual `tenant_id` handling in
business logic is forbidden to prevent data leakage and reduce boilerplate.

### II. Full-Link Context Propagation
Multi-tenant context MUST be automatically propagated across all execution boundaries, including
Web entries (HTTP Headers), Asynchronous Threads (TaskDecorator), and Message Queues (Kafka Headers).
The system ensures that the triggering actor's tenant identity is preserved throughout the entire
request lifecycle without manual intervention.

### III. Unified Metadata-Driven Design
The system architecture is driven by a hybrid metadata model. Core fields (Standard) and dynamic
custom fields (Extended) MUST be unified at the service layer. Data validation, UI rendering,
CSV exports, and AI extraction prompts MUST be dynamically generated from this metadata. This ensures
that per-tenant requirements are met through configuration rather than code changes.

### IV. AI-Manual Synergy & Traceability
AI-extracted data is treated as "suggested" until human verification or rule-based confirmation.
Both AI and manual sources MUST be clearly identified in the data model. Every modification to
contract elements MUST be traceable to an actor (User or AI model version).

### V. Rule-Driven Data Governance
All contract data MUST pass through the centralized rule engine before being published or considered
"Complete". Risks and inconsistencies MUST be flagged with explicit severity levels (Info, Warning,
Error) to ensure data quality and compliance.

### VI. Middleware Standardization
Integration with external SaaS CRMs and internal downstream systems MUST use standardized API
models and WebHooks. Decoupling through abstraction layers is mandatory to support diverse
integrations without compromising core system stability.

### VII. Absolute Auditability
Every change to contract data, configuration, or permissions MUST be recorded in an immutable audit
log. Logs MUST include the timestamp, actor ID, field changed, original value, and new value,
supporting full history reconstruction. Automated capture via Entity Listeners and AOP is preferred.

### VIII. Continuous Design Document Optimization
Design artifacts (spec.md, plan.md, tasks.md) are "living documents". They MUST be updated whenever
implementation diverges from the original plan or when new constraints are discovered. Outdated
documentation is a technical debt that must be resolved within the same feature branch.

### IX. UX & Functional Completeness Reflection
Developers MUST actively reflect on the user experience and functional completeness during
development. If a planned feature is found to be confusing, incomplete, or technically redundant, it
MUST be challenged and refined before completion. MVP doesn't mean "broken" or "confusing"; it means
"minimal but high quality".

### X. Maintenance of Global Manifests
The project maintains four critical global manifests: `features.md` (Features), `bugs.md` (Known Bugs),
`api.md` (API Registry), and `table.md` (Database Schema). These files MUST be refreshed with every
relevant code change (feature add, bug fix, API change, schema change) to ensuring a real-time,
accurate view of the system state.

## Technology Stack & Performance Standards

- **Backend**: Java 17, Spring Boot 3.2.x, Spring Data JPA.
- **Frontend**: Vue 3, Element Plus, Web Components for CRM embedding.
- **Storage**: PostgreSQL (Relational), Redis (Cache), MinIO (Object/Files).
- **Messaging**: Kafka for asynchronous event-driven updates.
- **Performance**: P95 response time for core APIs < 500ms; Data sync latency < 3s.

## Development Workflow & Quality Gates

- **Security**: Transparent logical isolation via Hibernate Filters and AOP. No manual `tenant_id` parameters in Repositories.
- **Propagation**: Automatic context propagation across Web, Async Threads, and Kafka boundaries.
- **Audit**: All entity modifications MUST be captured via `TenantEntityListener` or `AuditLogInterceptor`.
- **Documentation**: All feature merges must trigger updates to global manifests (`features.md`, `bugs.md`, `api.md`, `table.md`) as per Principle X.
- **Review**: Every PR must verify adherence to all ten Core Principles and ensure design docs are in sync with code.

## Governance

1. The Constitution is the supreme authority for project architectural and quality decisions.
2. Amendments to the Constitution require a formal Pull Request and approval from at least two lead
   maintainers.
3. Every feature specification (`spec.md`) and implementation plan (`plan.md`) MUST include a
   "Constitution Check" section.
4. Versioning follows Semantic Versioning (SemVer) rules.

**Version**: 1.4.0 | **Ratified**: 2026-01-21 | **Last Amended**: 2026-01-21

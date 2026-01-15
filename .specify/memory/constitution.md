<!--
Sync Impact Report:
- Version change: 1.0.0 → 1.1.0
- List of modified principles:
  - Principle 6: VI. Comprehensive End-to-End Testing (Added)
  - Principle 7: VII. Continuous Design Document Optimization (Added)
  - Principle 8: VIII. UX & Functional Completeness Reflection (Added)
- Added sections: none (extended Core Principles)
- Removed sections: none
- Templates requiring updates:
  - .specify/templates/plan-template.md (✅ updated)
  - .specify/templates/spec-template.md (✅ updated)
  - .specify/templates/tasks-template.md (✅ updated)
- Follow-up TODOs: none
-->

# Contract Master (合同全量要素系统) Constitution

## Core Principles

### I. Tenant Isolation First
Every database query, service call, and configuration access MUST include a validated `tenant_id`
context. Logic multi-tenancy is the foundation of the system; cross-tenant data leakage is a
critical security failure that must be prevented at the architectural level.

### II. AI-Manual Synergy & Traceability
AI-extracted data is treated as "suggested" until human verification or rule-based confirmation.
Both AI and manual sources MUST be clearly identified in the data model. Every modification to
contract elements MUST be traceable to an actor (User or AI model version).

### III. Rule-Driven Data Governance
All contract data MUST pass through the centralized rule engine before being published or considered
"Complete". Risks and inconsistencies MUST be flagged with explicit severity levels (Info, Warning,
Error) to ensure data quality and compliance.

### IV. Middleware Standardization
Integration with external SaaS CRMs and internal downstream systems MUST use standardized API
models and WebHooks. Decoupling through abstraction layers is mandatory to support diverse
integrations without compromising core system stability.

### V. Absolute Auditability
Every change to contract data, configuration, or permissions MUST be recorded in an immutable audit
log. Logs MUST include the timestamp, actor ID, field changed, original value, and new value,
supporting full history reconstruction.

### VI. Comprehensive End-to-End Testing
Every user story MUST be validated with an end-to-end test covering the full journey from trigger to
final outcome. E2E tests MUST run in a production-like environment (e.g., staging) to ensure all
integrations (CRM, AI, Rule Engine) function correctly together.

### VII. Continuous Design Document Optimization
Design artifacts (spec.md, plan.md, tasks.md) are "living documents". They MUST be updated whenever
implementation diverges from the original plan or when new constraints are discovered. Outdated
documentation is a technical debt that must be resolved within the same feature branch.

### VIII. UX & Functional Completeness Reflection
Developers MUST actively reflect on the user experience and functional completeness during
development. If a planned feature is found to be confusing, incomplete, or technically redundant, it
MUST be challenged and refined before completion. MVP doesn't mean "broken" or "confusing"; it means
"minimal but high quality".

## Technology Stack & Performance Standards

- **Backend**: Java 17, Spring Boot 3.2.x, Spring Data JPA.
- **Frontend**: Vue 3, Element Plus, Web Components for CRM embedding.
- **Storage**: PostgreSQL (Relational), Redis (Cache), MinIO (Object/Files).
- **Messaging**: Kafka for asynchronous event-driven updates.
- **Performance**: P95 response time for core APIs < 500ms; Data sync latency < 3s.

## Development Workflow & Quality Gates

- **Security**: Mandatory `tenant_id` filtering in all Repository layers.
- **Testing**: Minimum 80% coverage for core business logic + mandatory E2E coverage for all P1/P2 user stories.
- **Audit**: All entity modifications must be captured via `TenantEntityListener` or equivalent automated mechanisms.
- **Review**: Every PR must verify adherence to all eight Core Principles and ensure design docs are in sync with code.

## Governance

1. The Constitution is the supreme authority for project architectural and quality decisions.
2. Amendments to the Constitution require a formal Pull Request and approval from at least two lead
   maintainers.
3. Every feature specification (`spec.md`) and implementation plan (`plan.md`) MUST include a
   "Constitution Check" section.
4. Versioning follows Semantic Versioning (SemVer) rules.

**Version**: 1.1.0 | **Ratified**: 2026-01-14 | **Last Amended**: 2026-01-14

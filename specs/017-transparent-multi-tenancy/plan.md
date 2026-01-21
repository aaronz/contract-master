# Implementation Plan: Transparent Multi-tenancy

**Branch**: `017-transparent-multi-tenancy` | **Date**: 2026-01-21 | **Spec**: [/specs/017-transparent-multi-tenancy/spec.md](./spec.md)

**Input**: Feature specification from `/specs/017-transparent-multi-tenancy/spec.md`

## Summary

The primary requirement is to make multi-tenancy logic transparent in the backend, ensuring full-link context propagation (Web -> Async -> Kafka -> DB). The technical approach involves using Hibernate Filters for automatic data isolation, a ThreadLocal-based `TenantContext`, JPA Entity Listeners for automatic data tagging, and propagation mechanisms for asynchronous execution (TaskDecorator and Kafka Interceptors).

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Boot 3.2.x, Spring Data JPA (Hibernate 6.x), Spring Kafka 3.x  
**Storage**: PostgreSQL  
**Testing**: JUnit 5, MockMvc, Playwright (E2E)  
**Target Platform**: Linux server / Docker  
**Project Type**: Web application (Backend + Frontend)  
**Performance Goals**: P95 response time < 500ms, Kafka latency < 3s  
**Constraints**: Absolute tenant isolation via `tenant_id`, zero data leakage  
**Scale/Scope**: 100+ concurrent tenants, 10k+ contracts per tenant

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] **I. Tenant Isolation**: Does this feature maintain strict logical isolation?
- [x] **II. AI-Manual Synergy**: Are data sources and actors traceable?
- [x] **III. Rule Governance**: Does this integrate with the rule engine for validation?
- [x] **IV. Middleware Std**: Does it use standardized APIs/WebHooks for integration?
- [x] **V. Auditability**: Are all state changes captured in audit logs?
- [x] **VI. E2E Testing**: Is there an E2E test plan for every user story journey?
- [x] **VII. Doc Sync**: Plan includes tasks to keep design docs updated?
- [x] **VIII. UX/Completeness**: Has the user journey been reviewed for clarity and logic?
- [x] **IX. Manifest Sync**: Plan includes tasks to update global manifests (features, bugs, api, table)?

## Project Structure

### Documentation (this feature)

```text
specs/017-transparent-multi-tenancy/
├── plan.md              # This file
├── research.md          # Phase 0 output
├── data-model.md        # Phase 1 output
├── quickstart.md        # Phase 1 output
├── contracts/           # Phase 1 output
└── tasks.md             # Phase 2 output (generated later)
```

### Source Code (repository root)

```text
backend/
├── src/
│   ├── main/java/com/contract/master/
│   │   ├── config/             # Kafka and Async configuration
│   │   ├── security/           # TenantContext, Filters, Aspects
│   │   └── shared/domain/model/ # BaseTenantEntity
└── tests/

frontend/
├── src/
│   └── utils/request.js       # Axios interceptor for tenant header
```

**Structure Decision**: Web application structure. Backend changes focus on the security and configuration layers to provide cross-cutting multi-tenancy support. Frontend changes are minimal (ensure header propagation).


## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| None | N/A | N/A |

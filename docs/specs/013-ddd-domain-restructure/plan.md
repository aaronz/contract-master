# Implementation Plan: DDD Domain Restructure

**Branch**: `013-ddd-domain-restructure` | **Date**: 2026-01-18 | **Spec**: specs/013-ddd-domain-restructure/spec.md
**Input**: Feature specification from `/specs/013-ddd-domain-restructure/spec.md`

## Summary
Refactor the backend codebase to a domain-centric structure. Each domain module (e.g., `contract`, `identity`) will contain its own `application`, `domain`, `infrastructure`, `interface`, and `dto` layers. The current top-level global layered packages will be eliminated, and their contents will be migrated to the appropriate domain modules or a new `shared` module.

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Boot 3.2.1, Spring Data JPA, Kafka  
**Storage**: PostgreSQL, Redis, MinIO  
**Testing**: JUnit 5, AssertJ  
**Target Platform**: Linux server
**Project Type**: Web application (Backend)  
**Performance Goals**: P95 response time < 500ms, Data sync latency < 3s  
**Constraints**: Maintain 100% existing API compatibility, no circular dependencies between domains  
**Scale/Scope**: Refactor all core domains (contract, identity, notification, dashboard, evaluation, etc.)

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] **I. Tenant Isolation**: This refactor maintains `tenant_id` context usage within each domain's infrastructure layer.
- [x] **II. AI-Manual Synergy**: Domain objects will continue to identify data sources clearly.
- [x] **III. Rule Governance**: Business rules remain centralized within the `domain` layer of each module.
- [x] **IV. Middleware Std**: Standard integration protocols (Kafka, REST) are preserved during the move.
- [x] **V. Auditability**: Audit logic will be migrated to appropriate domain/application layers.
- [x] **VI. E2E Testing**: Regressions will be verified using existing automated tests.
- [x] **VII. Doc Sync**: This plan includes tasks for updating architecture documentation.
- [x] **VIII. UX/Completeness**: The refactor aims for a more intuitive developer experience.
- [x] **IX. Manifest Sync**: Global manifests will be updated to reflect the new package structure.

## Project Structure

### Documentation (this feature)

```text
specs/013-ddd-domain-restructure/
├── plan.md              # This file
├── research.md          # Domain-centric patterns and migration strategy
├── data-model.md        # Mapping of domain modules and boundaries
├── quickstart.md        # Guide for new domain creation
├── contracts/           # API contract verification
└── tasks.md             # Implementation tasks
```

### Source Code (repository root)

```text
backend/
├── src/
│   ├── main/java/com/contract/master/
│   │   ├── shared/         # Shared domain logic, generic VOs, common infrastructure
│   │   │   ├── domain/base/ # AggregateRoot, Entity, ValueObject
│   │   │   └── domain/model/# Money, TenantId
│   │   ├── contract/       # Contract Management Domain
│   │   │   ├── application/
│   │   │   ├── domain/
│   │   │   ├── infrastructure/
│   │   │   ├── interface/
│   │   │   └── dto/
│   │   ├── identity/       # Identity & Access Domain (User, Tenant)
│   │   │   ├── [same structure]
│   │   ├── notification/   # Notification Domain
│   │   │   ├── [same structure]
│   │   └── ...             # Other domains (evaluation, ai, etc.)
│   └── test/java/com/contract/master/
│       ├── unit/           # Domain unit tests organized by module
│       └── integration/    # Infrastructure/API tests organized by module
```

**Structure Decision**: Hexagonal Architecture flavor (Domain-Centric) implemented as vertical slices. We will leverage **Spring Application Events** for cross-domain side effects (like cache invalidation) to minimize tight coupling.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| Package Restructure | To align with DDD vertical slicing | Layered approach leads to "God packages" and difficult navigation in large projects |
| Migration Complexity | Existing logic is mixed | Starting from scratch is not viable for a production system |
| Duplicate Models | Temporary legacy support | Immediate deletion would break too many downstream services at once |
| Event-Driven Cache | Break Contract-Metadata cycle | Direct method calls create a hard circular dependency |

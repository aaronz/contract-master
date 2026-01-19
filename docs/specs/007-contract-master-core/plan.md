# Implementation Plan: Contract Master Core System

**Branch**: `001-contract-master-core` | **Date**: 2026-01-14 | **Spec**: [specs/001-contract-master-core/spec.md]
**Input**: Feature specification from `/specs/001-contract-master-core/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/commands/plan.md` for the execution workflow.

## Summary

This plan outlines the implementation of the Contract Master Core System, a middleware hub connecting SaaS CRMs with internal business logic. The system will provide real-time data synchronization, AI-powered field extraction, and rule-driven compliance validation. The technical approach leverages Java 17 with Spring Boot 3.2.x, PostgreSQL for relational data, Redis for caching, and Kafka for event-driven integration. Multi-tenant isolation and absolute auditability are baked into the architectural foundation.

## Technical Context

**Language/Version**: Java 17, Vue 3
**Primary Dependencies**: Spring Boot 3.2.x, Spring Data JPA, Spring AI (OpenAI/Azure), Drools 9.x, Kafka, Element Plus
**Storage**: PostgreSQL, Redis, MinIO
**Testing**: JUnit 5, Mockito, Playwright (E2E)
**Target Platform**: Linux Server (Docker/Kubernetes)
**Project Type**: web application
**Performance Goals**: API P95 < 500ms, Sync Latency < 3s
**Constraints**: Logical tenant isolation via `tenant_id`, Immutable audit logs
**Scale/Scope**: 100+ tenants, 10k+ contracts per tenant

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] **I. Tenant Isolation**: All entities extend `BaseTenantEntity`; all repositories filtered by `tenant_id`.
- [x] **II. AI-Manual Synergy**: Data model includes source attribution and modification history.
- [x] **III. Rule Governance**: All data passes through Drools engine before finalization.
- [x] **IV. Middleware Std**: RESTful API design following OpenApi 3.0; WebHook signature verification.
- [x] **V. Auditability**: `AuditLog` captures field-level diffs for every update.
- [x] **VI. E2E Testing**: Playwright scenarios covering CRM -> Extraction -> Validation flow.
- [x] **VII. Doc Sync**: Plan includes updating spec and data-model during implementation.
- [x] **VIII. UX/Completeness**: AI verification UI designed for rapid human correction.

## Project Structure

### Documentation (this feature)

```text
specs/001-contract-master-core/
├── plan.md              # This file
├── research.md          # Phase 0 output
├── data-model.md        # Phase 1 output
├── quickstart.md        # Phase 1 output
├── contracts/           # Phase 1 output
└── tasks.md             # Phase 2 output
```

### Source Code (repository root)

```text
backend/
├── src/
│   ├── main/
│   │   ├── java/com/contract/master/
│   │   │   ├── api/             # Controllers
│   │   │   ├── service/         # Business logic
│   │   │   ├── domain/          # Entities & Repositories
│   │   │   ├── rule/            # Drools logic
│   │   │   ├── integration/     # CRM WebHooks & APIs
│   │   │   └── ai/              # AI Extraction logic
│   │   └── resources/
│   └── test/
frontend/
├── src/
│   ├── components/
│   ├── views/
│   ├── api/
│   └── store/
└── tests/
```

**Structure Decision**: Option 2: Web application. The project is split into `backend/` and `frontend/` directories.

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| None | | |

# Implementation Plan: Pull-Model Integration API

**Branch**: `020-pull-model-integration-api` | **Date**: 2026-01-21 | **Spec**: [/specs/020-pull-model-integration-api/spec.md](./spec.md)
**Input**: Feature specification from `/specs/020-pull-model-integration-api/spec.md`

## Summary

This feature implements a Pull-Model API allowing downstream systems to retrieve contract data using an `X-API-KEY`. The core mechanism leverages the existing `FieldMapping` metadata and `ScriptSandbox` (Groovy) to dynamically transform contract data into the format required by each specific downstream system. Access is strictly controlled via API Keys linked to `DownstreamSystem` entities, ensuring multi-tenant isolation and data masking.

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Boot 3.2.x, Spring Data JPA, Groovy 4.x (via ScriptSandbox)  
**Storage**: PostgreSQL  
**Testing**: JUnit 5, MockMvc  
**Target Platform**: Linux server / Docker  
**Project Type**: Web application (Backend API)  
**Performance Goals**: < 500ms latency for 100 transformed records  
**Constraints**: Absolute tenant isolation, automated data masking based on mapping definitions  
**Scale/Scope**: Support polling from multiple concurrent downstream systems

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] **I. Transparent Tenant Isolation**: Isolation is enforced at the infrastructure level (API Key -> TenantContext).
- [x] **II. Full-Link Context Propagation**: Tenant context is populated from the API Key before data retrieval.
- [x] **III. Unified Metadata-Driven Design**: Response schema is dynamically generated from `FieldMapping` metadata.
- [x] **IV. AI-Manual Synergy**: Data sources are identifying in the response if mapped.
- [x] **V. Rule-Driven Data Governance**: Outbound data follows tenant-specific mapping rules.
- [x] **VI. Middleware Standardization**: Uses standard `X-API-KEY` header and REST patterns.
- [x] **VII. Absolute Auditability**: Every pull attempt is recorded in `IntegrationLog`.
- [x] **VIII. UX/Completeness**: Provides a robust alternative for systems not supporting Webhooks.
- [x] **IX. Manifest Sync**: Plan includes updating `api.md`.

## Project Structure

### Documentation (this feature)

```text
specs/020-pull-model-integration-api/
├── plan.md              # This file
├── research.md          # Phase 0 output
├── data-model.md        # Phase 1 output
├── quickstart.md        # Phase 1 output
├── contracts/           # Phase 1 output
└── tasks.md             # Phase 2 output
```

### Source Code (repository root)

```text
backend/src/main/java/com/contract/master/
├── integration/
│   ├── infrastructure/security/  # IntegrationApiKeyInterceptor (New)
│   ├── interfaces/rest/          # IntegrationPullController (New)
│   └── application/              # IntegrationPullService (New)
└── config/                       # WebConfig update for Interceptor
```

**Structure Decision**: Standard backend-heavy structure within the `integration` module.


## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| [e.g., 4th project] | [current need] | [why 3 projects insufficient] |
| [e.g., Repository pattern] | [specific problem] | [why direct DB access insufficient] |

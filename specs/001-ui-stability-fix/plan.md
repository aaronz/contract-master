# Implementation Plan: UI Stability and Feature Completion

**Branch**: `001-ui-stability-fix` | **Date**: 2026-01-15 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/001-ui-stability-fix/spec.md`

## Summary
The system currently has several high-visibility UI bugs and missing data connections in the Dashboard, Contract Management, and Integration modules. This plan addresses these issues by implementing a real-time Dashboard API, connecting frontend components to backend persistence, enabling server-side pagination, and exposing the full contract metadata for configuration.

## Technical Context

**Language/Version**: Java 17, Vue 3  
**Primary Dependencies**: Spring Boot 3.2.x, Spring Data JPA, Element Plus  
**Storage**: PostgreSQL, Redis, MinIO  
**Testing**: JUnit 5, Playwright E2E  
**Target Platform**: Linux/WASM  
**Project Type**: Web Application  
**Performance Goals**: P95 API response < 500ms, UI load < 1s  
**Constraints**: Tenant isolation in all queries, Audit logging for all changes  
**Scale/Scope**: 5 major modules, ~15 UI components to be fixed.

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] **I. Tenant Isolation**: All new dashboard and pagination queries will include mandatory `tenant_id` filtering.
- [x] **II. AI-Manual Synergy**: AI data sources and user verification state will be correctly displayed and persisted.
- [x] **III. Rule Governance**: New contract creation will trigger the Drools/SpEL rule engine validation.
- [x] **IV. Middleware Std**: Integration hub will use standardized API patterns for connector CRUD.
- [x] **V. Auditability**: All manual updates to contracts and configuration will be recorded in audit logs.
- [x] **VI. E2E Testing**: Every fixed user journey will have a corresponding Playwright test case.
- [x] **VII. Doc Sync**: Plan includes tasks to update `data-model.md` and API contracts.
- [x] **VIII. UX/Completeness**: Addressing reported bugs directly improves functional completeness.

## Project Structure

### Documentation (this feature)

```text
specs/001-ui-stability-fix/
├── plan.md              # This file
├── research.md          # Dashboard, Export, and Pagination decisions
├── data-model.md        # Updated Contract and Virtual entities
├── quickstart.md        # Dev environment setup
├── contracts/           # API contracts for Dashboard and Metadata
│   └── dashboard.yaml
└── tasks.md             # Implementation tasks (Phase 2)
```

### Source Code (repository root)

```text
backend/
├── src/main/java/com/contract/master/
│   ├── api/             # Added DashboardController
│   ├── service/         # Added DashboardService
│   └── domain/          # Updated Entity models
└── src/test/java/com/contract/master/

frontend/
├── src/
│   ├── views/           # Fixed Dashboard, Contract List, Integrations
│   ├── layout/          # Fixed Sidebar tenant display
│   └── services/        # Added Dashboard and Metadata API calls
└── tests/
```

**Structure Decision**: Standard Spring Boot + Vue 3 layout (Option 2).

## Phase 0: Research Findings
- See [research.md](./research.md) for full details.
- **Dashboard**: Use real-time SQL aggregation instead of mock data.
- **Export**: Implement CSV for first-phase report export.
- **Paging**: Connect `el-pagination` to backend `Pageable` endpoints.
- **Metadata**: Create a unified field metadata service.

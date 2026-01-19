# Implementation Plan: Core Middleware Stabilization

**Branch**: `004-core-stabilization` | **Date**: 2026-01-15 | **Spec**: [/specs/004-core-stabilization/spec.md]
**Input**: Feature specification from `/specs/004-core-stabilization/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/commands/plan.md` for the execution workflow.

## Summary

This feature focuses on stabilizing the Contract Master core middleware by resolving critical gaps identified in previous audits. Technical priorities include implementing a global `KafkaHeaderInterceptor` for automated tenant context propagation, closing the UI verification loop with a "Confirm" workflow, upgrading audit logs to capture field-level JSONB diffs, and providing a self-service management interface for downstream integrations.

## Technical Context

**Language/Version**: Java 17, Vue 3
**Primary Dependencies**: Spring Boot 3.2.x, Spring Data JPA, Spring Kafka, Hibernate 6.x, Element Plus, Playwright
**Storage**: PostgreSQL (with JSONB), Redis, MinIO
**Testing**: JUnit 5, Playwright (E2E)
**Target Platform**: Linux/Cloud
**Project Type**: web application
**Performance Goals**: <1s audit log retrieval; <200ms UI state transitions.
**Constraints**: Zero cross-tenant leakage in async tasks; Immutable field-level audit trails.
**Scale/Scope**: 100% core entity coverage for auditing; All Kafka listeners covered by interceptors.

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] **I. Tenant Isolation**: Global Kafka interceptor ensures zero isolation bypass in async tasks.
- [x] **II. AI-Manual Synergy**: UI supports explicit confirmation/override workflow with source flags.
- [x] **III. Rule Governance**: Drools outcomes are structured (JSONB) and visual in UI.
- [x] **IV. Middleware Std**: Downstream integrations managed via standard CRUD UI.
- [x] **V. Auditability**: Real field-level diffs (Old vs New) captured via Hibernate listeners.
- [x] **VI. E2E Testing**: Playwright probes cover the full "Sync-Verify-Publish" journey.
- [x] **VII. Doc Sync**: Plan includes final updates to architecture and design docs.
- [x] **VIII. UX/Completeness**: core journeys are seamless within the UI (no manual DB steps).


## Project Structure

### Documentation (this feature)

```text
specs/004-core-stabilization/
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
├── src/main/java/com/contract/master/
│   ├── config/          # Global Interceptors
│   ├── domain/          # JSONB Schema updates
│   ├── security/        # Hibernate Event Listeners
│   └── service/         # Verification Logic
frontend/
├── src/views/settings/  # Downstream Management
└── src/components/      # Verification UI
```

**Structure Decision**: Option 2: Web application. The stabilization requires synchronized changes across backend async boundaries and frontend journey controls.

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| None | | |

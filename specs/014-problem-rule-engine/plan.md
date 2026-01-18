# Implementation Plan: Problem Center and Rule Engine Design

**Branch**: `014-problem-rule-engine` | **Date**: 2026-01-18 | **Spec**: /specs/014-problem-rule-engine/spec.md
**Input**: Feature specification from `/specs/014-problem-rule-engine/spec.md`

## Summary

Refactor and implement the "Problem Center" and "Rule Engine" architecture. This involves establishing a clear relationship where Rules act as "discoverers" using various logic types (Groovy, Regex) and the Problem Center acts as the "cockpit" for tracking and resolving the resulting compliance issues. The technical approach leverages Kafka for asynchronous evaluation jobs and JSONB for precise contract localization.

## Technical Context

**Language/Version**: Java 17 (Backend), JavaScript/Vue 3 (Frontend)  
**Primary Dependencies**: Spring Boot 3.2.x, Spring Data JPA, Kafka, PDF.js, Element Plus  
**Storage**: PostgreSQL (with JSONB support), Redis, MinIO  
**Testing**: JUnit 5, Mockito, Playwright  
**Target Platform**: Linux server  
**Project Type**: Web application  
**Performance Goals**: P95 response time < 500ms, Single contract evaluation < 5s  
**Constraints**: Precise PDF localization (page/offset), Asynchronous evaluation flow  
**Scale/Scope**: Multi-tenant, support for 50+ rules per contract

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] **I. Tenant Isolation**: Every Rule, Problem, and Job will be scoped by `tenant_id`.
- [x] **II. AI-Manual Synergy**: Rule logic types will distinguish between AI-generated and system-predefined logic.
- [x] **III. Rule Governance**: All contract risk identification is centralized through the Rule Engine.
- [x] **IV. Middleware Std**: Asynchronous processing via Kafka events.
- [x] **V. Auditability**: State changes in Problems (e.g., RESOLVED) will be recorded in audit logs.
- [x] **VI. E2E Testing**: Acceptance scenarios define the full path from rule creation to problem resolution.
- [x] **VII. Doc Sync**: Design artifacts will be updated during the restructure.
- [x] **VIII. UX/Completeness**: Precise localization and Resizable Split Panes layout are prioritized for the user journey.
- [x] **IX. Manifest Sync**: Database schema and API registry will be updated in `table.md` and `api.md`.

## Project Structure

### Documentation (this feature)

```text
specs/014-problem-rule-engine/
├── plan.md              # This file
├── research.md          # Phase 0: Logic engines and localization strategy
├── data-model.md        # Phase 1: Rule, Problem, EvaluationJob schemas
├── quickstart.md        # Phase 1: Integration scenarios
├── contracts/           # Phase 1: REST API definitions
└── tasks.md             # Phase 2: Implementation tasks
```

### Source Code (repository root)

```text
backend/
├── src/main/java/com/contract/master/
│   ├── rule/            # Rule Engine Context
│   │   ├── domain/
│   │   ├── application/
│   │   ├── infrastructure/
│   │   └── interface/
│   ├── problemcenter/   # Problem Center Context
│   │   ├── domain/
│   │   ├── application/
│   │   ├── infrastructure/
│   │   └── interface/
│   └── shared/          # Kafka events, shared VOs
└── src/test/java/...

frontend/
├── src/
│   ├── views/
│   │   ├── rule/        # Rule management UI
│   │   └── problem/     # Problem Center cockpit UI
│   └── components/
│       └── pdf/         # Enhanced PDF viewer with highlighting
```

**Structure Decision**: Vertical slicing by domain context (Rule vs. Problem Center) within the backend, with shared integration events.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| JSONB Storage | Dynamic localization metadata | Fixed columns are too rigid for varied file formats |
| Embedded Script Engine | Flexible rule logic | Hardcoded rules require redeployment for every policy change |

# Implementation Plan: Manual Rule Evaluation

**Branch**: `001-manual-rule-evaluation` | **Date**: 2026-01-16 | **Spec**: [spec.md](spec.md)
**Input**: Feature specification from `specs/001-manual-rule-evaluation/spec.md`

## Summary

This plan outlines the technical implementation for the "Manual Rule Evaluation" feature. The core requirement is to allow users to manually trigger the evaluation of one or more rules against a selected list of contracts and review the results. The technical approach involves extending the existing frontend and backend applications. A new "Problem Center" page will be created in the Vue frontend to display evaluation results, supported by new REST endpoints in the Spring Boot backend. Rule evaluation will be handled asynchronously using Kafka to ensure the UI remains responsive.

## Technical Context

**Language/Version**: Java 17 (Backend), Vue 3 (Frontend)
**Primary Dependencies**: Spring Boot 3.2.x, Spring Data JPA, Kafka, Element Plus
**Storage**: PostgreSQL (for rules, contracts, evaluation jobs/results), Redis (caching), MinIO (if contracts involve file storage)
**Testing**: JUnit/Mockito (Backend), Jest/Vue Testing Library (Frontend)
**Target Platform**: Web Browser (via existing frontend application)
**Project Type**: Web application (frontend + backend)
**Performance Goals**: P95 response time for evaluation trigger APIs < 500ms; results available in Problem Center within 5 minutes for large batches.
**Constraints**: Must adhere to existing tenant isolation and auditability principles.
**Scale/Scope**: The feature should support evaluating up to 100 rules against 100 contracts in a single batch operation.

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [X] **I. Tenant Isolation**: All new database queries for rules, contracts, and results will be filtered by `tenant_id`.
- [X] **II. AI-Manual Synergy**: The trigger type (Manual) will be explicitly stored with the evaluation job, ensuring traceability.
- [X] **III. Rule Governance**: The feature is centered around invoking the existing rule engine.
- [X] **IV. Middleware Std**: New backend APIs will follow existing RESTful conventions. Kafka is used for asynchronous processing.
- [X] **V. Auditability**: A new audit event will be created for manual rule triggers, capturing the user, rules, and contracts involved.
- [ ] **VI. E2E Testing**: An E2E test plan needs to be created.
- [X] **VII. Doc Sync**: This plan and its artifacts are part of the design documentation.
- [X] **VIII. UX/Completeness**: The design incorporates user feedback and provides a complete, if minimal, journey for the requested feature.
- [ ] **IX. Manifest Sync**: The plan needs to include tasks to update global manifests.

## Project Structure

### Documentation (this feature)

```text
specs/001-manual-rule-evaluation/
├── plan.md              # This file
├── research.md          # Phase 0 output
├── data-model.md        # Phase 1 output
├── quickstart.md        # Phase 1 output
├── contracts/           # Phase 1 output
│   └── openapi.yaml
└── tasks.md             # Phase 2 output (created by /speckit.tasks)
```

### Source Code (repository root)

```text
backend/
├── src/main/java/com/contractmaster/
│   ├── evaluation/
│   │   ├── api/EvaluationController.java
│   │   ├── service/EvaluationService.java
│   │   ├── model/EvaluationJob.java
│   │   ├── model/EvaluationResult.java
│   │   └── repository/EvaluationJobRepository.java
│   └── problemcenter/
│       ├── api/ProblemCenterController.java
│       └── service/ProblemCenterService.java
└── test/java/com/contractmaster/evaluation/

frontend/
├── src/
│   ├── pages/
│   │   ├── RuleEditor.vue
│   │   └── ProblemCenter.vue
│   ├── services/
│   │   └── evaluationApi.js
│   └── components/
│       └── ContractSelectorModal.vue
└── tests/
```

**Structure Decision**: The feature will be implemented within the existing `frontend` and `backend` project structure, as it extends current functionality. New packages/directories will be created for the `evaluation` and `problemcenter` concerns to maintain modularity.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| *None* | - | - |
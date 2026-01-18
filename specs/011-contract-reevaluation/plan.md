# Implementation Plan: Contract Re-evaluation

**Branch**: `001-contract-reevaluation` | **Date**: 2026-01-17 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `specs/001-contract-reevaluation/spec.md`

## Summary

This feature adds the capability for a user to trigger a re-evaluation of a specific contract from the contract detail page. The user will be able to select one or more evaluation rules to be used in the re-evaluation. This will be implemented by adding a new button to the contract detail view, a modal for rule selection, and a new API endpoint to initiate the evaluation job.

## Technical Context

**Language/Version**: Java 17 (Backend), Vue 3 (Frontend)
**Primary Dependencies**: Spring Boot 3.2.x, Spring Data JPA, Kafka (Backend); Element Plus (Frontend)
**Storage**: PostgreSQL (for evaluation jobs)
**Testing**: JUnit, Mockito (Backend); Vitest (Frontend - assumed)
**Target Platform**: Web Browser
**Project Type**: Web application (backend + frontend)
**Performance Goals**: Performance targets for re-evaluation will be benchmarked and defined in a future iteration.
**Constraints**: None
**Scale/Scope**: Re-evaluation for a single contract against a selected set of rules.

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [X] **I. Tenant Isolation**: The feature will operate on tenant-specific contracts and rules. The new API endpoint must enforce tenant isolation.
- [ ] **II. AI-Manual Synergy**: N/A
- [X] **III. Rule Governance**: The feature is centered around the rule engine.
- [X] **IV. Middleware Std**: The new API will follow existing RESTful patterns.
- [X] **V. Auditability**: Triggering a re-evaluation will be an auditable event.
- [X] **VI. E2E Testing**: E2E tests will be created to cover the user journey of triggering a re-evaluation.
- [X] **VII. Doc Sync**: This plan and other design documents will be kept in sync with the implementation.
- [X] **VIII. UX/Completeness**: The user journey is straightforward and addresses the core need.
- [ ] **IX. Manifest Sync**: The `api.md` manifest will need to be updated with the new endpoint.

## Project Structure

### Documentation (this feature)

```text
specs/001-contract-reevaluation/
├── plan.md              # This file
├── research.md          # Not needed as there are no major unknowns
├── data-model.md        # To be created
├── quickstart.md        # To be created
├── contracts/           # To be created
└── tasks.md             # To be created by /speckit.tasks
```

### Source Code (repository root)

```text
backend/
├── src/main/java/com/contract/master/
│   ├── evaluation/
│   │   ├── api/EvaluationController.java # Modify to add new endpoint
│   │   └── service/EvaluationService.java # Modify to handle re-evaluation
│   └── domain/ # May need new entities for EvaluationJob
└── tests/

frontend/
├── src/
│   ├── views/contracts/ # Assuming contract detail view is here
│   │   └── ContractDetail.vue # Modify to add button
│   ├── components/ # New component for rule selection modal
│   │   └── RuleSelectorModal.vue
│   └── services/evaluationApi.js # Add new function to call re-evaluation API
└── tests/
```

**Structure Decision**: The existing project structure (backend/frontend) will be used. New components will be added to the frontend, and existing services and controllers on the backend will be modified.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
|           |            |                                     |
|           |            |                                     |
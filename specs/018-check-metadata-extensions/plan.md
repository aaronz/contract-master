# Implementation Plan: Audit and verify metadata-driven field extensions

**Branch**: `018-check-metadata-extensions` | **Date**: 2026-01-21 | **Spec**: [/specs/018-check-metadata-extensions/spec.md](./spec.md)

**Input**: Feature specification from `/specs/018-check-metadata-extensions/spec.md`

## Summary

The goal is to audit and verify the current metadata-driven design for field extensions. The system uses a hybrid approach: standard fields (hardcoded) and extended fields (dynamic DB records) are unified at the service layer using `FieldConfig` settings. This plan focuses on ensuring that value persistence, unified metadata retrieval, and the full data lifecycle (search, export, AI) work correctly across all scenarios.

## Technical Context

**Language/Version**: Java 17, Vue 3  
**Primary Dependencies**: Spring Boot 3.2.x, Spring Data JPA, Element Plus  
**Storage**: PostgreSQL  
**Testing**: JUnit 5, MockMvc, Vitest  
**Target Platform**: Web application (Backend + Frontend)
**Project Type**: Web application
**Performance Goals**: Unified metadata retrieval < 200ms
**Constraints**: Strict tenant isolation for custom fields and configs
**Scale/Scope**: Support 50+ extended fields per tenant

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] **I. Transparent Tenant Isolation**: Does this feature maintain strict logical isolation?
- [x] **II. Full-Link Context Propagation**: Are data sources and actors traceable?
- [x] **III. AI-Manual Synergy**: Are data sources and actors traceable?
- [x] **IV. Rule Governance**: Does this integrate with the rule engine for validation?
- [x] **V. Middleware Std**: Does it use standardized APIs/WebHooks for integration?
- [x] **VI. Absolute Auditability**: Are all state changes captured in audit logs?
- [x] **VII. Doc Sync**: Plan includes tasks to keep design docs updated?
- [x] **VIII. UX/Completeness**: Has the user journey been reviewed for clarity and logic?
- [x] **IX. Manifest Sync**: Plan includes tasks to update global manifests (features, bugs, api, table)?

## Project Structure

### Documentation (this feature)

```text
specs/018-check-metadata-extensions/
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
├── contract/
│   ├── application/     # ContractService, MetadataService
│   └── domain/model/    # ContractExtendField, ContractExtendData
└── contract/metadata/
    └── domain/model/    # FieldConfig

frontend/src/
├── components/          # DynamicFieldRenderer.vue
├── stores/              # fieldStore.js
└── views/settings/      # fields.vue
```

**Structure Decision**: Standard web application structure as defined in the project configuration.


## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| [e.g., 4th project] | [current need] | [why 3 projects insufficient] |
| [e.g., Repository pattern] | [specific problem] | [why direct DB access insufficient] |

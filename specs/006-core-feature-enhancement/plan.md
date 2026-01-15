# Implementation Plan: Contract Master Core Enhancements

**Branch**: `006-core-feature-enhancement` | **Date**: 2026-01-15 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/006-core-feature-enhancement/spec.md`

## Summary
Enhance the core system by unifying field management across all modules, supporting hybrid (Logic/AI) rule configurations, enabling API key visibility in the Integration Hub, and fixing the Problem Center resolution flow.

## Technical Context

**Language/Version**: Java 17, Vue 3
**Primary Dependencies**: Spring Boot 3.2.x, Spring Data JPA, Element Plus
**Storage**: PostgreSQL
**Testing**: JUnit 5, Playwright
**Target Platform**: Linux/WASM
**Project Type**: Web Application
**Performance Goals**: Metadata retrieval < 200ms
**Constraints**: Tenant isolation mandatory for all field lookups

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] **I. Tenant Isolation**: Metadata service filters extended fields by `tenant_id`.
- [x] **II. AI-Manual Synergy**: New AI Prompt rules support the synergy model.
- [x] **III. Rule Governance**: Unified rule config respects the governance engine.
- [x] **IV. Middleware Std**: Integration Hub fixes follow standard API patterns.
- [x] **V. Auditability**: Problem resolution and rule changes are audited.
- [x] **VI. E2E Testing**: New scenarios cover field management and rule config.
- [x] **VII. Doc Sync**: Plan includes updating `data-model.md`.
- [x] **VIII. UX/Completeness**: Field unification addresses a major UX gap.

## Project Structure

### Documentation (this feature)

```text
specs/006-core-feature-enhancement/
├── plan.md              # This file
├── research.md          # Strategy for metadata unification
├── data-model.md        # Updated RuleConfig and Metadata DTO
├── quickstart.md        # Verification steps
├── contracts/           # API specs
│   ├── metadata.yaml
│   └── problems.yaml
└── tasks.md             # Implementation tasks
```

### Source Code (repository root)

```text
backend/
├── src/main/java/com/contract/master/
│   ├── api/
│   │   ├── MetadataController.java
│   │   ├── ProblemController.java
│   │   └── RuleController.java (Update)
│   ├── service/
│   │   ├── MetadataService.java (New)
│   │   └── NotificationService.java (Update)
│   └── domain/
│       └── RuleConfig.java (Update)

frontend/
├── src/views/
│   ├── contract/
│   │   ├── list.vue (Update)
│   │   └── detail.vue (Update)
│   ├── settings/
│   │   ├── rules.vue (Update)
│   │   └── fields.vue (Update)
│   ├── integrations/
│   │   └── secrets.vue (Update)
│   └── compliance/
│       └── problems.vue (Update)
```

**Structure Decision**: Standard Spring Boot + Vue 3 layout.

## Phase 0: Research Findings
- **Metadata**: Unified service merging `ContractBase` reflection and `ContractExtendField` DB records.
- **Rules**: Add `rule_type` column to `rule_config` table.
- **Integration**: Ensure API key is returned in response only upon creation (security best practice).

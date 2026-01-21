# Implementation Plan: Robust Integration Hub (ESB Foundation)

**Branch**: `019-robust-integration-hub` | **Date**: 2026-01-21 | **Spec**: [/specs/019-robust-integration-hub/spec.md](./spec.md)

**Input**: Feature specification from `/specs/019-robust-integration-hub/spec.md`

## Summary

This feature refactors the existing Integration Hub from a simple, volatile sync utility into a robust Enterprise Service Bus (ESB) foundation. Key technical transitions include:
1. Moving from in-memory `@Async` processing to a persistent Kafka-based Outbox pattern for reliability.
2. Introducing Groovy-based dynamic data transformations for field mappings.
3. Adding observability through payload logging and manual event replay.
4. Implementing standardized OAuth2 Client Credentials authentication.
5. Proactive connection health monitoring via a scheduled heartbeat.

## Technical Context

**Language/Version**: Java 17  
**Primary Dependencies**: Spring Boot 3.2.x, Spring Data JPA, Spring Kafka 3.x, Groovy 4.x, Caffeine (Cache)  
**Storage**: PostgreSQL (Relational)  
**Testing**: JUnit 5, Mockito, Testcontainers (Kafka/Postgres)  
**Target Platform**: Docker-based Linux environment  
**Project Type**: Web application (Backend-heavy)  
**Performance Goals**: P95 Kafka produce latency < 100ms, Heartbeat check every 5 mins  
**Constraints**: Zero data loss for outbound events, strictly tenant-isolated scripts  
**Scale/Scope**: Support 100+ downstream systems with heterogeneous authentication protocols

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] **I. Tenant Isolation**: Does this feature maintain strict logical isolation?
- [x] **II. AI-Manual Synergy**: Are data sources and actors traceable? (Replay actor identified)
- [x] **III. Rule Governance**: Does this integrate with the rule engine for validation? (Pre-push validation)
- [x] **IV. Middleware Std**: Does it use standardized APIs/WebHooks for integration? (OAuth2/HMAC)
- [x] **V. Auditability**: Are all state changes captured in audit logs? (Integration logs)
- [x] **VI. E2E Testing**: Is there an E2E test plan for every user story journey?
- [x] **VII. Doc Sync**: Plan includes tasks to keep design docs updated?
- [x] **VIII. UX/Completeness**: Has the user journey been reviewed for clarity and logic? (Replay capability)
- [x] **IX. Manifest Sync**: Plan includes tasks to update global manifests (features, bugs, api, table)?

## Project Structure

### Documentation (this feature)

```text
specs/019-robust-integration-hub/
├── plan.md              # This file
├── research.md          # Phase 0 output
├── data-model.md        # Phase 1 output
├── quickstart.md        # Phase 1 output
├── contracts/           # Phase 1 output
└── tasks.md             # Phase 2 output
```

### Source Code (repository root)

```text
backend/src/main/java/com/contract/master/integration/
├── application/         # IntegrationPushService (Updated), TokenManager (New)
├── domain/
│   ├── model/           # IntegrationEvent (New), Updated Entities
│   └── service/         # ScriptSandbox (New)
└── infrastructure/      # KafkaConsumer, KafkaProducer
```

**Structure Decision**: Domain-centric backend structure within the `integration` package.


## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| [e.g., 4th project] | [current need] | [why 3 projects insufficient] |
| [e.g., Repository pattern] | [specific problem] | [why direct DB access insufficient] |

# Implementation Plan: Integration Hub

**Branch**: `005-integration-hub` | **Date**: 2026-01-15 | **Spec**: [/specs/005-integration-hub/spec.md]
**Input**: Feature specification from `/specs/005-integration-hub/spec.md`

**Note**: This template is filled in by the `/speckit.plan` command. See `.specify/templates/commands/plan.md` for the execution workflow.

## Summary

This feature implements a robust outbound synchronization engine (the "Integration Hub") for Contract Master. It enables automated, push-based delivery of verified contract data to external downstream systems via WebHooks. Key technical components include an asynchronous `WebHookPushService`, HMAC-SHA256 payload signing for security, persistence of delivery logs for auditability, and a management dashboard for monitoring and retrying sync tasks.

## Technical Context

**Language/Version**: Java 17, Vue 3
**Primary Dependencies**: Spring Boot 3.2.x, Spring Web (RestTemplate/WebClient), Element Plus
**Storage**: PostgreSQL (for `integration_log` table)
**Testing**: JUnit 5, Mockito, Playwright (E2E)
**Target Platform**: Linux/Cloud
**Project Type**: web application
**Performance Goals**: Push initiation < 200ms; Dashboard load < 500ms
**Constraints**: Absolute tenant isolation for API keys and log data; Mandatory rule engine validation before push.
**Scale/Scope**: Support 10+ downstream targets per tenant; Async thread pool for parallel delivery.

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] **I. Tenant Isolation**: All integration logs and settings filtered by `tenant_id`.
- [x] **II. AI-Manual Synergy**: Push is only triggered for `VERIFIED` contracts.
- [x] **III. Rule Governance**: Rule engine results checked before initiating outbound sync.
- [x] **IV. Middleware Std**: Implementation follows standard HMAC-SHA256 and JSON WebHook patterns.
- [x] **V. Auditability**: Every sync attempt (including payload hash) recorded in `integration_log`.
- [x] **VI. E2E Testing**: Playwright tests cover the full journey from "Publish" to "Push Success".
- [x] **VII. Doc Sync**: Design artifacts synchronized with push-based architecture.
- [x] **VIII. UX/Completeness**: Dashboard provides "Retry" and "History" visibility.

## Project Structure

### Documentation (this feature)

```text
specs/005-integration-hub/
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
│   ├── api/             # Integration Dashboard & Log Controllers
│   ├── service/         # WebHookPushService & Retry Logic
│   ├── domain/          # IntegrationLog & OutboundQueue entities
│   └── integration/     # HMAC Signer & Dispatcher
frontend/
├── src/views/settings/  # Integration Dashboard & Log Viewer
└── src/api/             # Outbound Sync & Log APIs
```

**Structure Decision**: Option 2: Web application. The Integration Hub requires a coordinated backend worker and frontend monitoring view.

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| None | | |

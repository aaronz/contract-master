# Contract Master Agent Instructions

Central hub for contract management middleware bridging SaaS CRMs and enterprise systems.

## Quick Reference
- **Stack**: Java 17 (Spring Boot 3.2.x), Vue 3 (Element Plus), PostgreSQL, Kafka.
- **Isolation**: Mandatory transparent multi-tenancy.
- **Authority**: [Project Constitution](.specify/memory/constitution.md) is the supreme guide.

## Guidelines & Patterns
For detailed instructions, follow these linked resources:
- [**Core Principles**](.agents/instructions/principles.md): Tenant isolation, auditability, metadata-driven design.
- [**Technology Stack**](.agents/instructions/tech-stack.md): Backend (Java 17/Spring), Frontend (Vue 3), and standards.
- [**Workflow & Quality**](.agents/instructions/workflow.md): Feature lifecycle, global manifests, and review gates.

## Global Manifests (MUST refresh on changes)
- `docs/features.md` (System Features)
- `docs/bugs.md` (Known Issues)
- `docs/api.md` (API Registry)
- `docs/table.md` (Database Schema)

## Core Commands
- **Backend Build**: `./mvnw clean install`
- **Frontend Dev**: `npm run dev` (in `frontend/`)
- **E2E Tests**: `npx playwright test` (in `tests/`)

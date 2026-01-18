# Research Plan: Backend DDD Refactor

**Date**: 2026-01-18
**Feature**: Backend DDD Refactor (012-backend-ddd-refactor)

## Phase 0 Research Topics

### 1. Spring Boot DDD Implementation Patterns
**Decision**: Adopt the **Hexagonal Architecture (Ports and Adapters)** flavor of DDD.
**Rationale**: This aligns with the Constitution's requirement for "Middleware Standardization" and decoupling through abstraction layers. In Spring Boot, this translates to:
- `domain.model`: Pure POJOs for Entities, Value Objects, and Aggregate Roots.
- `domain.repository`: Interfaces only.
- `infrastructure.persistence`: JPA implementations of repository interfaces.
- `application.service`: Orchestrates domain logic and interacts with ports.

**Alternatives Considered**:
- **Layered Architecture (Traditional)**: Faster to implement but leads to "Anemic Domain Models" where business logic leaks into service classes, violating DDD core principles.
- **Onion Architecture**: Very similar to Hexagonal, but Hexagonal is more explicit about "Ports" (interfaces) which fits better with Spring's dependency injection.

### 2. Migration Strategy for Existing Codebase
**Decision**: Use a **Parallel Model/Strangler Pattern** approach.
**Rationale**: The requirement SC-004 mandates 100% regression pass. We will:
1. Create the new DDD package structure.
2. Port business logic one Aggregate at a time.
3. Keep existing DTOs/Controllers initially, but point them to new Application Services.
4. Verify with existing E2E tests at each step.

**Alternatives Considered**:
- **Big Bang Refactor**: Too risky for a core system. Likely to break many things at once.

### 3. Handling Multi-tenancy in DDD
**Decision**: Implement **Tenant ID as a Value Object** embedded in Aggregate Roots.
**Rationale**: Constitution Principle I requires strict logical isolation. By making `TenantId` a first-class citizen in the Domain model, we ensure that every business operation is tenant-aware by design, not just by database filters.

**Alternatives Considered**:
- **Global Aspect/ThreadLocal**: Prone to "leaks" if a developer forgets to set the context. Value Objects in the constructor make it mandatory.

### 4. Performance Mitigation for DTO Mapping
**Decision**: Use **MapStruct** for high-performance compile-time mapping between Entities, Domain Models, and DTOs.
**Rationale**: Ensures we meet SC-005 (<5% performance loss). Manual mapping is error-prone; reflection-based mapping is too slow.

**Alternatives Considered**:
- **Manual Mapping**: Too much boilerplate code.
- **ModelMapper (Reflection)**: Significant performance overhead.

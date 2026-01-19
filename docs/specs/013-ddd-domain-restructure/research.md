# Research: DDD Domain Restructure

**Date**: 2026-01-18
**Feature**: DDD Domain Restructure (013-ddd-domain-restructure)

## Phase 0 Research Findings

### 1. Domain-Centric Structure vs. Layered Structure
**Decision**: Adopt **Domain-Centric (Vertical Slicing)** as the primary organizational principle.
**Rationale**: The current hybrid structure causes "split identity" issues where some domains have internal layers while others rely on global layers. Transitioning to a strict vertical slice approach ensures that each bounded context is self-contained, reducing cognitive load and preventing unintended coupling.
- Each domain module (e.g., `contract`, `identity`) will have:
    - `domain`: Core logic (Entities, VOs, Repository Interfaces, Domain Services).
    - `application`: Use cases and orchestration.
    - `infrastructure`: Technical details (JPA implementations, external clients).
    - `interface`: External entry points (REST Controllers, WebHooks).
    - `dto`: Data transfer objects for that domain.

### 2. Handling Circular Dependencies
**Decision**: Use **Domain Events** and **Dependency Inversion** to break cycles.
**Rationale**: 
- **Contract <-> Metadata**: Replace direct calls from `FieldConfigService` to `ContractService` with a Spring `ApplicationEvent`. When metadata changes, an event is published; `ContractService` listens and clears its cache.
- **Contract <-> Integration**: Define a `DownstreamProvider` interface in the `contract.domain` layer. The `integration` module will implement this interface in its `infrastructure` layer, keeping the `contract` domain independent of integration details.

### 3. Model Consolidation
**Decision**: Standardize on the `com.contract.master.contract.domain.model` version of the Contract entity.
**Rationale**: The exploration revealed two `Contract` implementations. The one in the `contract` subpackage is used by newer features (AI, Evaluation) and aligns better with the target domain-centric structure. The legacy version in the root `domain` package will be deprecated and its remaining usages migrated.

### 4. Shared Kernel Strategy
**Decision**: Introduce a `shared` package for truly cross-cutting domain concerns.
**Rationale**: Generic DDD base classes (`AggregateRoot`, `Entity`, `ValueObject`) and common types like `TenantId` or `Money` should live in `com.contract.master.shared`. This prevents code duplication across domains while maintaining strict layering.
- **Security**: Relocate generic Security VOs and Context to `shared.domain`. Keep Spring Security implementation details in a dedicated `infrastructure.security` module.

## Alternatives Considered
- **Modular Monolith (Maven/Gradle Modules)**: Rejected for this phase to avoid excessive build configuration complexity. We will achieve logical modularity through package structure first.
- **Strict Layered (Horizontal)**: Rejected because it's the current "problematic" state that led to cluttered global packages.

# Quickstart: DDD Domain Development

This guide describes how to work with the domain-centric structure in this project.

## Creating a New Domain
When adding a new bounded context (e.g., `billing`):

1.  **Create Package Structure**:
    `com.contract.master.billing`
    ├── `domain`: Entities, VOs, Repository interfaces
    ├── `application`: Use cases and domain services
    ├── `infrastructure`: Data access implementations, external adapters
    ├── `interface`: Controllers and WebHooks
    └── `dto`: Request/Response objects

2.  **Define Domain Logic**:
    - Focus on the `domain` package first.
    - Inherit from `shared.domain.base` for base DDD behaviors.

3.  **Implement Use Cases**:
    - Add methods to the `application` service.
    - Inject repository interfaces from the `domain`.

4.  **Wiring with Spring**:
    - Use constructor injection.
    - Ensure your domain modules are within the component scan path defined in `ContractManagementApplication`.

## Guidelines for Refactoring
- **Breaking Cycles**: If Module A depends on Module B, and B depends on A, use **Domain Events** (Module B publishes an event, Module A listens) or **Dependency Inversion** (Module A defines an interface that Module B implements).
- **No Global Leakage**: Do not add domain-specific logic to the `shared` module.
- **DTO mapping**: Map domain objects to DTOs in the `interface` layer or `application` layer, never expose Entities directly to the outside world.

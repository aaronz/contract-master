# DDD Refactoring Manifesto

## Bounded Contexts

### 1. Contract Context (`com.contract.master.contract`)
**Core Aggregate**: `Contract`
- **Value Objects**: `ContractAmount`, `ContractParty`, `ContractTimeline`
- **Responsibilities**: Contract lifecycle management, status transitions, metadata management.

### 2. Evaluation Context (`com.contract.master.evaluation`)
**Core Aggregate**: `RuleConfig`, `EvaluationJob`
- **Responsibilities**: Rule definition, manual/automatic re-evaluation execution, result aggregation.

### 3. Integration Context (`com.contract.master.integration`)
**Core Aggregate**: `DownstreamSystem`, `WebHookConfig`
- **Responsibilities**: Inbound CRM sync, outbound ERP/Finance pushes, field mapping.

### 4. Identity Context (`com.contract.master.identity`)
**Core Aggregate**: `User`, `Tenant`
- **Responsibilities**: RBAC, multi-tenant isolation, authentication.

## Layering Standard

| Layer | Responsibility |
|-------|----------------|
| **Interfaces** | REST Controllers, WebHook entry points. |
| **Application** | Orchestration of domain logic, transaction boundaries, security checks. |
| **Domain** | Pure business logic, Entities, Value Objects, Repository Interfaces. |
| **Infrastructure** | Persistence (JPA), External API Clients, Message Brokers (Kafka). |

## Key Refactoring Principles
1. **Rich Domain Model**: Move "How it works" from Services to Entities.
2. **Value Objects**: Encapsulate small, immutable data clusters (e.g., Party, Amount).
3. **Application Services**: Services should only "Coordinate", not "Decide".
4. **Ubiquitous Language**: Standardize naming across code, database, and specs (e.g., `ContractParty` instead of flat fields).

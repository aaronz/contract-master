# Data Model Mapping: DDD Domain Restructure

## Current vs. Target Package Mapping

This document maps the existing "cluttered" package structure to the target domain-centric structure.

### 1. Contract Domain
- **Current Location**: `com.contract.master.contract.*`, `com.contract.master.domain.model.contract.*`, `com.contract.master.application.contract.*`
- **Target Location**: `com.contract.master.contract`
    - `domain`: `Contract`, `ContractId`, `ContractNo`, `ContractRepository`, `ContractLifecycleService`, `DownstreamProvider` (new)
    - `application`: `ContractApplicationService`
    - `infrastructure`: `ContractRepositoryImpl`, `ContractEntity`, `persistence.*`
    - `interface`: `ContractController`
    - `dto`: `ContractDTO`

### 2. Identity & Access Domain (Identity)
- **Current Location**: `com.contract.master.identity.*`, `com.contract.master.domain.model.auth.*`, `com.contract.master.domain.model.tenant.*`
- **Target Location**: `com.contract.master.identity`
    - `domain`: `User`, `UserId`, `Tenant`, `TenantId`, `UserRepository`, `TenantRepository`
    - `application`: `AuthApplicationService`
    - `infrastructure`: `persistence.UserRepositoryImpl`
    - `interface`: `AuthController`
    - `dto`: `LoginRequest`, `AuthResponse`

### 3. Shared Domain (Kernel)
- **Current Location**: `com.contract.master.domain.model.base.*`, `com.contract.master.shared.*`
- **Target Location**: `com.contract.master.shared`
    - `domain.base`: `AggregateRoot`, `Entity`, `ValueObject`
    - `domain.model`: `Money`, `TenantId` (shared version)
    - `infrastructure.security`: `TenantContext` (infrastructure part)

### 4. Integration Domain
- **Current Location**: `com.contract.master.integration.*`
- **Target Location**: `com.contract.master.integration`
    - `domain`: `DownstreamSystem`, `FieldMapping`
    - `application`: `CrmIntegrationApplicationService`
    - `infrastructure`: `CrmClient`, implementation of `DownstreamProvider`
    - `interface`: `IntegrationWebHookController`

## Dependency Rules
1. **Vertical Slices**: Domains SHOULD NOT depend on each other's `infrastructure` or `interface` layers.
2. **Application Orchestration**: If Domain A needs information from Domain B, it should be coordinated by a Service in Application A or via a Domain Event.
3. **Shared Kernel**: All domains can depend on the `shared` module.
4. **No Circularity**: Strict enforcement of acyclic dependencies between modules.

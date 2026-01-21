# Data Model: Transparent Multi-tenancy

## Core Entities

### TenantId (Value Object)
- **id**: String (Primary key in `tenant_info`, discriminator in other tables)

### BaseTenantEntity (Mapped Superclass)
- **tenantId**: TenantId (Discriminator column `tenant_id`)
- **createTime**: LocalDateTime
- **createUser**: String
- **updateTime**: LocalDateTime
- **updateUser**: String

## Updated Entities (Applying Isolation)

### User (Identity)
- **Inherits**: `BaseTenantEntity` (New)
- **Fields**: `userId`, `userName`, `password`, `realName`, `status`
- **Isolation**: Will now be automatically filtered by Hibernate.

### Contract (Core)
- **Inherits**: `BaseTenantEntity` (Existing)
- **Fields**: `contractId`, `contractNo`, `contractName`, `amount`, `status`
- **Validation**: `tenantId` is mandatory and immutable after creation.

### Rule (Governance)
- **Inherits**: `BaseTenantEntity` (Existing)
- **Fields**: `ruleId`, `name`, `logicType`, `logicContent`, `status`

## Context Management

### TenantContext
- **Storage**: `ThreadLocal<String>`
- **Methods**:
    - `getCurrentTenant()`: Retrieves current active tenant.
    - `setCurrentTenant(id)`: Sets active tenant for the thread.
    - `clear()`: Removes context (Mandatory in `finally` blocks).
    - `executeAsSystem(Runnable)`: Bypasses isolation for system tasks.

## Infrastructure Mapping

| Component | Responsibility | Propagation Mechanism |
|-----------|----------------|------------------------|
| **TenantFilter** | Web Entry | HTTP Header `X-Tenant-ID` -> ThreadLocal |
| **TaskDecorator** | Async Threads | ThreadLocal -> Task ThreadLocal |
| **KafkaInterceptor** | Messaging | Kafka Header `tenant-id` -> ThreadLocal |
| **TenantAspect** | Repository | ThreadLocal -> Hibernate Session Filter |
| **TenantEntityListener** | Persistence | ThreadLocal -> Entity Field |

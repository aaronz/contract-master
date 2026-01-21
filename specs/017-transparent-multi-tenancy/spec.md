# Feature Specification: Transparent Multi-tenancy

**Feature Branch**: `017-transparent-multi-tenancy`  
**Created**: 2026-01-21  
**Status**: Draft  
**Input**: User description: "详细检查程序的多租户的逻辑，思考如何把多租户的逻辑在后端做成透明化，默认全链路传递多租户信息，如何优化相关设计"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Transparent Data Isolation (Priority: P1)

As a developer, I want the system to automatically handle tenant isolation at the database level so that I don't have to manually add `where tenant_id = ?` to every query or worry about data leakage.

**Why this priority**: Core requirement for security and developer productivity. Manual filtering is error-prone and leads to security vulnerabilities.

**Independent Test**: Create two tenants (Tenant A and Tenant B). Save a contract for Tenant A. Verify that a simple `findAll()` call while authenticated as Tenant B returns zero results, even if no explicit tenant filtering is written in the repository or service.

**Acceptance Scenarios**:

1. **Given** a user is logged into Tenant A, **When** they perform any data retrieval (List, Get), **Then** only data belonging to Tenant A is returned.
2. **Given** a new entity is saved while a tenant context is active, **When** the save operation completes, **Then** the `tenant_id` is automatically populated in the database without explicit setter calls in the business logic.

---

### User Story 2 - Full-Link Context Propagation (Priority: P2)

As a system architect, I want the tenant context to be automatically propagated across different execution layers (Web, Async Threads, Message Queue) so that multi-tenancy is preserved throughout the entire request lifecycle.

**Why this priority**: Ensures consistency in background tasks and asynchronous processing which are critical for AI extraction and rule evaluation.

**Independent Test**: Trigger an asynchronous AI extraction task for Tenant A. Verify that the resulting background job, Kafka messages, and final database updates all correctly reference Tenant A's ID without manual propagation.

**Acceptance Scenarios**:

1. **Given** a tenant context is set in the main thread, **When** a `@Async` method is called, **Then** the child thread inherits the same tenant context.
2. **Given** a message is sent to Kafka, **When** the message is consumed, **Then** the consumer automatically restores the tenant context from the message headers.

---

### User Story 3 - Centralized Tenant Management (Priority: P3)

As a system administrator, I want a centralized way to manage tenant configurations (e.g., enabled features, custom rules) so that the system behavior can be tailored per tenant without code changes.

**Why this priority**: Supports scalability and customization requirements for different organizations.

**Independent Test**: Enable a specific rule for Tenant A and disable it for Tenant B. Verify that the system execution reflects these settings for respective tenants.

**Acceptance Scenarios**:

1. **Given** a tenant-specific setting exists, **When** the system performs an operation, **Then** it uses the configuration corresponding to the current tenant context.

---

### Edge Cases

- **Missing Tenant Header**: How does the system handle requests without an `X-Tenant-ID` header? (Requirement: Default to a safe 'unauthorized' state or a clearly defined public/system tenant if applicable).
- **Context Leakage**: How to ensure tenant info doesn't "leak" between requests in a thread-pooled environment? (Requirement: Strict cleanup in `finally` blocks).
- **System-level Operations**: How to perform cross-tenant operations (e.g., global analytics) by a super-admin? (Requirement: Mechanism to bypass/override the automatic filter).

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST implement a global Hibernate filter that is automatically enabled for all `TenantAware` entities based on the current context.
- **FR-002**: System MUST use a `ThreadLocal`-based `TenantContext` to store and retrieve the current `tenantId`.
- **FR-003**: System MUST automatically extract `tenantId` from HTTP headers (`X-Tenant-ID`) in a Servlet Filter.
- **FR-004**: System MUST automatically propagate `tenantId` through Kafka headers for all asynchronous messages.
- **FR-005**: System MUST provide a `TaskDecorator` or similar mechanism to propagate `TenantContext` to `@Async` child threads.
- **FR-006**: System MUST use JPA Entity Listeners to automatically set the `tenantId` on new entities during the `@PrePersist` phase.
- **FR-007**: System MUST provide a "System Context" mode to allow super-admins to perform cross-tenant operations when explicitly authorized.

### Key Entities *(include if feature involves data)*

- **Tenant**: Represents an isolated organization within the system. Key attributes: `tenantId`, `name`, `status`.
- **TenantContext**: A non-persistent utility that holds the transient state of the current request's tenant.
- **BaseTenantEntity**: An abstract base class or mapped superclass that all multi-tenant entities must inherit from.

## Constitution Check *(mandatory)*

- [x] **I. Tenant Isolation**: Feature respects multi-tenant boundaries?
- [x] **II. AI-Manual Synergy**: Data source (AI/User) is identified? (Context remains consistent for AI-triggered actions)
- [x] **III. Rule Governance**: Feature utilizes rule engine for validation? (Rules are tenant-scoped)
- [x] **IV. Middleware Std**: Uses standard integration protocols? (Standard HTTP/Kafka headers)
- [x] **V. Auditability**: Actions are recorded in audit logs? (Audit logs will include tenantId)
- [x] **VI. E2E Testing**: User stories define clear end-to-end success criteria?
- [x] **VII. Doc Sync**: Specification includes all necessary functional details?
- [x] **VIII. UX/Completeness**: User journey is intuitive and addresses the core need?
- [x] **IX. Manifest Sync**: Impact on global manifests (features, bugs, api, table) is analyzed?

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 100% of data access repositories automatically apply tenant filtering without explicit code in the repository interfaces.
- **SC-002**: Zero data leakage between tenants confirmed by automated E2E tests with concurrent requests from different tenants.
- **SC-003**: Developer effort to add a new multi-tenant entity is reduced to simply extending `BaseTenantEntity`.
- **SC-004**: Asynchronous task success rate matches synchronous task rate for tenant context preservation.

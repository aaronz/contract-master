# Research: Transparent Multi-tenancy Optimization

## 1. Context Propagation for Async Tasks

- **Decision**: Implement a `ContextCopyingDecorator` using Spring's `TaskDecorator`.
- **Rationale**: This is the standard Spring way to propagate `ThreadLocal` variables to threads managed by a `TaskExecutor`. It ensures that `@Async` methods and other task-based executions inherit the parent thread's `tenantId`.
- **Implementation**:
    ```java
    public class ContextCopyingDecorator implements TaskDecorator {
        @Override
        public Runnable decorate(Runnable runnable) {
            String tenantId = TenantContext.getCurrentTenant();
            return () -> {
                try {
                    TenantContext.setCurrentTenant(tenantId);
                    runnable.run();
                } finally {
                    TenantContext.clear();
                }
            };
        }
    }
    ```

## 2. Hibernate Filter Enforcement & Gaps

- **Decision**: Extend `BaseTenantEntity` to `User.java` and all other business entities.
- **Rationale**: Current gaps in `User` and `ContractExtendData` (indirectly) create security risks. AOP pointcut `com.contract.master..domain..*` is broad enough, but entities must have the `@Filter` annotation to be affected.
- **Alternatives Considered**: 
    - Using a dedicated `TenantInterceptor` for all SQL: Rejected as Hibernate Filters are more natively integrated with JPA.
    - Custom Repository implementation: Too much boilerplate compared to AOP + Filters.

## 3. Redundancy Cleanup

- **Decision**: Deprecate manual `findByTenantId` repository methods.
- **Rationale**: Standard Spring Data JPA methods (e.g., `findAll()`, `findById()`) are automatically intercepted by `TenantAspect` which enables the Hibernate filter. Manual `tenant_id` parameters in query methods lead to "Double Filtering" (one in Java, one in SQL) which is redundant and confusing.

## 4. System-level Operation Bypass

- **Decision**: Provide a `TenantContext.executeAsSystem()` utility.
- **Rationale**: Certain background jobs or global analytics need to bypass tenant isolation.
- **Implementation**:
    ```java
    public static void executeAsSystem(Runnable task) {
        String original = getCurrentTenant();
        try {
            setCurrentTenant(null); // Null triggers filter disable or "bypass" value
            task.run();
        } finally {
            setCurrentTenant(original);
        }
    }
    ```
- **Note**: `TenantAspect` needs to be updated to explicitly `disableFilter` when `tenantId` is null to support this.

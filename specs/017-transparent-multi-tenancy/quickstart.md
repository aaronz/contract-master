# Quickstart: Working with Transparent Multi-tenancy

## 1. For Developers: Adding a New Entity

If you are adding a new business entity that needs to be tenant-isolated, simply extend `BaseTenantEntity`:

```java
@Entity
@Table(name = "my_new_feature")
public class MyFeature extends BaseTenantEntity {
    // Your fields here (tenantId is already handled)
}
```

**What happens automatically:**
1. Database queries will automatically include `WHERE tenant_id = 'current-tenant'`.
2. When saving a new instance, `tenant_id` will be automatically set from the current context.

## 2. For Developers: Calling Repositories

**DO NOT** add `tenantId` parameters to your repository methods unless absolutely necessary for cross-tenant logic.

```java
// WRONG - Redundant manual filtering
List<Contract> findByTenantId(String tenantId);

// CORRECT - Hibernate filter handles it transparently
List<Contract> findAll();
```

## 3. For Frontend: API Calls

The frontend `request.js` utility automatically attaches the `X-Tenant-ID` header. Ensure that `tenantId` is stored in `localStorage` upon login.

```javascript
// Automatically added by interceptor
headers: {
  'X-Tenant-ID': 'tenant-1'
}
```

## 4. Handling Asynchronous Tasks

If you use `@Async`, the `tenantId` is automatically copied to the background thread. No manual propagation is needed in the business logic.

```java
@Async
public void processInBackground() {
    // TenantContext.getCurrentTenant() will match the triggering user's tenant
}
```

## 5. System Bypass (Super-Admin)

To perform operations across all tenants or as a system user:

```java
TenantContext.executeAsSystem(() -> {
    // Hibernate filters are disabled here
    List<Contract> allGlobalContracts = contractRepository.findAll();
});
```

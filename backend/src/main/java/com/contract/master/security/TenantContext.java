package com.contract.master.security;

public class TenantContext {
    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> SYSTEM_CONTEXT = ThreadLocal.withInitial(() -> false);

    public static String getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    public static void setCurrentTenant(String tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    public static void clear() {
        CURRENT_TENANT.remove();
        SYSTEM_CONTEXT.remove();
    }

    public static boolean isSystemContext() {
        return SYSTEM_CONTEXT.get();
    }

    public static void executeAsSystem(Runnable task) {
        String originalTenant = getCurrentTenant();
        boolean originalSystem = isSystemContext();
        try {
            SYSTEM_CONTEXT.set(true);
            setCurrentTenant(null);
            task.run();
        } finally {
            SYSTEM_CONTEXT.set(originalSystem);
            if (originalTenant != null) {
                setCurrentTenant(originalTenant);
            } else {
                CURRENT_TENANT.remove();
            }
        }
    }
}

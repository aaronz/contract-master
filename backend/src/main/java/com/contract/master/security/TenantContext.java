package com.contract.master.security;

public class TenantContext {
    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    public static String getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    public static void setCurrentTenant(String tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }

    public static void executeAsSystem(Runnable task) {
        String originalTenant = getCurrentTenant();
        try {
            setCurrentTenant(null);
            task.run();
        } finally {
            if (originalTenant != null) {
                setCurrentTenant(originalTenant);
            } else {
                clear();
            }
        }
    }
}

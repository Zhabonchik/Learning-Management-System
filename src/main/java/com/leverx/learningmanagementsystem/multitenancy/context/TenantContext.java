package com.leverx.learningmanagementsystem.multitenancy.context;

public class TenantContext {

    private static final ThreadLocal<String> tenantId = new ThreadLocal<>();

    public static void setTenantId(String subdomain) {
        tenantId.set(subdomain);
    }

    public static String getTenantId() {
        return tenantId.get();
    }

    public static void clear() {
        tenantId.remove();
    }
}

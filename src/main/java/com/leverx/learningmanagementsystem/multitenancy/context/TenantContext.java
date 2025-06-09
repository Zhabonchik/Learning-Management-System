package com.leverx.learningmanagementsystem.multitenancy.context;

public class TenantContext {

    private static final ThreadLocal<String> tenantSubdomain = new ThreadLocal<>();

    public static void setTenantSubdomain(String subdomain) {
        tenantSubdomain.set(subdomain);
    }

    public static String getTenantSubdomain() {
        return tenantSubdomain.get();
    }

    public static void clear() {
        tenantSubdomain.remove();
    }
}

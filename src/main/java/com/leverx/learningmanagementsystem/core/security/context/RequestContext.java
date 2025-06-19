package com.leverx.learningmanagementsystem.core.security.context;

public class RequestContext {

    private static final ThreadLocal<String> tenantId = new ThreadLocal<>();
    private static final ThreadLocal<String> tenantSubdomain = new ThreadLocal<>();

    public static String getTenantId() {
        return tenantId.get();
    }

    public static String getTenantSubdomain() {
        return tenantSubdomain.get();
    }

    public static void setTenantId(String subdomain) {
        tenantId.set(subdomain);
    }

    public static void setTenantSubdomain(String subdomain) {
        tenantSubdomain.set(subdomain);
    }

    public static void clear() {
        tenantId.remove();
        tenantSubdomain.remove();
    }
}

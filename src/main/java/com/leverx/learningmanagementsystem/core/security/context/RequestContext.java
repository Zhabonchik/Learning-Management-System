package com.leverx.learningmanagementsystem.core.security.context;

import java.util.HashMap;
import java.util.Map;

public class RequestContext {

    public static final String TENANT_ID = "tenantId";
    public static final String TENANT_SUBDOMAIN = "tenantSubdomain";

    private static final ThreadLocal<Map<String, String>> context = ThreadLocal.withInitial(HashMap::new);

    public static String getTenantId() {
        return context.get().get(TENANT_ID);
    }

    public static String getTenantSubdomain() {
        return context.get().get(TENANT_SUBDOMAIN);
    }

    public static void setTenantId(String id) {
        context.get().put(TENANT_ID, id);
    }

    public static void setTenantSubdomain(String subdomain) {
        context.get().put(TENANT_SUBDOMAIN, subdomain);
    }

    public static void clear() {
        context.get().clear();
    }
}

package com.leverx.learningmanagementsystem.multitenancy.identifierresolver;

import com.leverx.learningmanagementsystem.multitenancy.context.TenantContext;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.util.Objects.nonNull;
import static org.hibernate.cfg.MultiTenancySettings.MULTI_TENANT_IDENTIFIER_RESOLVER;

@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver<String>, HibernatePropertiesCustomizer {

    public static final String PUBLIC = "public";

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantSubdomain = TenantContext.getTenantSubdomain();
        return (nonNull(tenantSubdomain)) ? tenantSubdomain : PUBLIC;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(MULTI_TENANT_IDENTIFIER_RESOLVER, this);
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return false;
    }
}

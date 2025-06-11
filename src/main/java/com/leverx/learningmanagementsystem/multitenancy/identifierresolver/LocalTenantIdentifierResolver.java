package com.leverx.learningmanagementsystem.multitenancy.identifierresolver;

import com.leverx.learningmanagementsystem.multitenancy.context.TenantContext;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.leverx.learningmanagementsystem.multitenancy.utils.MigrationUtils.PUBLIC;
import static com.leverx.learningmanagementsystem.multitenancy.utils.MigrationUtils.SCHEMA;
import static java.util.Objects.nonNull;
import static org.hibernate.cfg.MultiTenancySettings.MULTI_TENANT_IDENTIFIER_RESOLVER;

@Component
@Profile("local")
public class LocalTenantIdentifierResolver implements CurrentTenantIdentifierResolver<String>, HibernatePropertiesCustomizer {

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantId = TenantContext.getTenantId();
        return (nonNull(tenantId)) ? SCHEMA.formatted(tenantId) : PUBLIC;
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

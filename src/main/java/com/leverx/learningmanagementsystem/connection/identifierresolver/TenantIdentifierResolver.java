package com.leverx.learningmanagementsystem.connection.identifierresolver;

import com.leverx.learningmanagementsystem.core.security.context.RequestContext;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.leverx.learningmanagementsystem.db.constants.DatabaseConstants.PUBLIC;
import static java.util.Objects.nonNull;
import static org.hibernate.cfg.MultiTenancySettings.MULTI_TENANT_IDENTIFIER_RESOLVER;

@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver<String>, HibernatePropertiesCustomizer {

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantId = RequestContext.getTenantId();
        return (nonNull(tenantId)) ? tenantId : PUBLIC;
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

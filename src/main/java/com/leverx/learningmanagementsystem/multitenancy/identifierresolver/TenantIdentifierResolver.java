package com.leverx.learningmanagementsystem.multitenancy.identifierresolver;

import com.leverx.learningmanagementsystem.core.security.context.RequestContext;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

import static com.leverx.learningmanagementsystem.multitenancy.db.constants.DatabaseConstants.PUBLIC;
import static java.util.Objects.nonNull;

@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver<String> {

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantId = RequestContext.getTenantId();
        return (nonNull(tenantId)) ? tenantId : PUBLIC;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return false;
    }
}

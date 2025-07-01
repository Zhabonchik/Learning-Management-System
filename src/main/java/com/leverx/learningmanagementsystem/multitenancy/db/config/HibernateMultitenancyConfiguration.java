package com.leverx.learningmanagementsystem.multitenancy.db.config;

import com.leverx.learningmanagementsystem.multitenancy.db.tenant.resolver.TenantIdentifierResolver;
import lombok.AllArgsConstructor;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class HibernateMultitenancyConfiguration {

    private final TenantIdentifierResolver tenantIdentifierResolver;
    private final MultiTenantConnectionProvider<String> multiTenantConnectionProvider;

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return hibernateProperties -> {
            hibernateProperties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
            hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantIdentifierResolver);
        };
    }

}

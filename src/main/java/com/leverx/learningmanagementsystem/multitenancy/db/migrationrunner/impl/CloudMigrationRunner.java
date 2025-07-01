package com.leverx.learningmanagementsystem.multitenancy.db.migrationrunner.impl;

import com.leverx.learningmanagementsystem.btp.servicemanager.dto.SchemaBindingResponse;
import com.leverx.learningmanagementsystem.btp.servicemanager.service.ServiceManager;
import com.leverx.learningmanagementsystem.core.security.context.RequestContext;
import com.leverx.learningmanagementsystem.multitenancy.connection.datasource.config.DataSourceConfiguration;
import com.leverx.learningmanagementsystem.multitenancy.db.migrationrunner.AbstractMigrationRunner;
import com.leverx.learningmanagementsystem.multitenancy.db.migrationrunner.MigrationRunner;
import com.leverx.learningmanagementsystem.multitenancy.connection.provider.CustomMultiTenantConnectionProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.leverx.learningmanagementsystem.multitenancy.db.constants.DatabaseConstants.PUBLIC;
import static com.leverx.learningmanagementsystem.multitenancy.db.constants.DatabaseConstants.TENANT_ID;

@Component
@Slf4j
@AllArgsConstructor
@Profile("cloud")
public class CloudMigrationRunner extends AbstractMigrationRunner implements MigrationRunner {

    private final CustomMultiTenantConnectionProvider connectionProvider;
    private final ServiceManager serviceManager;
    private final DataSourceConfiguration dsConfig;

    @EventListener(ApplicationReadyEvent.class)
    public void runAll() {
        List<String> tenantIds = getAllTenantIds();

        tenantIds.forEach(this::run);
    }

    public void run(String tenantId) {
        try {
            RequestContext.setTenantId(tenantId);
            log.info("Tenant context in CloudSubscriptionService: {}", RequestContext.getTenantId());

            migrateSchema(connectionProvider, dsConfig.getDbChangelog());
        } finally {
            RequestContext.clear();
        }
    }

    private List<String> getAllTenantIds() {
        List<SchemaBindingResponse> schemaBindings = serviceManager.getServiceBindings();
        List<String> tenantIds = new ArrayList<>();

        schemaBindings.forEach(
                schemaBinding -> {
                    String tenantId = schemaBinding.labels().get(TENANT_ID).getFirst();
                    tenantIds.add(tenantId);
                }
        );

        tenantIds.add(PUBLIC);

        return tenantIds;
    }
}

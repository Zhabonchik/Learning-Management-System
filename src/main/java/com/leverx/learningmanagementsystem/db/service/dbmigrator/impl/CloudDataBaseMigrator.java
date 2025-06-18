package com.leverx.learningmanagementsystem.db.service.dbmigrator.impl;

import com.leverx.learningmanagementsystem.btp.servicemanager.dto.SchemaBindingResponse;
import com.leverx.learningmanagementsystem.btp.servicemanager.service.ServiceManager;
import com.leverx.learningmanagementsystem.core.security.context.TenantContext;
import com.leverx.learningmanagementsystem.db.service.dbmigrator.DataBaseMigrator;
import com.leverx.learningmanagementsystem.multitenancy.connectionprovider.CustomMultiTenantConnectionProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.leverx.learningmanagementsystem.db.constants.DatabaseConstants.PUBLIC;
import static com.leverx.learningmanagementsystem.db.constants.DatabaseConstants.TENANT_ID;

@Component
@Slf4j
@AllArgsConstructor
@Profile("cloud")
public class CloudDataBaseMigrator implements DataBaseMigrator {

    private final CustomMultiTenantConnectionProvider connectionProvider;
    private final ServiceManager serviceManager;

    @EventListener(ApplicationReadyEvent.class)
    public void migrateAllSchemas() {
        List<String> tenantIds = getAllTenantIds();

        tenantIds.forEach(this::migrateSchemaOnStartUp);
    }

    public void migrateSchemaOnStartUp(String tenantId) {
        TenantContext.setTenantId(tenantId);
        log.info("Tenant context in CloudSubscriptionService: {}", TenantContext.getTenantId());

        migrateSchema(connectionProvider);
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

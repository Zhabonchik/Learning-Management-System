package com.leverx.learningmanagementsystem.db.service.dbmigrator;

import com.leverx.learningmanagementsystem.btp.servicemanager.dto.SchemaBindingResponse;
import com.leverx.learningmanagementsystem.btp.servicemanager.service.ServiceManager;
import com.leverx.learningmanagementsystem.core.security.context.TenantContext;
import com.leverx.learningmanagementsystem.multitenancy.connectionprovider.CustomMultiTenantConnectionProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.leverx.learningmanagementsystem.db.utils.DatabaseUtils.PUBLIC;

@Component
@Slf4j
@AllArgsConstructor
@Profile("cloud")
public class CloudDataBaseMigrator implements DataBaseMigrator{

    private final CustomMultiTenantConnectionProvider connectionProvider;
    private final ServiceManager serviceManager;

    @EventListener(ApplicationReadyEvent.class)
    public void migrateAllSchemas() {
        List<String> tenantIds = getAllTenantIds();

        tenantIds.forEach(this::migrateSchemaOnStartUp);
    }

    public void migrateSchemaOnStartUp(String tenantId) {
        TenantContext.setTenantId(tenantId);

        migrateSchema(connectionProvider);
    }

    private List<SchemaBindingResponse> getAllSchemas() {
        return serviceManager.getServiceBindings();
    }

    private List<String> getAllTenantIds() {
        List<SchemaBindingResponse> schemaBindings = serviceManager.getServiceBindings();
        List<String> tenantIds = new ArrayList<>();

        schemaBindings.forEach(
                schemaBinding -> {
                    String tenantId = schemaBinding.labels().get("tenantId").getFirst();
                    tenantIds.add(tenantId);
                }
        );

        tenantIds.add(PUBLIC);

        return tenantIds;
    }

    /*private List<SchemaConfiguration> getSchemaConfigurations() {
        List<SchemaBindingResponse> schemas = getAllSchemas();
        List<SchemaConfiguration> configurations = new ArrayList<>();

        schemas.forEach(
                schema -> {
                    var schemaConfiguration = buildSchemaConfiguration(schema.credentials(), schema.labels());
                    configurations.add(schemaConfiguration);
                }
        );

        configurations.add(buildDefaultSchemaConfiguration());

        return configurations;
    }

    private SchemaConfiguration buildSchemaConfiguration(Map <String, String> credentials, Map<String, List<String>> labels) {
        return SchemaConfiguration.builder()
                .url(credentials.get("url"))
                .username(credentials.get("user"))
                .password(credentials.get("password"))
                .tenantId(labels.get("tenantId").getFirst())
                .build();
    }

    private SchemaConfiguration buildDefaultSchemaConfiguration() {
        return SchemaConfiguration.builder()
                .url(defaultDataSourceConfiguration.getUrl())
                .username(defaultDataSourceConfiguration.getUsername())
                .password(defaultDataSourceConfiguration.getPassword())
                .tenantId(PUBLIC)
                .build();
    }*/
}

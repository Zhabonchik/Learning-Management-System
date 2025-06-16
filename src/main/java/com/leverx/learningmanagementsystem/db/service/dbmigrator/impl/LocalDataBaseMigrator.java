package com.leverx.learningmanagementsystem.db.service.dbmigrator.impl;

import com.leverx.learningmanagementsystem.core.security.context.TenantContext;
import com.leverx.learningmanagementsystem.db.service.SchemaNameResolver;
import com.leverx.learningmanagementsystem.db.service.dbmigrator.DataBaseMigrator;
import com.leverx.learningmanagementsystem.multitenancy.connectionprovider.CustomMultiTenantConnectionProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.leverx.learningmanagementsystem.db.utils.DatabaseUtils.PUBLIC;

@Component
@Slf4j
@AllArgsConstructor
@Profile("local")
public class LocalDataBaseMigrator implements DataBaseMigrator {

    private final CustomMultiTenantConnectionProvider connectionProvider;
    private final JdbcTemplate jdbcTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void migrateAllSchemas() {
        List<String> schemas = getAllSchemas();
        schemas.add(PUBLIC);

        schemas.forEach(this::migrateSchemaOnStartUp);
    }

    public void migrateSchemaOnStartUp(String schemaName) {
        String tenantId = SchemaNameResolver.extractTenantId(schemaName);
        TenantContext.setTenantId(tenantId);

        migrateSchema(connectionProvider);
    }

    private List<String> getAllSchemas() {
        return jdbcTemplate.queryForList(
                "SELECT schema_name FROM information_schema.schemata" +
                        " WHERE schema_name LIKE 'schema_%'", String.class
        );
    }
}

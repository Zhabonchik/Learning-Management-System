package com.leverx.learningmanagementsystem.db.service.migrationrunner.impl;

import com.leverx.learningmanagementsystem.core.security.context.RequestContext;
import com.leverx.learningmanagementsystem.db.service.SchemaNameResolver;
import com.leverx.learningmanagementsystem.db.service.migrationrunner.DatabaseMigrationRunner;
import com.leverx.learningmanagementsystem.connection.provider.CustomMultiTenantConnectionProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.leverx.learningmanagementsystem.db.constants.DatabaseConstants.PUBLIC;

@Component
@Slf4j
@AllArgsConstructor
@Profile("local")
public class LocalDatabaseMigrationRunner implements DatabaseMigrationRunner {

    private final CustomMultiTenantConnectionProvider connectionProvider;
    private final JdbcTemplate jdbcTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void runAll() {
        List<String> tenantIds = getAllTenantIds();

        tenantIds.forEach(this::run);
    }

    public void run(String tenantId) {
        RequestContext.setTenantId(tenantId);
        log.info("Tenant context in LocalSubscriptionService: {}", RequestContext.getTenantId());

        migrateSchema(connectionProvider);
    }

    private List<String> getAllSchemas() {
        return jdbcTemplate.queryForList(
                "SELECT schema_name FROM information_schema.schemata" +
                        " WHERE schema_name LIKE 'schema_%'", String.class
        );
    }

    private List<String> getAllTenantIds() {
        List<String> schemas = getAllSchemas();
        schemas.add(PUBLIC);

        List<String> tenantIds = new ArrayList<>();
        schemas.forEach(schemaName -> tenantIds.add(SchemaNameResolver.extractTenantId(schemaName)));

        return tenantIds;
    }
}

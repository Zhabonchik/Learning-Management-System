package com.leverx.learningmanagementsystem.btp.registry.service.impl;

import com.leverx.learningmanagementsystem.btp.registry.service.TenantRegistryService;
import com.leverx.learningmanagementsystem.core.db.service.LocalDatabaseMigrator;
import com.leverx.learningmanagementsystem.core.db.service.SchemaNameResolver;
import com.leverx.learningmanagementsystem.multitenancy.connectionprovider.LocalMultitenantConnectionProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
@Profile("local")
public class LocalTenantRegistryService implements TenantRegistryService {

    private final JdbcTemplate jdbcTemplate;
    private final LocalDatabaseMigrator databaseMigrator;
    private final LocalMultitenantConnectionProvider multitenantConnectionProvider;

    @Override
    public void subscribeTenant(String tenantId) {
        String schemaName = SchemaNameResolver.configureSchemaName(tenantId);
        createSchema(schemaName);
        databaseMigrator.migrateSchema(schemaName);
    }

    @Override
    public void unsubscribeTenant(String tenantId) {
        String schemaName = SchemaNameResolver.configureSchemaName(tenantId);
        deleteSchema(schemaName);
        closeConnections(schemaName);
    }

    private void createSchema(String schemaName) {
        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS " + schemaName);
        log.info("Created schema [{}]", schemaName);
    }

    private void deleteSchema(String schemaName) {
        jdbcTemplate.execute("DROP SCHEMA IF EXISTS %s CASCADE".formatted(schemaName));
    }

    private void closeConnections(String schemaName) {
        multitenantConnectionProvider.removeDataSource(schemaName);
    }
}

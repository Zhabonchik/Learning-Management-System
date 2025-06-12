package com.leverx.learningmanagementsystem.btp.registry.service.impl;

import com.leverx.learningmanagementsystem.btp.registry.service.TenantRegistryService;
import com.leverx.learningmanagementsystem.core.db.service.DatabaseMigrator;
import com.leverx.learningmanagementsystem.multitenancy.connectionprovider.DataSourcesProvider;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

import static com.leverx.learningmanagementsystem.multitenancy.utils.MigrationUtils.SCHEMA;

@Service
@Slf4j
@AllArgsConstructor
@Profile("local")
public class LocalTenantRegistryService implements TenantRegistryService {

    private final JdbcTemplate jdbcTemplate;
    private final DataSourcesProvider dataSourcesProvider;
    private final DatabaseMigrator databaseMigrator;

    @Override
    public void subscribeTenant(String tenantId) {
        String schemaName = getSchemaName(tenantId);
        createSchema(schemaName);
        databaseMigrator.migrateSchema(schemaName);
    }

    @Override
    public void unsubscribeTenant(String tenantId) {
        String schemaName = getSchemaName(tenantId);
        deleteSchema(schemaName);
        closeConnections(schemaName);
    }

    private void createSchema(String schemaName) {
        log.info("Creating schema [{}]", schemaName);
        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS " + schemaName);
        log.info("Created schema [{}]", schemaName);
    }

    private void deleteSchema(String schemaName) {
        jdbcTemplate.execute("DROP SCHEMA IF EXISTS %s CASCADE".formatted(schemaName));
    }

    private void closeConnections(String schemaName) {
        DataSource ds = dataSourcesProvider.getTenantDataSources().remove(schemaName);
        if (ds instanceof HikariDataSource hikari) {
            hikari.close();
        }
    }

    private String getSchemaName(String tenantId) {
        return SCHEMA.formatted(tenantId);
    }
}

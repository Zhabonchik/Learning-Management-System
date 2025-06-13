package com.leverx.learningmanagementsystem.btp.registry.service.impl;

import com.leverx.learningmanagementsystem.btp.registry.service.SubscriptionService;
import com.leverx.learningmanagementsystem.core.security.context.TenantContext;
import com.leverx.learningmanagementsystem.db.service.dbmigrator.DataBaseMigrator;
import com.leverx.learningmanagementsystem.db.service.SchemaNameResolver;
import com.leverx.learningmanagementsystem.multitenancy.connectionprovider.CustomMultiTenantConnectionProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static com.leverx.learningmanagementsystem.btp.registry.utils.RegistryUtils.CREATE_SCHEMA;
import static com.leverx.learningmanagementsystem.btp.registry.utils.RegistryUtils.DROP_SCHEMA;
import static com.leverx.learningmanagementsystem.btp.registry.utils.RegistryUtils.ROUTER_URL;

@Service
@Slf4j
@AllArgsConstructor
@Profile("local")
public class LocalSubscriptionService implements SubscriptionService {

    private final JdbcTemplate jdbcTemplate;
    private final DataBaseMigrator databaseMigrator;
    private final CustomMultiTenantConnectionProvider multitenantConnectionProvider;

    @Override
    public String subscribe(String tenantId, String tenantSubDomain) {
        TenantContext.setTenantId(tenantId);
        String schemaName = SchemaNameResolver.configureSchemaName(tenantId);
        createSchema(schemaName);
        databaseMigrator.migrateSchema(multitenantConnectionProvider);
        return ROUTER_URL.formatted(tenantSubDomain);
    }

    @Override
    public void unsubscribe(String tenantId) {
        String schemaName = SchemaNameResolver.configureSchemaName(tenantId);
        deleteSchema(schemaName);
        closeConnections(schemaName);
    }

    private void createSchema(String schemaName) {
        log.info("Creating schema {}", schemaName);
        try (Connection connection = multitenantConnectionProvider.getConnection(TenantContext.getTenantId())){
            Statement statement = connection.createStatement();

            statement.execute(CREATE_SCHEMA.formatted(schemaName));
            log.info("Created schema [{}]", schemaName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteSchema(String schemaName) {
        log.info("Deleting schema {}", schemaName);
        try (Connection connection = multitenantConnectionProvider.getConnection(TenantContext.getTenantId())){
            Statement statement = connection.createStatement();

            statement.execute(DROP_SCHEMA.formatted(schemaName));
            log.info("Deleted schema [{}]", schemaName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeConnections(String schemaName) {
        multitenantConnectionProvider.removeDataSource(schemaName);
    }
}

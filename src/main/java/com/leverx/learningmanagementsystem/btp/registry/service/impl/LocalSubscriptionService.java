package com.leverx.learningmanagementsystem.btp.registry.service.impl;

import com.leverx.learningmanagementsystem.btp.registry.model.DependenciesResponseDto;
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
import java.util.Collections;
import java.util.List;

import static com.leverx.learningmanagementsystem.btp.registry.constants.RegistryConstants.CREATE_SCHEMA;
import static com.leverx.learningmanagementsystem.btp.registry.constants.RegistryConstants.DROP_SCHEMA;
import static com.leverx.learningmanagementsystem.btp.registry.constants.RegistryConstants.ROUTER_URL;

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

    @Override
    public List<DependenciesResponseDto> getDependencies() {
        return Collections.emptyList();
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

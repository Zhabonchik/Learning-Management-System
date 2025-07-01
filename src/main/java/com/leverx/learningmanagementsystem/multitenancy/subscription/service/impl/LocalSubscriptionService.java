package com.leverx.learningmanagementsystem.multitenancy.subscription.service.impl;

import com.leverx.learningmanagementsystem.multitenancy.subscription.model.DependenciesResponseDto;
import com.leverx.learningmanagementsystem.multitenancy.subscription.service.SubscriptionService;
import com.leverx.learningmanagementsystem.core.security.context.RequestContext;
import com.leverx.learningmanagementsystem.multitenancy.db.migrationrunner.MigrationRunner;
import com.leverx.learningmanagementsystem.multitenancy.db.schema.SchemaNameResolver;
import com.leverx.learningmanagementsystem.multitenancy.connection.provider.CustomMultiTenantConnectionProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

import static com.leverx.learningmanagementsystem.multitenancy.subscription.constants.SubscriptionConstants.CREATE_SCHEMA;
import static com.leverx.learningmanagementsystem.multitenancy.subscription.constants.SubscriptionConstants.DROP_SCHEMA;
import static com.leverx.learningmanagementsystem.multitenancy.subscription.constants.SubscriptionConstants.LOCAL_ROUTER_URL;

@Service
@Slf4j
@AllArgsConstructor
@Profile("local")
public class LocalSubscriptionService implements SubscriptionService {

    private final MigrationRunner databaseMigrator;
    private final CustomMultiTenantConnectionProvider multitenantConnectionProvider;

    @Override
    public String subscribe(String tenantId, String tenantSubDomain) {
        RequestContext.setTenantId(tenantId);

        String schemaName = SchemaNameResolver.configureSchemaName(tenantId);
        createSchema(schemaName);

        databaseMigrator.run(tenantId);

        return LOCAL_ROUTER_URL.formatted(tenantSubDomain);
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
        try (Connection connection = multitenantConnectionProvider.getConnection(RequestContext.getTenantId())){
            Statement statement = connection.createStatement();

            statement.execute(CREATE_SCHEMA.formatted(schemaName));
            log.info("Created schema [{}]", schemaName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteSchema(String schemaName) {
        log.info("Deleting schema {}", schemaName);
        try (Connection connection = multitenantConnectionProvider.getConnection(RequestContext.getTenantId())){
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

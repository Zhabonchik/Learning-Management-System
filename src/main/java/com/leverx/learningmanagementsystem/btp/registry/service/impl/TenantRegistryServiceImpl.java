package com.leverx.learningmanagementsystem.btp.registry.service.impl;

import com.leverx.learningmanagementsystem.btp.registry.service.TenantRegistryService;
import com.leverx.learningmanagementsystem.core.exception.model.CustomLiquibaseException;
import com.leverx.learningmanagementsystem.multitenancy.config.DataSourceConfiguration;
import com.leverx.learningmanagementsystem.multitenancy.connectionprovider.DataSourcesProvider;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;

@Service
@Slf4j
@AllArgsConstructor
public class TenantRegistryServiceImpl implements TenantRegistryService {

    private static final String DB_CHANGELOG = "db/changelog/db.changelog-master.yaml";

    private final DataSourceConfiguration dataSourceConfiguration;
    private final JdbcTemplate jdbcTemplate;
    private final DataSourcesProvider dataSourcesProvider;

    @Override
    public void subscribeTenant(String tenantSubdomain) {
        createSchema(tenantSubdomain);
        applyMigrations(tenantSubdomain);
    }

    @Override
    public void unsubscribeTenant(String tenantSubdomain) {
        deleteSchema(tenantSubdomain);
        closeConnections(tenantSubdomain);
    }

    private void createSchema(String schemaName) {
        log.info("Creating schema [{}]", schemaName);
        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS " + schemaName);
        log.info("Created schema [{}]", schemaName);
    }

    private void deleteSchema(String schemaName) {
        jdbcTemplate.execute("DROP SCHEMA IF EXISTS %s CASCADE".formatted(schemaName));
    }

    private void applyMigrations(String schemaName) {
        try {
            Connection connection = DriverManager.getConnection(
                    dataSourceConfiguration.getUrl(),
                    dataSourceConfiguration.getUsername(),
                    dataSourceConfiguration.getPassword());
            connection.setSchema(schemaName);

            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(
                    DB_CHANGELOG,
                    new ClassLoaderResourceAccessor(),
                    database
            );
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (Exception e) {
            log.info("Could not apply migrations to new schema.");
            throw new CustomLiquibaseException("Could not apply migrations to new schema");
        }
    }

    private void closeConnections(String schemaName) {
        DataSource ds = dataSourcesProvider.getTenantDataSources().remove(schemaName);
        if (ds instanceof HikariDataSource hikari) {
            hikari.close();
        }
    }
}

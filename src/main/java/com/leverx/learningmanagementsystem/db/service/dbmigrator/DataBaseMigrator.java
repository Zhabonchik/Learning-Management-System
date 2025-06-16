package com.leverx.learningmanagementsystem.db.service.dbmigrator;

import com.leverx.learningmanagementsystem.core.exception.model.CustomLiquibaseException;
import com.leverx.learningmanagementsystem.core.security.context.TenantContext;
import com.leverx.learningmanagementsystem.multitenancy.connectionprovider.CustomMultiTenantConnectionProvider;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;

import java.sql.Connection;

import static com.leverx.learningmanagementsystem.db.utils.DatabaseUtils.DB_CHANGELOG;
import static com.leverx.learningmanagementsystem.db.utils.DatabaseUtils.SCHEMA;

public interface DataBaseMigrator {

    default void migrateSchema(CustomMultiTenantConnectionProvider connectionProvider) {
        String tenantId = TenantContext.getTenantId();
        try (Connection connection = connectionProvider.getConnection(tenantId)) {

            Database dataBase = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(
                    DB_CHANGELOG,
                    new ClassLoaderResourceAccessor(),
                    dataBase
            );

            liquibase.update(new Contexts(), new LabelExpression());
        } catch (Exception e) {
            throw new CustomLiquibaseException("Could not apply migration to schema: "  + SCHEMA.formatted(tenantId));
        }
    }

    void migrateAllSchemas();

    void migrateSchemaOnStartUp(String tenantId);
}

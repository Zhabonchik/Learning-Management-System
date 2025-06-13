package com.leverx.learningmanagementsystem.core.db.service;

import com.leverx.learningmanagementsystem.core.exception.model.CustomLiquibaseException;
import com.leverx.learningmanagementsystem.core.security.context.TenantContext;
import com.leverx.learningmanagementsystem.multitenancy.connectionprovider.LocalMultitenantConnectionProvider;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.sql.Connection;

import static com.leverx.learningmanagementsystem.core.db.utils.DatabaseUtils.DB_CHANGELOG;

@Component
@Slf4j
@AllArgsConstructor
@Profile("local")
public class LocalDatabaseMigrator {

    private final LocalMultitenantConnectionProvider connectionProvider;

    public void migrateSchema(String schemaName) {
        String tenantId = SchemaNameResolver.extractTenantId(schemaName);
        TenantContext.setTenantId(tenantId);

        try (Connection connection = connectionProvider.getConnection(tenantId)) {

            log.info("Current tenant {}", TenantContext.getTenantId());

            Database dataBase = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(
                    DB_CHANGELOG,
                    new ClassLoaderResourceAccessor(),
                    dataBase
            );

            liquibase.update(new Contexts(), new LabelExpression());
        } catch (Exception e) {
            log.info("Could not apply migration to schema: {}", schemaName);
            log.error("Migration error", e);
            throw new CustomLiquibaseException("Could not apply migration to schema: " + schemaName);
        }
    }
}

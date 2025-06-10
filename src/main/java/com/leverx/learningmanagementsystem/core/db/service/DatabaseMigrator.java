package com.leverx.learningmanagementsystem.core.db.service;

import com.leverx.learningmanagementsystem.core.exception.model.CustomLiquibaseException;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

import static com.leverx.learningmanagementsystem.multitenancy.utils.MigrationUtils.DB_CHANGELOG;

@Component
@Slf4j
@AllArgsConstructor
public class DatabaseMigrator {

    private final DataSource dataSource;

    public void migrateSchema(String schemaName) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setSchema(schemaName);

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

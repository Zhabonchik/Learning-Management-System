package com.leverx.learningmanagementsystem.db.service.migrationrunner;

import com.leverx.learningmanagementsystem.core.exception.model.CustomLiquibaseException;
import com.leverx.learningmanagementsystem.core.security.context.RequestContext;
import com.leverx.learningmanagementsystem.connection.provider.CustomMultiTenantConnectionProvider;
import liquibase.integration.spring.SpringLiquibase;

import javax.sql.DataSource;

import static com.leverx.learningmanagementsystem.db.constants.DatabaseConstants.DB_CHANGELOG;
import static com.leverx.learningmanagementsystem.db.constants.DatabaseConstants.SCHEMA;

public interface DatabaseMigrationRunner {

    default void migrateSchema(CustomMultiTenantConnectionProvider connectionProvider) {
        String tenantId = RequestContext.getTenantId();
        try {
            DataSource dataSource = connectionProvider.getDataSource(tenantId);
            /*Database dataBase = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(
                    DB_CHANGELOG,
                    new ClassLoaderResourceAccessor(),
                    dataBase
            );

            liquibase.update(new Contexts(), new LabelExpression());*/
            SpringLiquibase liquibase = new SpringLiquibase();
            liquibase.setChangeLog(DB_CHANGELOG);
            liquibase.setDataSource(dataSource);

            liquibase.afterPropertiesSet();
        } catch (Exception e) {
            throw new CustomLiquibaseException("Could not apply migration to schema: "  + SCHEMA.formatted(tenantId));
        }
    }

    void runAll();

    void run(String tenantId);
}

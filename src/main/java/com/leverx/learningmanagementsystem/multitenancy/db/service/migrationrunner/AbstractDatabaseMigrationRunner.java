package com.leverx.learningmanagementsystem.multitenancy.db.service.migrationrunner;

import com.leverx.learningmanagementsystem.core.exception.model.CustomLiquibaseException;
import com.leverx.learningmanagementsystem.core.security.context.RequestContext;
import com.leverx.learningmanagementsystem.multitenancy.connection.provider.CustomMultiTenantConnectionProvider;
import liquibase.integration.spring.SpringLiquibase;

import javax.sql.DataSource;

import static com.leverx.learningmanagementsystem.multitenancy.db.constants.DatabaseConstants.SCHEMA;

public abstract class AbstractDatabaseMigrationRunner {

    public void migrateSchema(CustomMultiTenantConnectionProvider connectionProvider, String dbChangelog) {
        String tenantId = RequestContext.getTenantId();
        try {
            DataSource dataSource = connectionProvider.getDataSource(tenantId);
            SpringLiquibase liquibase = new SpringLiquibase();
            liquibase.setChangeLog(dbChangelog);
            liquibase.setDataSource(dataSource);

            liquibase.afterPropertiesSet();
        } catch (Exception e) {
            throw new CustomLiquibaseException("Could not apply migration to schema: "  + SCHEMA.formatted(tenantId));
        }
    }
}

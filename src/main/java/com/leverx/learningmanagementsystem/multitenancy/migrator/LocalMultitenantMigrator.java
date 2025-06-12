package com.leverx.learningmanagementsystem.multitenancy.migrator;

import com.leverx.learningmanagementsystem.core.db.service.DataSourceConfigurer;
import com.leverx.learningmanagementsystem.core.db.service.LocalDatabaseMigrator;
import com.leverx.learningmanagementsystem.core.db.service.SchemaNameResolver;
import com.leverx.learningmanagementsystem.core.security.context.TenantContext;
import com.leverx.learningmanagementsystem.multitenancy.routingdatasource.RoutingDataSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
@AllArgsConstructor
@Profile("local")
@Slf4j
public class LocalMultitenantMigrator {

    private final JdbcTemplate jdbcTemplate;
    private final LocalDatabaseMigrator databaseMigrator;
    private final RoutingDataSource routingDataSource;
    private final DataSourceConfigurer dsConfigurer;

    @EventListener(ApplicationReadyEvent.class)
    public void migrateAllSchemas() {
        List<String> schemas = getAllSchemas();
        configureRoutingDataSource(schemas);

        schemas.forEach(databaseMigrator::migrateSchema);
    }

    private List<String> getAllSchemas() {

        return jdbcTemplate.queryForList(
                "SELECT schema_name FROM information_schema.schemata" +
                        " WHERE schema_name LIKE 'schema_%'", String.class
        );
    }

    private void configureRoutingDataSource(List<String> schemas) {
        schemas.forEach(schema -> {
            DataSource dataSource = dsConfigurer.configureDataSource(schema);
            routingDataSource.addDataSource(schema, dataSource);
        });
    }
}

package com.leverx.learningmanagementsystem.multitenancy.migrator;

import com.leverx.learningmanagementsystem.core.db.service.DatabaseMigrator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.leverx.learningmanagementsystem.multitenancy.utils.MigrationUtils.PUBLIC;

@Component
@AllArgsConstructor
@Profile("local")
@Slf4j
public class LocalMultitenantMigrator {

    private final JdbcTemplate jdbcTemplate;
    private final DatabaseMigrator databaseMigrator;

    @EventListener(ApplicationReadyEvent.class)
    public void migrateAllSchemas() {
        List<String> schemas = getAllSchemas();

        schemas.forEach(databaseMigrator::migrateSchema);
    }

    private List<String> getAllSchemas() {
        List<String> schemas = jdbcTemplate.queryForList(
                "SELECT schema_name FROM information_schema.schemata" +
                        " WHERE schema_name LIKE 'schema_%'", String.class
        );
        schemas.add(PUBLIC);

        return schemas;
    }
}

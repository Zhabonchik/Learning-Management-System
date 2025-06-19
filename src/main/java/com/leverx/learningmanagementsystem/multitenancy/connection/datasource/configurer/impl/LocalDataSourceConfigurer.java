package com.leverx.learningmanagementsystem.multitenancy.connection.datasource.configurer.impl;

import com.leverx.learningmanagementsystem.multitenancy.connection.datasource.config.DataSourceConfiguration;
import com.leverx.learningmanagementsystem.multitenancy.connection.datasource.configurer.DataSourceConfigurer;
import com.leverx.learningmanagementsystem.multitenancy.db.service.SchemaNameResolver;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
@AllArgsConstructor
@Slf4j
@Profile({"local", "test"})
public class LocalDataSourceConfigurer implements DataSourceConfigurer {

    private final DataSourceConfiguration dsConfig;

    public DataSource configureDataSource(String tenantId) {
        try {
            String schemaName = SchemaNameResolver.configureSchemaName(tenantId);

            log.info("Configuring local DataSource for {}", tenantId);
            HikariDataSource dataSource = configureDataSourceWithoutSchema(dsConfig);
            dataSource.setSchema(schemaName);

            return dataSource;
        } catch (Exception e) {
            log.error("Failed to configure DataSource", e);
            return null;
        }
    }
}

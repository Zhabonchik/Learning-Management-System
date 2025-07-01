package com.leverx.learningmanagementsystem.multitenancy.connection.datasource.configurer.impl;

import com.leverx.learningmanagementsystem.multitenancy.connection.datasource.config.DataSourceConfiguration;
import com.leverx.learningmanagementsystem.multitenancy.connection.datasource.configurer.DataSourceConfigurer;
import com.leverx.learningmanagementsystem.multitenancy.db.schema.SchemaNameResolver;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

import static com.leverx.learningmanagementsystem.multitenancy.db.constants.DatabaseConstants.MAXIMUM_POOL_SIZE;
import static com.leverx.learningmanagementsystem.multitenancy.db.constants.DatabaseConstants.MINIMUM_IDLE;

@Service
@AllArgsConstructor
@Slf4j
@Profile({"local", "test"})
public class LocalDataSourceConfigurer implements DataSourceConfigurer {

    private final DataSourceConfiguration dataSourceConfiguration;

    @Override
    public DataSource configureDataSource(String tenantId) {
        try {
            String schemaName = SchemaNameResolver.configureSchemaName(tenantId);

            log.info("Configuring local RoutingDataSource for {}", tenantId);
            HikariDataSource dataSource = configureDataSourceWithoutSchema();
            dataSource.setSchema(schemaName);

            return dataSource;
        } catch (Exception e) {
            log.error("Failed to configure RoutingDataSource", e);
            return null;
        }
    }

    @Override
    public HikariDataSource configureDataSourceWithoutSchema() {
        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setJdbcUrl(dataSourceConfiguration.getUrl());
        dataSource.setUsername(dataSourceConfiguration.getUsername());
        dataSource.setPassword(dataSourceConfiguration.getPassword());
        dataSource.setDriverClassName(dataSourceConfiguration.getDriverClassName());
        dataSource.setMaximumPoolSize(MAXIMUM_POOL_SIZE);
        dataSource.setMinimumIdle(MINIMUM_IDLE);

        return dataSource;
    }
}

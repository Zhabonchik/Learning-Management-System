package com.leverx.learningmanagementsystem.core.db.service;

import com.leverx.learningmanagementsystem.core.db.config.DataSourceConfiguration;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static com.leverx.learningmanagementsystem.core.db.utils.DatabaseUtils.MAXIMUM_POOL_SIZE;
import static com.leverx.learningmanagementsystem.core.db.utils.DatabaseUtils.MINIMUM_IDLE;

@Service
@AllArgsConstructor
@Slf4j
public class DataSourceConfigurer {

    private final DataSourceConfiguration dsConfig;

    public DataSource configureDataSource(String schemaName) {
        try {
            String tenantId = SchemaNameResolver.extractTenantId(schemaName);

            log.info("Configuring DataSource for {}", tenantId);
            HikariDataSource dataSource = new HikariDataSource();

            dataSource.setJdbcUrl(dsConfig.getUrl());
            dataSource.setUsername(dsConfig.getUsername());
            dataSource.setPassword(dsConfig.getPassword());
            dataSource.setDriverClassName(dsConfig.getDriverClassName());
            dataSource.setSchema(schemaName);
            dataSource.setMaximumPoolSize(MAXIMUM_POOL_SIZE);
            dataSource.setMinimumIdle(MINIMUM_IDLE);

            return dataSource;
        } catch (Exception e) {
            log.error("Failed to configure DataSource", e);
            return null;
        }
    }
}

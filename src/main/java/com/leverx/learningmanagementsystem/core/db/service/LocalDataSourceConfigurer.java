package com.leverx.learningmanagementsystem.core.db.service;

import com.leverx.learningmanagementsystem.core.db.config.DataSourceConfiguration;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

import static com.leverx.learningmanagementsystem.core.db.utils.DatabaseUtils.MAXIMUM_POOL_SIZE;
import static com.leverx.learningmanagementsystem.core.db.utils.DatabaseUtils.MINIMUM_IDLE;

@Service
@AllArgsConstructor
@Slf4j
@Profile("local")
public class LocalDataSourceConfigurer {

    private final DataSourceConfiguration dsConfig;

    public DataSource configureLocalDataSource(String tenantId) {
        try {
            String schemaName = SchemaNameResolver.configureSchemaName(tenantId);

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

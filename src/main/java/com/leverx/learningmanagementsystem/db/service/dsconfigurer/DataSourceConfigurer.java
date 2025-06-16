package com.leverx.learningmanagementsystem.db.service.dsconfigurer;

import com.leverx.learningmanagementsystem.db.config.DataSourceConfiguration;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

import static com.leverx.learningmanagementsystem.db.constants.DatabaseConstants.MAXIMUM_POOL_SIZE;
import static com.leverx.learningmanagementsystem.db.constants.DatabaseConstants.MINIMUM_IDLE;

public interface DataSourceConfigurer {

    DataSource configureDataSource(String tenantId);

    default HikariDataSource configureDataSourceWithoutSchema(DataSourceConfiguration config) {
        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setJdbcUrl(config.getUrl());
        dataSource.setUsername(config.getUsername());
        dataSource.setPassword(config.getPassword());
        dataSource.setDriverClassName(config.getDriverClassName());
        dataSource.setMaximumPoolSize(MAXIMUM_POOL_SIZE);
        dataSource.setMinimumIdle(MINIMUM_IDLE);

        return dataSource;
    }
}

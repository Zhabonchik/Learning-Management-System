package com.leverx.learningmanagementsystem.multitenancy.connection.datasource.configurer;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public interface DataSourceConfigurer {

    DataSource configureDataSource(String tenantId);

    HikariDataSource configureDataSourceWithoutSchema();
}

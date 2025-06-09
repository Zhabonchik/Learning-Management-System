package com.leverx.learningmanagementsystem.multitenancy.connectionprovider;

import com.leverx.learningmanagementsystem.multitenancy.config.DataSourceConfiguration;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.hibernate.cfg.MultiTenancySettings.MULTI_TENANT_CONNECTION_PROVIDER;

@Component
@AllArgsConstructor
@Getter
public class LocalMultiTenantConnectionProvider implements MultiTenantConnectionProvider<String>, HibernatePropertiesCustomizer, DataSourcesProvider {

    public static final String PUBLIC = "public";

    private final Map<String, DataSource> tenantDataSources = new ConcurrentHashMap<>();
    private final DataSourceConfiguration dsConfig;


    @Override
    public Connection getAnyConnection() throws SQLException {
        return getConnection(PUBLIC);
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String schema) throws SQLException {
        DataSource tenantDataSource = tenantDataSources.computeIfAbsent(schema,
                this::createDataSourceForTenant);
        return tenantDataSource.getConnection();
    }

    @Override
    public void releaseConnection(String s, Connection connection) throws SQLException {
        connection.setSchema(PUBLIC);
        connection.close();
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(MULTI_TENANT_CONNECTION_PROVIDER, this);
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class<?> aClass) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return null;
    }

    private DataSource createDataSourceForTenant(String schema) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(dsConfig.getUrl());
        dataSource.setUsername(dsConfig.getUsername());
        dataSource.setPassword(dsConfig.getPassword());
        dataSource.setDriverClassName(dsConfig.getDriverClassName());
        dataSource.setSchema(schema);
        dataSource.setMaximumPoolSize(10);
        dataSource.setMinimumIdle(2);
        return dataSource;
    }
}

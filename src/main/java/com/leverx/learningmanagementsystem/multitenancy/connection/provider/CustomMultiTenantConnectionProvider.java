package com.leverx.learningmanagementsystem.multitenancy.connection.provider;

import com.leverx.learningmanagementsystem.multitenancy.connection.datasource.configurer.DataSourceConfigurer;
import com.leverx.learningmanagementsystem.multitenancy.connection.datasource.RoutingDataSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
@AllArgsConstructor
@Slf4j
public class CustomMultiTenantConnectionProvider implements MultiTenantConnectionProvider<String> {

    private final RoutingDataSource routingDataSource;
    private final DataSourceConfigurer dataSourceConfigurer;

    @Override
    public Connection getAnyConnection() throws SQLException {
        return routingDataSource.getResolvedDefaultDataSource().getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantId) throws SQLException {
        try {
            return routingDataSource.getConnection();
        } catch (IllegalStateException e) {
            log.info("Could not get connection for {}", tenantId);
            DataSource dataSource = dataSourceConfigurer.configureDataSource(tenantId);
            routingDataSource.addDataSource(tenantId, dataSource);

            return routingDataSource.getConnection();
        } catch (Exception e) {
            log.info("Exception in getConnection() with message: {}", e.getMessage());
            throw e;
        }
    }

    public DataSource getDataSource(String tenantId) {
        try {
            return routingDataSource.getDataSource();
        } catch (IllegalStateException e) {
            log.info("Could not get datasource for {}", tenantId);
            DataSource dataSource = dataSourceConfigurer.configureDataSource(tenantId);
            routingDataSource.addDataSource(tenantId, dataSource);

            return routingDataSource.getDataSource();
        }
    }

    @Override
    public void releaseConnection(String s, Connection connection) throws SQLException {
        connection.close();
    }

    public void removeDataSource(String tenantId) {
        routingDataSource.removeDataSource(tenantId);
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
}

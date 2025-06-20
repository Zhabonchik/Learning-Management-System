package com.leverx.learningmanagementsystem.multitenancy.connection.datasource;

import com.leverx.learningmanagementsystem.core.security.context.RequestContext;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Setter
public class RoutingDataSource extends AbstractRoutingDataSource {

    private final Map<Object, Object> targetDataSources = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        super.setTargetDataSources(targetDataSources);
        super.setLenientFallback(false);
        super.afterPropertiesSet();
    }

    @PreDestroy
    public void destroy() {
        closeConnections();
    }

    public synchronized void addDataSource(String tenantId, DataSource dataSource) {
        targetDataSources.put(tenantId, dataSource);
        super.afterPropertiesSet();
    }

    public synchronized void removeDataSource(String tenantId) {
        DataSource dataSource = (DataSource) targetDataSources.remove(tenantId);

        if (dataSource instanceof AutoCloseable closeable) {
            try {
                closeable.close();
            } catch (Exception e) {
                log.warn("Error closing RoutingDataSource for tenant {}", tenantId, e);
            }
        }

        super.afterPropertiesSet();
    }

    public synchronized DataSource getDataSource() {
        return determineTargetDataSource();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return RequestContext.getTenantId();
    }

    private void closeConnections() {
        targetDataSources.forEach((key, value) -> {
            HikariDataSource ds = (HikariDataSource) value;
            ds.close();
        });
    }
}

package com.leverx.learningmanagementsystem.multitenancy.connection.datasource;

import com.leverx.learningmanagementsystem.core.security.context.RequestContext;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Setter
public class DataSource extends AbstractRoutingDataSource {

    private final Map<Object, Object> targetDataSources = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        super.setTargetDataSources(targetDataSources);
        super.setLenientFallback(false);
        super.afterPropertiesSet();
    }

    public synchronized void addDataSource(String tenantId, javax.sql.DataSource dataSource) {
        targetDataSources.put(tenantId, dataSource);
        super.afterPropertiesSet();
    }

    public synchronized void removeDataSource(String tenantId) {
        javax.sql.DataSource dataSource = (javax.sql.DataSource) targetDataSources.remove(tenantId);

        if (dataSource instanceof AutoCloseable closeable) {
            try {
                closeable.close();
            } catch (Exception e) {
                log.warn("Error closing DataSource for tenant {}", tenantId, e);
            }
        }

        super.afterPropertiesSet();
    }

    public synchronized javax.sql.DataSource getDataSource() {
        return determineTargetDataSource();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return RequestContext.getTenantId();
    }
}

package com.leverx.learningmanagementsystem.connection.routingdatasource;

import com.leverx.learningmanagementsystem.core.security.context.RequestContext;
import jakarta.annotation.PostConstruct;
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
                log.warn("Error closing DataSource for tenant {}", tenantId, e);
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
}

package com.leverx.learningmanagementsystem.multitenancy.connection.datasource.configurer.impl;

import com.leverx.learningmanagementsystem.btp.servicemanager.dto.SchemaBindingResponse;
import com.leverx.learningmanagementsystem.btp.servicemanager.service.ServiceManager;
import com.leverx.learningmanagementsystem.multitenancy.connection.datasource.config.DataSourceConfiguration;
import com.leverx.learningmanagementsystem.multitenancy.connection.datasource.configurer.DataSourceConfigurer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

import java.util.Map;

import static com.leverx.learningmanagementsystem.multitenancy.db.constants.DatabaseConstants.DRIVER;
import static com.leverx.learningmanagementsystem.multitenancy.db.constants.DatabaseConstants.PASSWORD;
import static com.leverx.learningmanagementsystem.multitenancy.db.constants.DatabaseConstants.PUBLIC;
import static com.leverx.learningmanagementsystem.multitenancy.db.constants.DatabaseConstants.URL;
import static com.leverx.learningmanagementsystem.multitenancy.db.constants.DatabaseConstants.USER;

@Service
@AllArgsConstructor
@Slf4j
@Profile("cloud")
public class CloudDataSourceConfigurer implements DataSourceConfigurer {

    private final DataSourceConfiguration dataSourceConfiguration;
    private final ServiceManager serviceManager;

    @Override
    public DataSource configureDataSource(String tenantId) {
        DataSourceConfiguration dsConfig = new DataSourceConfiguration();
        if (tenantId.equals(PUBLIC)) {
            dsConfig = dataSourceConfiguration;
        } else {
            SchemaBindingResponse schemaBinding = serviceManager.getServiceBindingByTenantId(tenantId);
            Map<String, String> credentials = schemaBinding.credentials();

            dsConfig.setUsername(credentials.get(USER));
            dsConfig.setPassword(credentials.get(PASSWORD));
            dsConfig.setUrl(credentials.get(URL));
            dsConfig.setDriverClassName(credentials.get(DRIVER));
        }

        try {
            log.info("Configuring local RoutingDataSource for {}", tenantId);
            return configureDataSourceWithoutSchema(dsConfig);
        } catch (Exception e) {
            log.error("Failed to configure RoutingDataSource", e);
            return null;
        }
    }
}

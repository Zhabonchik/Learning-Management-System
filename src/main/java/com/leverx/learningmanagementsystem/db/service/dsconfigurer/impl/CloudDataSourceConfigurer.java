package com.leverx.learningmanagementsystem.db.service.dsconfigurer.impl;

import com.leverx.learningmanagementsystem.btp.servicemanager.dto.SchemaBindingResponse;
import com.leverx.learningmanagementsystem.btp.servicemanager.service.ServiceManager;
import com.leverx.learningmanagementsystem.db.config.DataSourceConfiguration;
import com.leverx.learningmanagementsystem.db.service.dsconfigurer.DataSourceConfigurer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

import java.util.Map;

import static com.leverx.learningmanagementsystem.db.utils.DatabaseUtils.PUBLIC;

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

            dsConfig.setUsername(credentials.get("user"));
            dsConfig.setPassword(credentials.get("password"));
            dsConfig.setUrl(credentials.get("url"));
            dsConfig.setDriverClassName(credentials.get("driver"));
        }

        try {
            log.info("Configuring local DataSource for {}", tenantId);
            return configureDataSourceWithoutSchema(dsConfig);
        } catch (Exception e) {
            log.error("Failed to configure DataSource", e);
            return null;
        }
    }
}

package com.leverx.learningmanagementsystem.multitenancy.dto.impl;

import com.leverx.learningmanagementsystem.multitenancy.dto.TenantDataSourceConfiguration;
import lombok.Data;
import org.springframework.context.annotation.Profile;

@Data
@Profile("cloud")
public class CloudTenantDataSourceConfiguration implements TenantDataSourceConfiguration {

    private String url;
    private String username;
    private String password;
    private String driverClassName;
}

package com.leverx.learningmanagementsystem.multitenancy.dto;

public interface TenantDataSourceConfiguration {

    String getUrl();

    String getUsername();

    String getPassword();

    String getDriverClassName();
}

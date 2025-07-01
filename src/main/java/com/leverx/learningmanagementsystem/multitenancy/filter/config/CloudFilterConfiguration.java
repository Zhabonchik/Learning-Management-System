package com.leverx.learningmanagementsystem.multitenancy.filter.config;

import com.leverx.learningmanagementsystem.multitenancy.filter.impl.CloudRequestContextFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("cloud")
public class CloudFilterConfiguration {

    @Bean
    public FilterRegistrationBean<CloudRequestContextFilter> tenantContextFilter() {
        FilterRegistrationBean<CloudRequestContextFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new CloudRequestContextFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(2);
        return filterRegistrationBean;
    }
}

package com.leverx.learningmanagementsystem.core.security.filter.config;

import com.leverx.learningmanagementsystem.core.security.filter.impl.ActuatorAndApiFilter;
import com.leverx.learningmanagementsystem.core.security.filter.impl.CloudRequestContextFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("cloud")
public class CloudFilterConfiguration {

    @Bean
    public FilterRegistrationBean<ActuatorAndApiFilter> actuatorAndApiFilter() {
        FilterRegistrationBean<ActuatorAndApiFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new ActuatorAndApiFilter());
        filterRegistrationBean.addUrlPatterns("/actuator/**", "/api/v1/**");
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<CloudRequestContextFilter> tenantContextFilter() {
        FilterRegistrationBean<CloudRequestContextFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new CloudRequestContextFilter());
        filterRegistrationBean.addUrlPatterns("/");
        filterRegistrationBean.setOrder(2);
        return filterRegistrationBean;
    }
}

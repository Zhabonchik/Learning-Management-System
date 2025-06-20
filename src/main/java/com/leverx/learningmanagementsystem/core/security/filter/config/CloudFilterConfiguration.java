package com.leverx.learningmanagementsystem.core.security.filter.config;

import com.leverx.learningmanagementsystem.core.security.filter.impl.ActuatorAndApiFilter;
import com.leverx.learningmanagementsystem.core.security.filter.impl.CloudRequestContextFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static com.leverx.learningmanagementsystem.core.security.constants.SecurityConstants.ACTUATOR_PATH;
import static com.leverx.learningmanagementsystem.core.security.constants.SecurityConstants.SUBSCRIPTIONS_PATH;

@Configuration
@Profile("cloud")
public class CloudFilterConfiguration {

    @Bean
    public FilterRegistrationBean<ActuatorAndApiFilter> actuatorAndApiFilter() {
        FilterRegistrationBean<ActuatorAndApiFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new ActuatorAndApiFilter());
        filterRegistrationBean.addUrlPatterns(ACTUATOR_PATH, SUBSCRIPTIONS_PATH);
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<CloudRequestContextFilter> tenantContextFilter(@Value("${approuter.name}") String approuterName) {
        FilterRegistrationBean<CloudRequestContextFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new CloudRequestContextFilter(approuterName));
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(2);
        return filterRegistrationBean;
    }
}

package com.leverx.learningmanagementsystem.core.audit.config;

import com.leverx.learningmanagementsystem.core.audit.service.SecurityAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AuditingConfiguration {

    @Bean
    AuditorAware<String> auditorAware() {
        return new SecurityAuditorAware();
    }
}

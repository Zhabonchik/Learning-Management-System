package com.leverx.learningmanagementsystem.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static com.leverx.learningmanagementsystem.config.security.Roles.MANAGER;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@Profile("dev")
public class SecurityFilterChainDev {

    @Bean
    public SecurityFilterChain actuatorSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .securityMatcher("/actuator/**")
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/actuator/health").permitAll()
                        .anyRequest().hasRole(MANAGER.name()))
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .securityMatcher("/**")
                .authorizeHttpRequests((requests) -> requests
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .build();
    }
}

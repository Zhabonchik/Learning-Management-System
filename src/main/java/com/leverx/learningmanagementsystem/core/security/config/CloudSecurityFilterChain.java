package com.leverx.learningmanagementsystem.core.security.config;

import com.leverx.learningmanagementsystem.core.security.converter.CustomTokenAuthenticationConverter;
import com.sap.cloud.security.xsuaa.XsuaaServiceConfiguration;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static com.leverx.learningmanagementsystem.core.security.model.AuthRoles.LMS_ADMIN;
import static com.leverx.learningmanagementsystem.core.security.model.AuthRoles.MANAGER;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@Profile("cloud")
@AllArgsConstructor
public class CloudSecurityFilterChain {

    private final XsuaaServiceConfiguration xsuaaServiceConfiguration;

    @Bean
    @Order(1)
    public SecurityFilterChain actuatorSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/actuator/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/actuator/health").permitAll()
                        .anyRequest().hasRole(MANAGER.name()))
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    /*@Bean
    @Order(2)
    public SecurityFilterChain xsuaaSubscribeSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/v1/subscribe/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/v1/subscribe/**").permitAll())
                .httpBasic(Customizer.withDefaults())
                .build();
    }*/

    @Bean
    @Order(2)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/v1/application-info").hasRole(LMS_ADMIN.name())
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(new CustomTokenAuthenticationConverter(xsuaaServiceConfiguration))))
                .build();
    }
}

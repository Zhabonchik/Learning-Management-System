package com.leverx.learningmanagementsystem.core.security.config;

import com.sap.cloud.security.xsuaa.XsuaaServiceConfiguration;
import com.sap.cloud.security.xsuaa.token.TokenAuthenticationConverter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static com.leverx.learningmanagementsystem.core.security.constants.SecurityConstants.ACTUATOR_HEALTH_PATH;
import static com.leverx.learningmanagementsystem.core.security.constants.SecurityConstants.ACTUATOR_PATH;
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
                .securityMatcher(ACTUATOR_PATH)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers(ACTUATOR_HEALTH_PATH).permitAll()
                        .anyRequest().hasRole(MANAGER.name()))
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(request -> request
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(tokenAuthenticationConverter())))
                .build();
    }

    private TokenAuthenticationConverter tokenAuthenticationConverter() {
        TokenAuthenticationConverter authenticationConverter = new TokenAuthenticationConverter(xsuaaServiceConfiguration);
        authenticationConverter.setLocalScopeAsAuthorities(true);
        return authenticationConverter;
    }
}

package com.leverx.learningmanagementsystem.core.security.config;

import com.leverx.learningmanagementsystem.core.security.model.SecurityEntityProperties;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.leverx.learningmanagementsystem.core.security.model.AuthRoles.USER;
import static com.leverx.learningmanagementsystem.core.security.model.AuthRoles.MANAGER;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final SecurityEntityProperties configuration;

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = getUser();
        UserDetails manager = getManager();
        return new InMemoryUserDetailsManager(user, manager);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private UserDetails getUser() {
        return User.builder()
                .username(configuration.getUser().username())
                .password(bCryptPasswordEncoder().encode(configuration.getUser().password()))
                .roles(USER.name())
                .build();
    }

    private UserDetails getManager() {
        return User.builder()
                .username(configuration.getManager().username())
                .password(bCryptPasswordEncoder().encode(configuration.getManager().password()))
                .roles(MANAGER.name())
                .build();
    }
}
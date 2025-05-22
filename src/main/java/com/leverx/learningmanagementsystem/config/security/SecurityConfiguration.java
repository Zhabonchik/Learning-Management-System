package com.leverx.learningmanagementsystem.config.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.leverx.learningmanagementsystem.config.security.Roles.USER;
import static com.leverx.learningmanagementsystem.config.security.Roles.MANAGER;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {

    private final SecurityEntityConfiguration configuration;

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
                .username(configuration.getUser().getUsername())
                .password(bCryptPasswordEncoder().encode(configuration.getUser().getPassword()))
                .authorities(USER.asRole())
                .build();
    }

    private UserDetails getManager() {
        return User.builder()
                .username(configuration.getManager().getUsername())
                .password(bCryptPasswordEncoder().encode(configuration.getManager().getPassword()))
                .authorities(MANAGER.asRole())
                .build();
    }
}
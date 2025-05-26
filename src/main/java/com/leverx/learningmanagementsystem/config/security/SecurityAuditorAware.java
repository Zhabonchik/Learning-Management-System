package com.leverx.learningmanagementsystem.config.security;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Optional;

public class SecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.isAuthenticated()) {
            return Optional.of("Anonymous");
        }

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            var username = authentication.getName();
            return Optional.ofNullable(username);
        } else if (authentication instanceof JwtAuthenticationToken) {
            Jwt token = (Jwt) authentication.getPrincipal();
            String clientId = token.getClaimAsString("client_id");
            return Optional.of(clientId);
        }

        return Optional.of("Could not identify user");
    }
}

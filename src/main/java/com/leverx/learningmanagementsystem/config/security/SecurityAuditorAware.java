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

        if (authentication instanceof UsernamePasswordAuthenticationToken token) {
            return extractUsername(token);
        } else if (authentication instanceof JwtAuthenticationToken token) {
            return extractClientId(token);
        }

        return Optional.of("Could not identify user");
    }

    private Optional<String> extractUsername(UsernamePasswordAuthenticationToken token) {
        var username = token.getName();
        return Optional.ofNullable(username);
    }

    private Optional<String> extractClientId(JwtAuthenticationToken authToken) {
        Jwt token = (Jwt) authToken.getPrincipal();
        String clientId = token.getClaimAsString("client_id");
        return Optional.of(clientId);
    }
}

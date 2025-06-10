package com.leverx.learningmanagementsystem.core.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.leverx.learningmanagementsystem.core.exception.model.TenantException;
import com.leverx.learningmanagementsystem.multitenancy.context.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.util.Objects.nonNull;

@Component
@Slf4j
@Profile("cloud")
public class CloudTenantInfoFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.startsWith("/actuator")) {
            filterChain.doFilter(request, response);
            return;
        }

        String bearerToken = request.getHeader("Authorization");
        if (nonNull(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            log.info("Bearer token: {}", token);
            DecodedJWT decodedJWT = JWT.decode(token);
            log.info("DecodedJWT payload: {}", decodedJWT.getPayload());
            String tenantId = decodedJWT.getClaim("zid").asString();
            log.info("TenantId: {}", tenantId);
            TenantContext.setTenantId(tenantId);
        } else {
            log.info("Bearer token is null or invalid");
            throw new TenantException("Authorization header is missing");
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}

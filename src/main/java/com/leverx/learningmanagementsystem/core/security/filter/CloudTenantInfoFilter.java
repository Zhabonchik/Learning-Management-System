package com.leverx.learningmanagementsystem.core.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.leverx.learningmanagementsystem.core.exception.model.TenantException;
import com.leverx.learningmanagementsystem.core.security.context.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.util.Objects.nonNull;

@Component
@Slf4j
@Profile("cloud")
public class CloudTenantInfoFilter extends OncePerRequestFilter {

    public static final String BEARER_PREFIX = "Bearer ";
    public static final String ZID = "zid";
    public static final String AUTHORIZATION = "Authorization";

    @Value("${APPROUTER_NAME}")
    private String approuterName;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.startsWith("/actuator") || path.startsWith("/api/v1")) {
            filterChain.doFilter(request, response);
            return;
        }

        String subdomain = extractTenantSubdomain(request);
        log.info("Tenant subdomain: {}", subdomain);

        String tenantId = extractTenantId(request);
        log.info("Tenant id: {}", tenantId);

        setTenantContext(tenantId, subdomain);

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }

    private String extractTenantId(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);

        if (nonNull(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            String token = bearerToken.substring(BEARER_PREFIX.length());
            log.info("Bearer token: {}", token);

            DecodedJWT decodedJWT = JWT.decode(token);
            log.info("DecodedJWT payload: {}", decodedJWT.getPayload());

            return decodedJWT.getClaim(ZID).asString();
        } else {
            log.info("Bearer token is null or invalid");
            throw new TenantException("Authorization header is missing");
        }
    }

    private String extractTenantSubdomain(HttpServletRequest request) {
        String serverName = request.getServerName();
        return serverName.substring(0, serverName.indexOf(approuterName));
    }

    private void setTenantContext(String tenantId, String tenantSubdomain) {
        TenantContext.setTenantId(tenantId);
        TenantContext.setTenantSubdomain(tenantSubdomain);
    }
}

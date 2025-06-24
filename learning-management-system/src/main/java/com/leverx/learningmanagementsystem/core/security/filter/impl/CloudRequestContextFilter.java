package com.leverx.learningmanagementsystem.core.security.filter.impl;

import com.leverx.learningmanagementsystem.core.security.context.RequestContext;
import com.leverx.learningmanagementsystem.core.security.filter.service.TenantExtractor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.leverx.learningmanagementsystem.core.security.constants.SecurityConstants.ACTUATOR_HEALTH_PATH;
import static com.leverx.learningmanagementsystem.core.security.constants.SecurityConstants.APPLICATION_INFO_PATH;

@Slf4j
@Profile("cloud")
@AllArgsConstructor
public class CloudRequestContextFilter extends OncePerRequestFilter {

    private String approuterName;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        TenantExtractor tenantExtractor = new TenantExtractor();

        String subdomain = tenantExtractor.extractTenantSubdomain(request.getServerName(), approuterName);
        log.info("Tenant subdomain: {}", subdomain);

        String tenantId = tenantExtractor.extractTenantId(request);
        log.info("Tenant id: {}", tenantId);

        tenantExtractor.setTenantContext(tenantId, subdomain);

        try {
            filterChain.doFilter(request, response);
        } finally {
            RequestContext.clear();
        }
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        return (requestURI.startsWith(ACTUATOR_HEALTH_PATH) || requestURI.startsWith(APPLICATION_INFO_PATH));
    }
}

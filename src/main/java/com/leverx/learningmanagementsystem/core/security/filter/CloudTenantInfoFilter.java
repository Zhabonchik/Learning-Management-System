package com.leverx.learningmanagementsystem.core.security.filter;

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

        String host = request.getServerName();
        int index = host.indexOf("-learning");

        if (index > 0) {
            String tenantSubdomain = host.substring(0, host.indexOf("-learning"));
            log.info("Tenant subdomain: {}", tenantSubdomain);
            TenantContext.setTenantSubdomain(tenantSubdomain);
        } else {
            log.info("Could not extract tenant subdomain from request");
            throw new TenantException("Could not extract tenant subdomain from request");
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}

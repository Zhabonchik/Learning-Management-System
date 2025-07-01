package com.leverx.learningmanagementsystem.multitenancy.filter.impl;

import com.leverx.learningmanagementsystem.core.security.context.RequestContext;
import com.sap.cloud.security.xsuaa.token.AuthenticationToken;
import com.sap.cloud.security.xsuaa.token.XsuaaToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

import static com.leverx.learningmanagementsystem.core.security.constants.SecurityConstants.ACTUATOR_HEALTH_PATH;
import static com.leverx.learningmanagementsystem.core.security.constants.SecurityConstants.APPLICATION_INFO_PATH;
import static com.leverx.learningmanagementsystem.core.security.constants.SecurityConstants.SUBSCRIPTIONS_PATH;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Profile("cloud")
@AllArgsConstructor
public class CloudRequestContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Optional<XsuaaToken> xsuaaTokenOpt = SecurityUtils.retrieveXsuaaTokenFromRequest(request);
            Optional<String> tenantIdOpt = xsuaaTokenOpt.map(XsuaaToken::getZoneId);
            Optional<String> subdomainOpt = xsuaaTokenOpt.map(XsuaaToken::getSubdomain);

            tenantIdOpt.ifPresent(RequestContext::setTenantId);
            subdomainOpt.ifPresent(RequestContext::setTenantSubdomain);

            filterChain.doFilter(request, response);
        } finally {
            RequestContext.clear();
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        return (requestURI.startsWith(ACTUATOR_HEALTH_PATH) || requestURI.startsWith(APPLICATION_INFO_PATH) || requestURI.startsWith(SUBSCRIPTIONS_PATH));
    }
}

@NoArgsConstructor(access = PRIVATE)
final class SecurityUtils {

    public static Optional<XsuaaToken> retrieveXsuaaTokenFromRequest(HttpServletRequest request) {
        Optional<Principal> userPrincipalOpt = Optional.ofNullable(request.getUserPrincipal());
        return userPrincipalOpt.filter(it -> it instanceof AuthenticationToken)
                .map(it -> (XsuaaToken) ((AuthenticationToken) it).getPrincipal());
    }
}

package com.leverx.learningmanagementsystem.core.security.filter.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.leverx.learningmanagementsystem.core.exception.model.TenantException;
import com.leverx.learningmanagementsystem.core.security.context.RequestContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.nonNull;

@Slf4j
public class TenantExtractor {

    public static final String BEARER_PREFIX = "Bearer ";
    public static final String ZID = "zid";
    public static final String AUTHORIZATION = "Authorization";

    public String extractTenantId(HttpServletRequest request) {
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

    public String extractTenantSubdomain(String serverName, String approuterName) {
        return serverName.substring(0, serverName.indexOf(approuterName) - 1);
    }

    public void setTenantContext(String tenantId, String tenantSubdomain) {
        RequestContext.setTenantId(tenantId);
        RequestContext.setTenantSubdomain(tenantSubdomain);
    }
}

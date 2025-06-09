package com.leverx.learningmanagementsystem.core.security.converter;

import com.sap.cloud.security.xsuaa.XsuaaServiceConfiguration;
import com.sap.cloud.security.xsuaa.token.TokenAuthenticationConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;

@Slf4j
public class CustomTokenAuthenticationConverter extends TokenAuthenticationConverter {

    public CustomTokenAuthenticationConverter(XsuaaServiceConfiguration xsuaaServiceConfiguration) {
        super(xsuaaServiceConfiguration);
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        AbstractAuthenticationToken authentication = super.convert(jwt);

        log.info("I am Custom converter");

        Map<String, Object> xsSystemAttributes = jwt.getClaim("xs.system.attributes");
        log.info("xssystem attributes: {}", xsSystemAttributes);
        List<String> roleCollections = Collections.emptyList();

        if (nonNull(xsSystemAttributes)) {
            Object rc = xsSystemAttributes.get("xs.rolecollections");
            if (rc instanceof List) {
                roleCollections = (List<String>) rc;
            }
        }

        log.info("Found role collections: {}", roleCollections);

        if (!roleCollections.isEmpty()) {
            List<GrantedAuthority> authorities = new ArrayList<>(authentication.getAuthorities());
            roleCollections.forEach(role ->
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role))
            );

            log.info("Final authorities: {}", authorities);
            return new JwtAuthenticationToken(jwt, authorities);
        }

        log.info("No role collections found");
        return authentication;
    }
}
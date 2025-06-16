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

@Slf4j
public class CustomTokenAuthenticationConverter extends TokenAuthenticationConverter {

    public CustomTokenAuthenticationConverter(XsuaaServiceConfiguration xsuaaServiceConfiguration) {
        super(xsuaaServiceConfiguration);
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        AbstractAuthenticationToken authentication = super.convert(jwt);
        log.info("Custom converter processing JWT token");

        List<String> roleCollections = extractRoleCollections(jwt);
        log.info("Found role collections: {}", roleCollections);

        if (roleCollections.isEmpty()) {
            log.info("No role collections found");
            return authentication;
        }

        return createAuthenticationWithAuthorities(jwt, authentication, roleCollections);
    }

    private List<String> extractRoleCollections(Jwt jwt) {
        Map<String, Object> xsSystemAttributes = jwt.getClaim("xs.system.attributes");
        log.debug("xs.system.attributes: {}", xsSystemAttributes);

        if (xsSystemAttributes == null) {
            return Collections.emptyList();
        }

        Object roleCollections = xsSystemAttributes.get("xs.rolecollections");
        return roleCollections instanceof List ? (List<String>) roleCollections : Collections.emptyList();
    }

    private JwtAuthenticationToken createAuthenticationWithAuthorities(
            Jwt jwt,
            AbstractAuthenticationToken authentication,
            List<String> roleCollections
    ) {
        List<GrantedAuthority> authorities = new ArrayList<>(authentication.getAuthorities());
        roleCollections.forEach(role ->
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role))
        );

        log.info("Final authorities: {}", authorities);
        return new JwtAuthenticationToken(jwt, authorities);
    }
}
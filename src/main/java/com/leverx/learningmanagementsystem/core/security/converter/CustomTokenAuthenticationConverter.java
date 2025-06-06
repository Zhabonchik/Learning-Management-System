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
import java.util.List;

@Slf4j
public class CustomTokenAuthenticationConverter extends TokenAuthenticationConverter {

    public CustomTokenAuthenticationConverter(XsuaaServiceConfiguration xsuaaServiceConfiguration) {
        super(xsuaaServiceConfiguration);
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        AbstractAuthenticationToken authentication = super.convert(jwt);

        log.info("I am Custom converter");
        log.info("Jwt token: {}", jwt);

        List<String> roleCollections = jwt.getClaimAsStringList("xs.system.attributes.xs.rolecollections");
        if (roleCollections != null) {
            List<GrantedAuthority> authorities = new ArrayList<>(authentication.getAuthorities());
            roleCollections.forEach(role ->
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role))
            );
            log.info("Converting JWT. Role collections: {}, Current authorities: {}",
                    roleCollections,
                    authorities);
            return new JwtAuthenticationToken(
                    jwt,
                    authorities,
                    authentication.getName()
            );
        }
        return authentication;
    }
}
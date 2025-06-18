package com.leverx.learningmanagementsystem.btp.xsuaa.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
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
public class CustomTokenAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        log.info("Jwt token subject: {}", jwt.getSubject());

        List<String> roleCollections = extractRoleCollections(jwt);
        log.info("Found role collections: {}", roleCollections);

        List<GrantedAuthority> authorities = new ArrayList<>();

        return createAuthenticationWithAuthorities(jwt, authorities, roleCollections);
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
            List<GrantedAuthority> authorities,
            List<String> roleCollections
    ) {
        roleCollections.forEach(role ->
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role))
        );

        log.info("Final authorities: {}", authorities);
        return new JwtAuthenticationToken(jwt, authorities);
    }
}

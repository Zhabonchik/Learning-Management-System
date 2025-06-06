package com.leverx.learningmanagementsystem.btp.registry.controller;

import com.leverx.learningmanagementsystem.btp.registry.model.RegistryRequestDto;
import com.leverx.learningmanagementsystem.btp.registry.model.RegistryResponseDto;
import com.leverx.learningmanagementsystem.btp.registry.model.XsuaaResponseDto;
import com.leverx.learningmanagementsystem.btp.registry.service.XsuaaService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Profile("cloud")
@Slf4j
public class SaasController {

    public static final String ROUTER_URL = "https://%s-learning-management-system-approuter.cfapps.us10-001.hana.ondemand.com";
    public static final String INFO_ENDPOINT = "https://067769ebtrial-dev-learning-management-system.cfapps.us10-001.hana.ondemand.com/api/v1/application-info";

    private final XsuaaService xsuaaService;

    @GetMapping("/application-info")
    public XsuaaResponseDto getXsuaaConfiguration() {
        return xsuaaService.getXsuaaInfo();
    }

    @PutMapping("/subscribe/{tenantId}")
    @ResponseStatus(OK)
    public RegistryResponseDto onSubscribe(@PathVariable("tenantId") String tenantId,
                                      @RequestBody RegistryRequestDto body) {
        String url = ROUTER_URL.formatted(body.subscribedSubdomain());
        log.info("Subscribing tenant [id = {}]", tenantId);
        return new RegistryResponseDto(url, INFO_ENDPOINT);
    }

    @DeleteMapping("/subscribe/{tenantId}")
    public void onUnsubscribe(@PathVariable("tenantId") String tenantId) {
        log.info("Unsubscribing tenant [id = {}]", tenantId);
    }

    @GetMapping("/check-token-details")
    public Map<String, Object> checkTokenDetails(JwtAuthenticationToken authentication) {
        return Map.of(
                "name", authentication.getName(),
                "authorities", authentication.getAuthorities().stream()
                        .map(Object::toString)
                        .collect(Collectors.toList()),
                "tokenAttributes", authentication.getTokenAttributes()
        );
    }
}

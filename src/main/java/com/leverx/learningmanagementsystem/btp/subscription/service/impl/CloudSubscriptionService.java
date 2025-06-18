package com.leverx.learningmanagementsystem.btp.subscription.service.impl;

import com.leverx.learningmanagementsystem.btp.appinfo.config.ApprouterConfiguration;
import com.leverx.learningmanagementsystem.btp.subscription.service.SubscriptionService;
import com.leverx.learningmanagementsystem.core.app.config.AppConfiguration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import static com.leverx.learningmanagementsystem.btp.subscription.constants.SubscriptionConstants.HTTPS_PROTOCOL;

@Service
@Profile("cloud")
@Slf4j
@AllArgsConstructor
public class CloudSubscriptionService implements SubscriptionService {

    private final AppConfiguration appConfiguration;
    private final ApprouterConfiguration approuterConfiguration;

    @Override
    public String subscribe(String tenantId, String tenantSubDomain) {
        String appUri = appConfiguration.getUri();
        String appName = appUri.substring(0,appUri.indexOf('.'));

        return "%s://%s-%s".formatted(
                HTTPS_PROTOCOL,
                tenantSubDomain,
                appUri.replace(appName, approuterConfiguration.getName()));
    }

    @Override
    public void unsubscribe(String tenantId) {
    }
}

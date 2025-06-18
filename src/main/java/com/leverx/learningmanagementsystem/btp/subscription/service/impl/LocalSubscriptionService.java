package com.leverx.learningmanagementsystem.btp.subscription.service.impl;

import com.leverx.learningmanagementsystem.btp.subscription.service.SubscriptionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import static com.leverx.learningmanagementsystem.btp.subscription.constants.SubscriptionConstants.LOCAL_ROUTER_URL;

@Service
@Slf4j
@AllArgsConstructor
@Profile("local")
public class LocalSubscriptionService implements SubscriptionService {

    @Override
    public String subscribe(String tenantId, String tenantSubDomain) {
        return LOCAL_ROUTER_URL.formatted(tenantSubDomain);
    }

    @Override
    public void unsubscribe(String tenantId) {
    }

}

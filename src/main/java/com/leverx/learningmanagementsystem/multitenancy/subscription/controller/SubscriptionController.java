package com.leverx.learningmanagementsystem.multitenancy.subscription.controller;

import com.leverx.learningmanagementsystem.multitenancy.subscription.model.DependenciesResponseDto;
import com.leverx.learningmanagementsystem.multitenancy.subscription.model.SubscriptionRequestDto;
import com.leverx.learningmanagementsystem.multitenancy.subscription.service.SubscriptionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/subscriptions")
@Profile({"cloud", "local"})
@Slf4j
@AllArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PutMapping("/tenants/{tenantId}")
    @ResponseStatus(OK)
    public String onSubscribe(@PathVariable("tenantId") String tenantId,
                              @RequestBody SubscriptionRequestDto body) {
        log.info("Subscribing tenant [id = {}]", tenantId);
        return subscriptionService.subscribe(tenantId, body.subscribedSubdomain());
    }

    @DeleteMapping("/tenants/{tenantId}")
    @ResponseStatus(NO_CONTENT)
    public void onUnsubscribe(@PathVariable("tenantId") String tenantId) {
        log.info("Unsubscribing tenant [id = {}]", tenantId);
        subscriptionService.unsubscribe(tenantId);
    }

    @GetMapping("/dependencies")
    @ResponseStatus(OK)
    public List<DependenciesResponseDto> getDependencies() {
        log.info("Returning dependencies");
        return subscriptionService.getDependencies();
    }
}

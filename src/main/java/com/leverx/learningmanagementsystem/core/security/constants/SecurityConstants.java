package com.leverx.learningmanagementsystem.core.security.constants;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class SecurityConstants {

    public static final String ACTUATOR_PATH = "/actuator/**";
    public static final String ACTUATOR_HEALTH_PATH = "/actuator/health";
    public static final String APPLICATION_INFO_PATH = "/api/v1/application-info";
    public static final String SUBSCRIPTIONS_PATH = "/api/v1/subscriptions/";
}

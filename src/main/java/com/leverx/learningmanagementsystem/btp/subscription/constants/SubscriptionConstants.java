package com.leverx.learningmanagementsystem.btp.subscription.constants;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class SubscriptionConstants {

    public static final String SCHEMA = "schema_%s";
    public static final String LOCAL_ROUTER_URL = "https://%s-learning-management-system-approuter.cfapps.us10-001.hana.ondemand.com";
    public static final String CREATE_SCHEMA = "CREATE SCHEMA IF NOT EXISTS %s";
    public static final String DROP_SCHEMA = "DROP SCHEMA IF EXISTS %s CASCADE";
    public static final String HTTPS_PROTOCOL = "https";
}

package com.leverx.learningmanagementsystem.multitenancy.db.constants;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class DatabaseConstants {

    /*public static final String DB_CHANGELOG = "db/changelog/db.changelog-master.yaml";*/
    public static final String SCHEMA = "schema_%s";
    public static final String PUBLIC = "public";
    public static final String TENANT_ID = "tenantId";
    public static final String USER = "user";
    public static final String PASSWORD = "password";
    public static final String URL = "url";
    public static final String DRIVER = "driver";
    public static final int MAXIMUM_POOL_SIZE = 10;
    public static final int MINIMUM_IDLE = 2;
}

package com.leverx.learningmanagementsystem.core.db.utils;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class DatabaseUtils {

    public static final String DB_CHANGELOG = "db/changelog/db.changelog-master.yaml";
    public static final String SCHEMA = "schema_%s";
    public static final String PUBLIC = "public";
    public static final int MAXIMUM_POOL_SIZE = 10;
    public static final int MINIMUM_IDLE = 2;
}

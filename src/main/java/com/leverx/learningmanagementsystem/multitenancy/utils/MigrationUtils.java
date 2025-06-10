package com.leverx.learningmanagementsystem.multitenancy.utils;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class MigrationUtils {

    public static final String DB_CHANGELOG = "db/changelog/db.changelog-master.yaml";
    public static final String SCHEMA = "schema_%s";
    public static final String PUBLIC = "public";
}

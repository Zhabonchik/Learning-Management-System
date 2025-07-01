package com.leverx.learningmanagementsystem.multitenancy.db.migrationrunner;

public interface MigrationRunner {

    void runAll();

    void run(String tenantId);
}

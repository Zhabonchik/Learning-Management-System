package com.leverx.learningmanagementsystem.multitenancy.db.service.migrationrunner;

public interface DatabaseMigrationRunner {

    void runAll();

    void run(String tenantId);
}

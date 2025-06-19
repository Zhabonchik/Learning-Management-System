package com.leverx.learningmanagementsystem.multitenancy.db.service;

import static com.leverx.learningmanagementsystem.btp.subscription.constants.SubscriptionConstants.SCHEMA;
import static com.leverx.learningmanagementsystem.multitenancy.db.constants.DatabaseConstants.PUBLIC;

public class SchemaNameResolver {

    public static String extractTenantId(String schemaName) {
        return schemaName.equals(PUBLIC)
                ? schemaName
                : schemaName.substring(schemaName.indexOf('_') + 1);
    }

    public static String configureSchemaName(String tenantId) {
        return tenantId.equals(PUBLIC)
                ? tenantId
                : SCHEMA.formatted(tenantId);
    }
}

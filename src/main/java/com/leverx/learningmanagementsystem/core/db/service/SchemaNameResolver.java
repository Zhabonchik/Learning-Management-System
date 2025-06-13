package com.leverx.learningmanagementsystem.core.db.service;

import static com.leverx.learningmanagementsystem.btp.registry.utils.RegistryUtils.SCHEMA;
import static com.leverx.learningmanagementsystem.core.db.utils.DatabaseUtils.PUBLIC;

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

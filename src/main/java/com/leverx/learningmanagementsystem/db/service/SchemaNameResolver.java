package com.leverx.learningmanagementsystem.db.service;

import static com.leverx.learningmanagementsystem.btp.registry.constants.RegistryConstants.SCHEMA;
import static com.leverx.learningmanagementsystem.db.constants.DatabaseConstants.PUBLIC;

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

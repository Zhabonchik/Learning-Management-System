package com.leverx.learningmanagementsystem.multitenancy.connectionprovider;

import javax.sql.DataSource;
import java.util.Map;

public interface DataSourcesProvider {

    Map<String, DataSource> getTenantDataSources();
}

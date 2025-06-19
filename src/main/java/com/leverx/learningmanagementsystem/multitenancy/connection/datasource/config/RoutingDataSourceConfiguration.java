package com.leverx.learningmanagementsystem.multitenancy.connection.datasource.config;

import com.leverx.learningmanagementsystem.multitenancy.connection.datasource.configurer.DataSourceConfigurer;
import com.leverx.learningmanagementsystem.multitenancy.connection.datasource.DataSource;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.leverx.learningmanagementsystem.multitenancy.db.constants.DatabaseConstants.PUBLIC;

@Configuration
@AllArgsConstructor
public class RoutingDataSourceConfiguration {

    private final DataSourceConfigurer dataSourceConfigurer;

    @Bean
    public javax.sql.DataSource routingDataSource() {
        DataSource dataSource = new DataSource();

        javax.sql.DataSource defaultDataSource = dataSourceConfigurer.configureDataSource(PUBLIC);
        dataSource.setDefaultTargetDataSource(defaultDataSource);

        return dataSource;
    }

}

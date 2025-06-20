package com.leverx.learningmanagementsystem.multitenancy.connection.datasource.config;

import com.leverx.learningmanagementsystem.multitenancy.connection.datasource.RoutingDataSource;
import com.leverx.learningmanagementsystem.multitenancy.connection.datasource.configurer.DataSourceConfigurer;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

import static com.leverx.learningmanagementsystem.multitenancy.db.constants.DatabaseConstants.PUBLIC;

@Configuration
@AllArgsConstructor
public class RoutingDataSourceConfiguration {

    private final DataSourceConfigurer dataSourceConfigurer;

    @Bean
    public DataSource routingDataSource() {
        RoutingDataSource dataSource = new RoutingDataSource();

        DataSource defaultDataSource = dataSourceConfigurer.configureDataSource(PUBLIC);
        dataSource.setDefaultTargetDataSource(defaultDataSource);

        return dataSource;
    }

}

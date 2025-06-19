package com.leverx.learningmanagementsystem.db.config;

import com.leverx.learningmanagementsystem.db.service.dsconfigurer.DataSourceConfigurer;
import com.leverx.learningmanagementsystem.connection.routingdatasource.RoutingDataSource;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

import static com.leverx.learningmanagementsystem.db.constants.DatabaseConstants.PUBLIC;

@Configuration
@AllArgsConstructor
public class RoutingDataSourceConfiguration {

    private final DataSourceConfigurer dataSourceConfigurer;

    @Bean
    public DataSource routingDataSource() {
        RoutingDataSource routingDataSource = new RoutingDataSource();

        DataSource defaultDataSource = dataSourceConfigurer.configureDataSource(PUBLIC);
        routingDataSource.setDefaultTargetDataSource(defaultDataSource);

        return routingDataSource;
    }

}

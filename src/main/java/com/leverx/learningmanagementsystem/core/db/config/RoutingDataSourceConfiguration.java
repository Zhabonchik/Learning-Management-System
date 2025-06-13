package com.leverx.learningmanagementsystem.core.db.config;

import com.leverx.learningmanagementsystem.core.db.service.LocalDataSourceConfigurer;
import com.leverx.learningmanagementsystem.multitenancy.routingdatasource.RoutingDataSource;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

import static com.leverx.learningmanagementsystem.core.db.utils.DatabaseUtils.PUBLIC;

@Configuration
@AllArgsConstructor
public class RoutingDataSourceConfiguration {

    private final LocalDataSourceConfigurer localDataSourceConfigurer;

    @Bean
    public DataSource routingDataSource() {
        RoutingDataSource routingDataSource = new RoutingDataSource();

        DataSource defaultDataSource = localDataSourceConfigurer.configureLocalDataSource(PUBLIC);
        routingDataSource.setDefaultTargetDataSource(defaultDataSource);

        return routingDataSource;
    }

}

package com.jgarivera.vibur.springboot.starter;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vibur.dbcp.ViburDBCPDataSource;
import org.vibur.dbcp.ViburDataSource;

import javax.sql.DataSource;

@Configuration
@ConditionalOnClass(ViburDataSource.class)
@ConditionalOnMissingBean(DataSource.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(ViburDataSourceProperties.class)
public class ViburAutoConfiguration {

    @Bean
    public ViburDBCPDataSource dataSource(ViburDataSourceProperties properties) {
        ViburDBCPDataSource dataSource = new ViburDBCPDataSource();

        dataSource.setJdbcUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        dataSource.setDriverClassName(properties.getDriverClassName());
        dataSource.start();

        return dataSource;
    }
}

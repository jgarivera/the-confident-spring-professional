package com.jgarivera.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgarivera.ApplicationLauncher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

// This is needed by Spring in order to create an `ApplicationContext`
// Every method annotated with @Bean should have one instance created on startup
@Configuration
// Scan for components in the application launcher's package (root package) and subpackages
@ComponentScan(basePackageClasses = ApplicationLauncher.class)
@PropertySource("classpath:/application.properties")
@PropertySource(value = "classpath:/application-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
public class MyFancyPdfInvoicesApplicationConfiguration {

    // Since Jackson is a third-party library, and we cannot annotate
    // its ObjectMapper class directly, a bean method is provided
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    // This is declared so that undefined properties will throw an error
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}

package com.jgarivera.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgarivera.ApplicationLauncher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

// This is needed by Spring in order to create an `ApplicationContext`
// Every method annotated with @Bean should have one instance created on startup
@Configuration
// Scan for components in the application launcher's package (root package) and subpackages
@ComponentScan(basePackageClasses = ApplicationLauncher.class)
@PropertySource("classpath:/application.properties")
@PropertySource(value = "classpath:/application-${spring.profiles.active}.properties", ignoreResourceNotFound = true)

// Initialize Spring MVC default configuration
@EnableWebMvc
public class ApplicationConfiguration {

    // Since Jackson is a third-party library, and we cannot annotate
    // its ObjectMapper class directly, a bean method is provided
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    // This is responsible for making validation annotations on controller
    // arguments work
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    // This is declared so that undefined properties will throw an error
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setOrder(1);

        // This will render .html and .xhtml files using Thymeleaf
        // You can have many different view resolver beans for other templating libraries
        // such as Freemarker and Velocity. Just make sure their view names are unique
        // from another. e.g. Freemarker uses .ftl
        viewResolver.setViewNames(new String[]{"*.html", "*.xhtml"});

        return viewResolver;
    }

    // This is a Thymeleaf-specific configuration bean
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());

        return templateEngine;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();

        // Find templates from the classpath. e.g. src/main/resources/templates
        templateResolver.setPrefix("classpath:/templates/");

        // Disable caching since we are in development
        templateResolver.setCacheable(false);

        return templateResolver;
    }
}

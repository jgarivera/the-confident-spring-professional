package com.jgarivera.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgarivera.MyBankApplicationLauncher;
import com.jgarivera.service.TransactionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackageClasses = MyBankApplicationLauncher.class)
@PropertySource("classpath:/application.properties")
public class MyBankApplicationConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}

package com.hg.budget.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreConfig {

    @Bean
    public IdGenerator idGenerator() {
        return new DefaultIdGenerator();
    }
}

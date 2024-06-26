package com.hg.budget.core.config;

import com.hg.budget.core.client.DateTimeHolder;
import com.hg.budget.core.client.IdGenerator;
import com.hg.budget.core.infrastructure.SystemDateTimeHolder;
import com.hg.budget.core.infrastructure.SystemIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreConfig {

    @Bean
    public IdGenerator idGenerator() {
        return new SystemIdGenerator();
    }

    @Bean
    public DateTimeHolder dateTimeHolder() {
        return new SystemDateTimeHolder();
    }
}

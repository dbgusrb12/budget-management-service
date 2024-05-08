package com.hg.budget.external.config;

import com.hg.budget.core.factory.YamlPropertySourceFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:external.yaml", factory = YamlPropertySourceFactory.class)
@EnableFeignClients(basePackages = {"com.hg.budget.external.**"})
public class FeignConfig {

}
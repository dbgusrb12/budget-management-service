package com.hg.budget.domain.persistence.config;

import com.hg.budget.core.factory.YamlPropertySourceFactory;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@RequiredArgsConstructor
@PropertySource(value = "classpath:domain-persistence.yaml", factory = YamlPropertySourceFactory.class)
@EnableConfigurationProperties(DbProperties.class)
@EnableJpaRepositories("com.hg.budget.domain.persistence.**")
@EntityScan("com.hg.budget.domain.persistence.**")
public class DbConfig {

    private final DbProperties dbProperties;

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
            .driverClassName(dbProperties.getDriverClassName())
            .url(dbProperties.getUrl())
            .username(dbProperties.getUsername())
            .password(dbProperties.getPassword())
            .build();
    }
}

package com.hg.budget.was.core.security;

import com.google.gson.Gson;
import com.hg.budget.application.account.service.AccountQueryService;
import com.hg.budget.was.core.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/*
 * Spring Security 를 사용하지 않고 직접 구현하기위한 설정 class
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String JOIN_API_URL = "/api/v*/accounts/join";
    private static final String LOGIN_API_URL = "/api/v*/accounts/login";
    private final AccountQueryService accountQueryService;

    @Bean
    public Interceptor interceptor(Gson gson) {
        return new Interceptor(JOIN_API_URL, LOGIN_API_URL, gson, userDetailsService(), passwordEncoder(), new JwtUtil());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService(accountQueryService);
    }
}

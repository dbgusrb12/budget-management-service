package com.hg.budget.was.core.security;

import com.google.gson.Gson;
import com.hg.budget.application.account.service.AccountQueryService;
import com.hg.budget.was.core.security.jwt.JwtUtil;
import com.hg.budget.was.core.security.process.AuthenticationProcessingManager;
import com.hg.budget.was.core.security.process.JoinRequestProcessing;
import com.hg.budget.was.core.security.process.JwtRequestProcessing;
import com.hg.budget.was.core.security.process.LoginRequestProcessing;
import java.util.List;
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
    private final Gson gson;

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService(accountQueryService);
    }

    @Bean
    public AuthenticationProcessingManager authenticationProcessingManager() {
        return new AuthenticationProcessingManager(
            new JoinRequestProcessing(JOIN_API_URL),
            new LoginRequestProcessing(LOGIN_API_URL, gson, userDetailsService(), passwordEncoder(), jwtUtil()),
            new JwtRequestProcessing(List.of(JOIN_API_URL, LOGIN_API_URL), jwtUtil())
        );
    }

    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor(authenticationProcessingManager());
    }
}

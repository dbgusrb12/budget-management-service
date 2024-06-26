package com.hg.budget.was.core.security;

import com.google.gson.Gson;
import com.hg.budget.application.account.AccountQueryService;
import com.hg.budget.was.core.security.jwt.JwtUtil;
import com.hg.budget.was.core.security.process.AdminAuthorizeProcessing;
import com.hg.budget.was.core.security.process.AuthenticationProcessingChain;
import com.hg.budget.was.core.security.process.JoinRequestProcessing;
import com.hg.budget.was.core.security.process.JwtRequestProcessing;
import com.hg.budget.was.core.security.process.LoginRequestProcessing;
import com.hg.budget.was.core.security.process.UserAuthorizeProcessing;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/*
 * Spring Security 를 사용하지 않고 직접 구현하기위한 설정 class
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String WILDCARD_API_URL = "/api/**";
    private static final String JOIN_API_URL = "/api/v*/accounts/join";
    private static final String LOGIN_API_URL = "/api/v*/accounts/login";
    private static final RequestMatcher ADMIN_ONLY_API = new AntPathRequestMatcher("/api/v*/categories", HttpMethod.POST.name());
    private final AccountQueryService accountQueryService;
    private final Gson gson;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService(accountQueryService);
    }

    @Bean
    public AuthenticationProcessingChain authenticationProcessingManager() {
        final JwtUtil jwtUtil = new JwtUtil();
        return new AuthenticationProcessingChain(
            new JoinRequestProcessing(JOIN_API_URL),
            new LoginRequestProcessing(LOGIN_API_URL, gson, userDetailsService(), passwordEncoder(), jwtUtil),
            new JwtRequestProcessing(WILDCARD_API_URL, jwtUtil),
            new AdminAuthorizeProcessing(ADMIN_ONLY_API),
            new UserAuthorizeProcessing(WILDCARD_API_URL)
        );
    }

    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor(authenticationProcessingManager());
    }
}

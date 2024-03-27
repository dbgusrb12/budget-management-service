package com.hg.budget.was.core.security.process;

import com.hg.budget.was.core.exception.AuthenticationException;
import com.hg.budget.was.core.security.UserDetails;
import com.hg.budget.was.core.security.context.UserDetailsContextHolder;
import com.hg.budget.was.core.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

public class JwtRequestProcessing implements AuthenticationProcessing {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_HEADER_JWT_SCHEMA = "Bearer";

    private final RequestMatcher requiresAuthenticationRequestMatcher;
    private final JwtUtil jwtUtil;

    public JwtRequestProcessing(String pattern, JwtUtil jwtUtil) {
        this.requiresAuthenticationRequestMatcher = new AntPathRequestMatcher(pattern);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return requiresAuthenticationRequestMatcher.matches(request);
    }

    @Override
    public boolean attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        UserDetailsContextHolder.clearContext();
        final String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (!isJwtAuthentication(authorizationHeader)) {
            throw new AuthenticationException();
        }
        final String accessToken = authorizationHeader.substring(7);
        final UserDetails loginUser = jwtUtil.parseToken(accessToken);
        // TODO 권한 검사 추가 필요
        UserDetailsContextHolder.setContext(loginUser);
        return true;
    }

    private boolean isJwtAuthentication(String authorizationHeader) {
        if (!StringUtils.hasText(authorizationHeader)) {
            return false;
        }
        return authorizationHeader.startsWith(AUTHORIZATION_HEADER_JWT_SCHEMA);
    }
}

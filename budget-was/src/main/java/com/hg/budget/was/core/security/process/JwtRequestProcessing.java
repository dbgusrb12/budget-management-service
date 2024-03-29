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

/*
 * JWT 인증 process
 * JWT 인증은 세번째 인증 process 로 실행되며,
 * 앞의 chain 에 걸리지 않은 요청들을 전부 인증 시도를 한 뒤,
 * 인증에 성공하면 다음 chain 으로 넘어간다.
 *
 * 인증 process 는 다음과 같다.
 *
 * Authorization Header 를 가져와 Bearer 키워드가 붙은 토큰인지 확인 한 뒤,
 * JWT 토큰을 검증하고, Claim 을 기반으로 UserDetails 를 Context 에 저장한다.
 *
 * (주의) JWT Token 검증만 하고, 실제 DB 에 존재하는 회원인지는 체크하지 않는다.
 */
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
    public boolean continueChain() {
        return true;
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

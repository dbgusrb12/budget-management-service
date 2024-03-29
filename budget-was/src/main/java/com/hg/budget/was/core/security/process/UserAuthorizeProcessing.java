package com.hg.budget.was.core.security.process;

import com.hg.budget.was.core.exception.AccessDeniedException;
import com.hg.budget.was.core.security.UserDetails;
import com.hg.budget.was.core.security.context.UserDetailsContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/*
 * User 권한 인가 process
 * User 인가는 다섯번째 인증 process 로 실행되며,
 * JWT 인증 process 가 성공한 요청들을 대상으로 실행된다.
 * User 권한이 필요한 요청이 들어오면 인증 시도를 한 뒤,
 * 인증에 성공하면 chain 을 종료한다.
 */
public class UserAuthorizeProcessing implements AuthenticationProcessing {

    private final List<RequestMatcher> requiresAuthenticationRequestMatchers;
    private static final List<String> PERMIT = List.of("ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER");

    public UserAuthorizeProcessing(String... pattern) {
        this.requiresAuthenticationRequestMatchers = Arrays.stream(pattern)
            .map(AntPathRequestMatcher::new)
            .collect(Collectors.toList());
    }

    @Override
    public int getOrder() {
        return 4;
    }

    @Override
    public boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return requiresAuthenticationRequestMatchers.stream()
            .anyMatch(requestMatcher -> requestMatcher.matches(request));
    }

    @Override
    public boolean continueChain() {
        return false;
    }

    @Override
    public boolean attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final UserDetails loginUser = UserDetailsContextHolder.getContext();
        if (hasPermit(loginUser.getRole())) {
            return true;
        }
        throw new AccessDeniedException();
    }

    private boolean hasPermit(String role) {
        return PERMIT.contains(role);
    }
}

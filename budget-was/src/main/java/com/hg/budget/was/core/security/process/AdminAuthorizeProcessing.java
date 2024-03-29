package com.hg.budget.was.core.security.process;

import com.hg.budget.was.core.exception.AccessDeniedException;
import com.hg.budget.was.core.security.UserDetails;
import com.hg.budget.was.core.security.context.UserDetailsContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.security.web.util.matcher.RequestMatcher;

/*
 * Admin 권한 인가 process
 * Admin 인가는 네번째 인증 process 로 실행되며,
 * JWT 인증 process 가 성공한 요청들을 대상으로 실행된다.
 * Admin 권한이 필요한 요청이 들어오면 인증 시도를 한 뒤,
 * 인증에 성공하거나, Admin 권한이 필요한 요청이 아니라면 다음 chain 으로 넘어간다.
 */
public class AdminAuthorizeProcessing implements AuthenticationProcessing {

    private final List<RequestMatcher> requiresAuthenticationRequestMatchers;

    public AdminAuthorizeProcessing(RequestMatcher... requiresAuthenticationRequestMatchers) {
        this.requiresAuthenticationRequestMatchers = List.of(requiresAuthenticationRequestMatchers);
    }

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return requiresAuthenticationRequestMatchers.stream()
            .anyMatch(requestMatcher -> requestMatcher.matches(request));
    }

    @Override
    public boolean continueChain() {
        return true;
    }

    @Override
    public boolean attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final UserDetails loginUser = UserDetailsContextHolder.getContext();
        if (hasPermitAdmin(loginUser.getRole())) {
            return true;
        }
        throw new AccessDeniedException();
    }

    private boolean hasPermitAdmin(String role) {
        return "ROLE_ADMIN".equals(role);
    }
}

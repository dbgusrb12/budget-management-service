package com.hg.budget.was.core.security.process;

import com.hg.budget.was.core.exception.AccessDeniedException;
import com.hg.budget.was.core.security.UserDetails;
import com.hg.budget.was.core.security.context.UserDetailsContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.security.web.util.matcher.RequestMatcher;

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

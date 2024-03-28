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

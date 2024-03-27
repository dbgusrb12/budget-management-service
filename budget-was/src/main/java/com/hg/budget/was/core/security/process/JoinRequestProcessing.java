package com.hg.budget.was.core.security.process;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class JoinRequestProcessing implements AuthenticationProcessing {

    private final RequestMatcher joinRequestMatcher;

    public JoinRequestProcessing(String joinProcessesUrl) {
        this.joinRequestMatcher = new AntPathRequestMatcher(joinProcessesUrl);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return joinRequestMatcher.matches(request);
    }

    @Override
    public boolean attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }
}

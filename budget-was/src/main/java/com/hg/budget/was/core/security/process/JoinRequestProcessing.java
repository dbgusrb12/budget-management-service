package com.hg.budget.was.core.security.process;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/*
 * 회원 가입 요청 인증 process
 * 회원 가입은 첫번째 인증 process 로 실행되며,
 * 회원 가입 요청이 들어올 경우 다음 체인으로 넘어가지 않고,
 * 무조건 인증 성공 처리로 진행한다.
 */
public class JoinRequestProcessing implements AuthenticationProcessing {

    private final RequestMatcher joinRequestMatcher;

    public JoinRequestProcessing(String joinProcessesUrl) {
        this.joinRequestMatcher = new AntPathRequestMatcher(joinProcessesUrl, HttpMethod.POST.name());
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
    public boolean continueChain() {
        return false;
    }

    @Override
    public boolean attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }
}

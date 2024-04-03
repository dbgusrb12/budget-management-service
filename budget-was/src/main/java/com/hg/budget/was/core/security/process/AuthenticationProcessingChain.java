package com.hg.budget.was.core.security.process;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/*
 * AuthenticationProcessing 리스트를 chain 형태로 실행한다.
 * 첫번째 process 부터 진행하며,
 * 인증이 필요한 요청인지 확인하고,
 * 인증이 필요한 요청이라면 인증 시도를 한 뒤,
 * 다음 chain 으로 넘어갈지 바로 인증을 끝낼지 정한다.
 */
public class AuthenticationProcessingChain {

    private final List<AuthenticationProcessing> authenticationProcessingList;

    public AuthenticationProcessingChain(AuthenticationProcessing... authenticationProcessingList) {
        this.authenticationProcessingList = Arrays.stream(authenticationProcessingList)
            .sorted(Comparator.comparing(AuthenticationProcessing::getOrder))
            .collect(Collectors.toList());
    }

    public boolean authenticate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        for (AuthenticationProcessing authenticationProcessing : authenticationProcessingList) {
            if (authenticationProcessing.requiresAuthentication(request, response)) {
                final boolean authentication = authenticationProcessing.attemptAuthentication(request, response);
                if (authenticationProcessing.continueChain()) {
                    continue;
                }
                return authentication;
            }
        }
        return true;
    }
}

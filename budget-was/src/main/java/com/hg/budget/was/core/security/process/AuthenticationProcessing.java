package com.hg.budget.was.core.security.process;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AuthenticationProcessing {

    /*
     * 인증 process 의 order 설정
     * AuthenticationProcessingManager 에서 해당 order 의 오름차순으로 정렬 후 실행한다.
     */
    int getOrder();

    /*
     * 인증 process 의 진행 여부 설정
     * 해당 반환값이 false 라면 인증을 시도하지 않고 넘어간다.
     */
    boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response);

    /*
     * 다음 인증 process 의 진행 여부 설정
     * 해당 반환값이 false 라면 다음 인증을 시도하지 않고 종료한다.
     */
    boolean continueChain();

    /*
     * 인증 process 실행
     */
    boolean attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException;
}

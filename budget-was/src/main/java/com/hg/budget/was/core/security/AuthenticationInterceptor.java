package com.hg.budget.was.core.security;

import com.hg.budget.was.core.security.context.UserDetailsContextHolder;
import com.hg.budget.was.core.security.process.AuthenticationProcessingManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class AuthenticationInterceptor implements HandlerInterceptor {

    private final AuthenticationProcessingManager authenticationProcessingManager;

    public AuthenticationInterceptor(AuthenticationProcessingManager authenticationProcessingManager) {
        this.authenticationProcessingManager = authenticationProcessingManager;
    }

    /*
     * 핸들러 실행 이전의 시점에 실행된다.
     *
     * 정확한 시정은 다음과 같다.
     * RequestMappingHandlerMapping 이 핸들러 객체를 결정한 후,
     * RequestMappingHandlerAdapter 가 핸들러를 호출하기 전에 실행된다.
     *
     * DispatcherServlet 이 HandlerExecutionChain 을 통해 실행한다.
     * HandlerExecutionChain 에는 등록한 인터셉터들이 존재하며, 여러개의 인터셉터들이 등록된 순서대로 실행된다.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return authenticationProcessingManager.authenticate(request, response);
    }

    /*
     * 핸들러가 실행된 이후의 시점에 실행된다.
     *
     * 정확한 시점은 다음과 같다.
     * RequestMappingHandlerAdapter 가 핸들러를 호출한 후,
     * DispatcherServlet 이 뷰를 렌더링 하기 전에 실행된다.
     * 그렇기 때문에 ModelAndView 를 파라미터로 전달받으며,
     * 해당 ModelAndView 객체를 조작해 추가 모델 객체를 노출 할 수 있다.
     *
     * DispatcherServlet 이 HandlerExecutionChain 을 통해 실행한다.
     * HandlerExecutionChain 에는 등록한 인터셉터들이 존재하며, 여러개의 인터셉터들이 등록된 순서와 반대로 실행된다.
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /*
     * 핸들러가 실행된 이후 실행된다.
     *
     * 정확한 시점은 다음과 같다.
     * DispatcherServlet 이 뷰를 렌더링 한 이후의 콜백으로 실행된다.
     * 또는, 에러가 발생했을 때 콜백으로 실행된다.
     *
     * DispatcherServlet 이 HandlerExecutionChain 을 통해 실행한다.
     * HandlerExecutionChain 에는 등록한 인터셉터들이 존재하며, 여러개의 인터셉터들 중 preHandle 을 실행한 순서의 역순으로 실행된다.
     * ex) A,B,C 인터셉터가 등록되어 있고, B.preHandle 이 false 를 리턴했을 경우 A.afterCompletion 만 실행한다.
     *
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserDetailsContextHolder.clearContext();
    }
}

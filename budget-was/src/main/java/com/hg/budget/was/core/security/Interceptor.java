package com.hg.budget.was.core.security;

import com.google.gson.Gson;
import com.hg.budget.application.core.code.ApplicationCode;
import com.hg.budget.application.core.exception.ApplicationException;
import com.hg.budget.was.account.command.LoginCommand;
import com.hg.budget.was.account.response.LoginResponse;
import com.hg.budget.was.core.exception.AuthenticationException;
import com.hg.budget.was.core.response.OkResponse;
import com.hg.budget.was.core.security.context.UserDetailsContextHolder;
import com.hg.budget.was.core.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class Interceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_HEADER_JWT_SCHEMA = "Bearer";
    private final RequestMatcher joinRequestMatcher;
    private final RequestMatcher loginMatcher;
    private final Gson gson;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Interceptor(String joinProcessUrl, String loginUrl, Gson gson, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder,
        JwtUtil jwtUtil) {
        this.joinRequestMatcher = new AntPathRequestMatcher(joinProcessUrl);
        this.loginMatcher = new AntPathRequestMatcher(loginUrl);
        this.gson = gson;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
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
        UserDetailsContextHolder.clearContext();
        if (joinRequestMatcher.matches(request)) {
            return true;
        }

        if (loginMatcher.matches(request)) {
            final LoginCommand command = gson.fromJson(request.getReader(), LoginCommand.class);
            UserDetails loginUser = userDetailsService.loadUserByUsername(command.id());
            if (!passwordEncoder.matches(command.password(), loginUser.getPassword())) {
                throw new ApplicationException(ApplicationCode.BAD_CREDENTIALS);
            }
            final String accessToken = jwtUtil.issueToken(loginUser);
            final String responseBody = gson.toJson(new OkResponse<>(new LoginResponse(accessToken)));
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
            response.getWriter().println(responseBody);
            UserDetailsContextHolder.setContext(loginUser);
            return true;
        }
        final String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (!isJwtAuthentication(authorizationHeader)) {
            throw new AuthenticationException();
        }
        final String accessToken = authorizationHeader.substring(7);
        final UserDetails loginUser = jwtUtil.parseToken(accessToken);
        // TODO 권한 검사 추가 필요
        UserDetailsContextHolder.setContext(loginUser);
        return true;
    }

    private boolean isJwtAuthentication(String authorizationHeader) {
        if (!StringUtils.hasText(authorizationHeader)) {
            return false;
        }
        return authorizationHeader.startsWith(AUTHORIZATION_HEADER_JWT_SCHEMA);
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

package com.hg.budget.was.core.security.process;

import com.google.gson.Gson;
import com.hg.budget.application.core.code.ApplicationCode;
import com.hg.budget.application.core.exception.ApplicationException;
import com.hg.budget.was.account.command.LoginCommand;
import com.hg.budget.was.account.response.LoginResponse;
import com.hg.budget.was.core.response.OkResponse;
import com.hg.budget.was.core.security.UserDetails;
import com.hg.budget.was.core.security.UserDetailsService;
import com.hg.budget.was.core.security.context.UserDetailsContextHolder;
import com.hg.budget.was.core.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/*
 * 로그인 인증 process
 * 로그인 인증은 두번째 인증 process 로 실행되며,
 * 로그인 요청이 들어올 경우 인증 시도를 한 뒤,
 * 인증이 성공하면 chain 을 종료한다.
 *
 * 인증 process 는 다음과 같다.
 *
 * RequestBody 에 id, password 를 가져와 DB 의 유저와 비교 한 후,
 * JWT 토큰을 발급해 ResponseBody 에 넣는다.
 * 이후, DB 에서 조회한 UserDetails 를 Context 에 저장한다.
 */
public class LoginRequestProcessing implements AuthenticationProcessing {

    private final RequestMatcher loginRequestMatcher;
    private final Gson gson;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginRequestProcessing(
        String loginProcessUrl,
        Gson gson,
        UserDetailsService userDetailsService,
        PasswordEncoder passwordEncoder,
        JwtUtil jwtUtil
    ) {
        this.loginRequestMatcher = new AntPathRequestMatcher(loginProcessUrl);
        this.gson = gson;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return loginRequestMatcher.matches(request);
    }

    @Override
    public boolean continueChain() {
        return false;
    }

    @Override
    public boolean attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserDetailsContextHolder.clearContext();
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
}

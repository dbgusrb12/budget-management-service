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

package com.hg.budget.was.core.security.process;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AuthenticationProcessingManager {

    private final List<AuthenticationProcessing> authenticationProcessingList;

    public AuthenticationProcessingManager(AuthenticationProcessing... authenticationProcessingList) {
        this.authenticationProcessingList = Arrays.stream(authenticationProcessingList)
            .sorted(Comparator.comparing(AuthenticationProcessing::getOrder))
            .collect(Collectors.toList());
    }

    public boolean authenticate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        for (AuthenticationProcessing authenticationProcessing : authenticationProcessingList) {
            if (authenticationProcessing.requiresAuthentication(request, response)) {
                return authenticationProcessing.attemptAuthentication(request, response);
            }
        }
        return true;
    }
}

package com.hg.budget.was.core.exception;

import com.hg.budget.application.core.code.ApplicationCode;
import lombok.Getter;

@Getter
public class AuthenticationException extends RuntimeException {

    private final ApplicationCode applicationCode = ApplicationCode.INVALID_AUTHENTICATION_INFO;

    public AuthenticationException(Throwable cause) {
        super(ApplicationCode.INVALID_AUTHENTICATION_INFO.getMessage(), cause);
    }

    public AuthenticationException() {
        super(ApplicationCode.INVALID_AUTHENTICATION_INFO.getMessage());
    }
}

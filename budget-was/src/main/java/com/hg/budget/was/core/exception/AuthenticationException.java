package com.hg.budget.was.core.exception;

import com.hg.budget.application.core.code.ApplicationCode;
import lombok.Getter;

@Getter
public class AuthenticationException extends RuntimeException {

    private final ApplicationCode applicationCode = ApplicationCode.INVALID_AUTHENTICATION_INFO;
    private String logMessage;

    public AuthenticationException(String logMessage, Throwable cause) {
        super(ApplicationCode.INVALID_AUTHENTICATION_INFO.getMessage(), cause);
        this.logMessage = logMessage;
    }

    public AuthenticationException(String logMessage) {
        super(ApplicationCode.INVALID_AUTHENTICATION_INFO.getMessage());
        this.logMessage = logMessage;
    }

    public AuthenticationException(Throwable cause) {
        super(ApplicationCode.INVALID_AUTHENTICATION_INFO.getMessage(), cause);
    }

    public AuthenticationException() {
        super(ApplicationCode.INVALID_AUTHENTICATION_INFO.getMessage());
    }
}

package com.hg.budget.was.core.exception;

import com.hg.budget.application.core.code.ApplicationCode;
import lombok.Getter;

@Getter
public class AccessDeniedException extends RuntimeException {

    private final ApplicationCode applicationCode = ApplicationCode.ACCESS_DENIED_AUTHENTICATION;
    private String logMessage;

    public AccessDeniedException(String logMessage, Throwable cause) {
        super(ApplicationCode.ACCESS_DENIED_AUTHENTICATION.getMessage(), cause);
        this.logMessage = logMessage;
    }

    public AccessDeniedException(String logMessage) {
        super(ApplicationCode.ACCESS_DENIED_AUTHENTICATION.getMessage());
        this.logMessage = logMessage;
    }

    public AccessDeniedException(Throwable cause) {
        super(ApplicationCode.ACCESS_DENIED_AUTHENTICATION.getMessage(), cause);
    }

    public AccessDeniedException() {
        super(ApplicationCode.ACCESS_DENIED_AUTHENTICATION.getMessage());
    }
}
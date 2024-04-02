package com.hg.budget.was.core.exception;

import com.hg.budget.application.core.code.ApplicationCode;
import lombok.Getter;

@Getter
public class AccessDeniedException extends RuntimeException {

    private final ApplicationCode applicationCode = ApplicationCode.ACCESS_DENIED_AUTHENTICATION;

    public AccessDeniedException() {
        super(ApplicationCode.ACCESS_DENIED_AUTHENTICATION.getMessage());
    }
}
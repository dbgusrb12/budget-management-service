package com.hg.budget.application.exception;

import com.hg.budget.application.code.ApplicationCode;
import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private final ApplicationCode applicationCode;
    private String logMessage;

    public ApplicationException(ApplicationCode applicationCode, String logMessage, Throwable cause) {
        super(applicationCode.getMessage(), cause);
        this.applicationCode = applicationCode;
        this.logMessage = logMessage;
    }

    public ApplicationException(ApplicationCode applicationCode, String logMessage) {
        super(applicationCode.getMessage());
        this.applicationCode = applicationCode;
        this.logMessage = logMessage;
    }

    public ApplicationException(ApplicationCode applicationCode, Throwable cause) {
        super(applicationCode.getMessage(), cause);
        this.applicationCode = applicationCode;
    }

    public ApplicationException(ApplicationCode applicationCode) {
        super(applicationCode.getMessage());
        this.applicationCode = applicationCode;
    }
}

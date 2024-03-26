package com.hg.budget.was.core.response;

import com.hg.budget.application.core.code.ApplicationCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ErrorResponse {

    private final String name;
    private final String code;
    private final String message;

    public ErrorResponse(ApplicationCode applicationCode) {
        this.name = applicationCode.name();
        this.code = applicationCode.getCode();
        this.message = applicationCode.getMessage();
    }
}
package com.hg.budget.was.core.response;

import com.hg.budget.application.core.code.ApplicationCode;
import lombok.Getter;

@Getter
public class OkResponse<T> {

    public static final OkResponse<Void> OK = new OkResponse<>();

    private final String name = ApplicationCode.SUCCESS.name();
    private final String code = ApplicationCode.SUCCESS.getCode();
    private final String message = ApplicationCode.SUCCESS.getMessage();
    private T result;

    public OkResponse(T result) {
        this.result = result;
    }

    public OkResponse() {
    }
}

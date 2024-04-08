package com.hg.budget.was.account.response;

import com.hg.budget.application.account.dto.AccountDto;

public record MyInfoResponse(
    String id,
    String nickname,
    String status,
    String role,
    String signUpDateTime,
    String signInDateTime
) {

    public static MyInfoResponse from(AccountDto account) {
        return new MyInfoResponse(
            account.id(),
            account.nickname(),
            account.status(),
            account.role(),
            account.signUpDateTime(),
            account.signInDateTime()
        );
    }
}

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
            account.getId(),
            account.getNickname(),
            account.getStatus(),
            account.getRole(),
            account.getSignUpDateTime(),
            account.getSignInDateTime()
        );
    }
}

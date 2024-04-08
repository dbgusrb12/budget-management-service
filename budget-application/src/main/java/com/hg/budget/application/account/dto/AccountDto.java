package com.hg.budget.application.account.dto;

import com.hg.budget.core.client.DateTimeHolder;
import com.hg.budget.domain.account.Account;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountDto {

    private final String id;
    private final String password;
    private final String nickname;
    private final String status;
    private final String role;
    private final String signUpDateTime;
    private final String signInDateTime;

    public static AccountDto of(Account account, DateTimeHolder dateTimeHolder) {
        return new AccountDto(
            account.getId(),
            account.getPassword(),
            account.getNickname(),
            account.getStatus().name(),
            account.getRole().getValue(),
            dateTimeHolder.toString(account.getSignUpDateTime()),
            dateTimeHolder.toString(account.getSignInDateTime())
        );
    }
}

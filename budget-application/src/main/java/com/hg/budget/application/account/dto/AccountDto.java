package com.hg.budget.application.account.dto;

import com.hg.budget.domain.account.AccountRole;
import com.hg.budget.domain.account.AccountStatus;
import lombok.Getter;

@Getter
public class AccountDto {

    private final String id;
    private final String password;
    private final String nickname;
    private final String status;
    private final String role;
    private final String signUpDateTime;
    private final String signInDateTime;

    public AccountDto(
        String id,
        String password,
        String nickname,
        AccountStatus status,
        AccountRole role,
        String signUpDateTime,
        String signInDateTime
    ) {
        this.id = id;
        this.password = password;
        this.nickname = nickname;
        this.status = status.name();
        this.role = role.getValue();
        this.signUpDateTime = signUpDateTime;
        this.signInDateTime = signInDateTime;
    }
}

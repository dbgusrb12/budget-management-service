package com.hg.budget.application.account.service.dto;

import com.hg.budget.core.util.ObjectUtils;
import com.hg.budget.domain.account.AccountRole;
import com.hg.budget.domain.account.AccountStatus;
import java.time.LocalDateTime;
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
        LocalDateTime signUpDateTime,
        LocalDateTime signInDateTime
    ) {
        this.id = id;
        this.password = password;
        this.nickname = nickname;
        this.status = status.name();
        this.role = role.getValue();
        this.signUpDateTime = signUpDateTime.toString();
        this.signInDateTime = ObjectUtils.toStringOrNull(signInDateTime);
    }
}

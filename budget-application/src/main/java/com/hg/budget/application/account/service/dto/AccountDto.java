package com.hg.budget.application.account.service.dto;

import com.hg.budget.core.util.ObjectUtils;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class AccountDto {

    private final String id;
    private final String password;
    private final String nickname;
    private final String signUpDateTime;
    private final String signInDateTime;

    public AccountDto(
        String id,
        String password,
        String nickname,
        LocalDateTime signUpDateTime,
        LocalDateTime signInDateTime
    ) {
        this.id = id;
        this.password = password;
        this.nickname = nickname;
        this.signUpDateTime = signUpDateTime.toString();
        this.signInDateTime = ObjectUtils.toStringOrNull(signInDateTime);
    }
}

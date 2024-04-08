package com.hg.budget.application.budget.dto;

import com.hg.budget.domain.account.Account;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreatedUser {

    private final String id;
    private final String nickname;

    public static CreatedUser from(Account account) {
        return new CreatedUser(account.getId(), account.getNickname());
    }
}

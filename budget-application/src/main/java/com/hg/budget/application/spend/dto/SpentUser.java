package com.hg.budget.application.spend.dto;

import com.hg.budget.domain.account.Account;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SpentUser {

    private final String id;
    private final String nickname;

    public static SpentUser from(Account account) {
        return new SpentUser(account.getId(), account.getNickname());
    }
}

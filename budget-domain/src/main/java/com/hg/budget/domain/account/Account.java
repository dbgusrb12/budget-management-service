package com.hg.budget.domain.account;

import com.hg.budget.core.util.TimeUtils;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class Account {

    private final String id;
    private final String password;
    private final String nickname;
    private final LocalDateTime signUpDateTime;
    private final LocalDateTime updatedDateTime;
    private LocalDateTime signInDateTime;

    public static Account ofCreated(String id, String password, String nickname) {
        final LocalDateTime now = TimeUtils.now();
        return new Account(id, password, nickname, now, now);
    }
}

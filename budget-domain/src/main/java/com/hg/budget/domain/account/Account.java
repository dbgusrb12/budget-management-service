package com.hg.budget.domain.account;

import com.hg.budget.core.util.TimeUtils;
import java.time.LocalDateTime;
import java.util.Objects;
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
    private final AccountStatus status;
    private final AccountRole role;
    private final LocalDateTime signUpDateTime;
    private LocalDateTime signInDateTime;

    public static Account ofCreated(String id, String password, String nickname, AccountRole role) {
        final LocalDateTime now = TimeUtils.now();
        return new Account(id, password, nickname, AccountStatus.CREATED, role, now);
    }

    public static Account of(
        String id,
        String password,
        String nickname,
        String status,
        String role,
        LocalDateTime signUpDateTime,
        LocalDateTime signInDateTime
    ) {
        return new Account(id, password, nickname, AccountStatus.of(status), AccountRole.of(role), signUpDateTime, signInDateTime);
    }

    public static Account ofNotExist() {
        return new Account(null, null, null, null, null, null);
    }

    public AccountStatus getStatus() {
        if (this.status == AccountStatus.CREATED) {
            return AccountStatus.LIVED;
        }
        return this.status;
    }

    public boolean exist() {
        return this.status == AccountStatus.LIVED || this.status == AccountStatus.LEFT;
    }

    public boolean notExist() {
        return !exist();
    }

    public boolean isDuplicated() {
        return exist();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Account that = (Account) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

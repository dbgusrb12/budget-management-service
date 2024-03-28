package com.hg.budget.domain.persistence.account;

import com.hg.budget.core.config.IdGenerator;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "ACCOUNT")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountEntity {

    @Id
    private Long id;

    private String accountId;
    private String password;
    private String nickname;
    private String status;
    private String role;
    private LocalDateTime signUpDateTime;
    private LocalDateTime signInDateTime;

    public static AccountEntity of(
        IdGenerator idGenerator,
        String accountId,
        String password,
        String nickname,
        String status,
        String role,
        LocalDateTime signUpDateTime,
        LocalDateTime signInDateTime
    ) {
        return new AccountEntity(idGenerator.generateRandomLong(), accountId, password, nickname, status, role, signUpDateTime, signInDateTime);
    }

    public static AccountEntity ofUpdate(
        Long id,
        String accountId,
        String password,
        String nickname,
        String status,
        String role,
        LocalDateTime signUpDateTime,
        LocalDateTime signInDateTime
    ) {
        return new AccountEntity(id, accountId, password, nickname, status, role, signUpDateTime, signInDateTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccountEntity that = (AccountEntity) o;
        return Objects.equals(accountId, that.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }
}

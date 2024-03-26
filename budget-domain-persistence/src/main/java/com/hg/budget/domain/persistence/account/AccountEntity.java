package com.hg.budget.domain.persistence.account;

import com.hg.budget.core.util.IdGenerator;
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
    private LocalDateTime signUpDateTime;
    private LocalDateTime signInDateTime;

    public static AccountEntity of(
        String accountId,
        String password,
        String nickname,
        String status,
        LocalDateTime signUpDateTime,
        LocalDateTime signInDateTime
    ) {
        return new AccountEntity(IdGenerator.generateRandomLong(), accountId, password, nickname, status, signUpDateTime, signInDateTime);
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

package com.hg.budget.domain.account;

import static org.assertj.core.api.Assertions.assertThat;

import com.hg.budget.core.client.DateTimeHolder;
import com.hg.budget.domain.mock.MockDateTimeHolder;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AccountTest {

    @Test
    @DisplayName("ofCreated 메서드로 추가 할 유저 객체를 생성 할 수 있다.")
    void ofCreatedTest() {
        // given
        DateTimeHolder dateTimeHolder = new MockDateTimeHolder(LocalDateTime.of(2024, 3, 28, 0, 0, 0));
        // when
        final var account = Account.ofCreated("id", "password", "nickname", AccountRole.USER, dateTimeHolder);

        // then
        assertThat(account.getId()).isEqualTo("id");
        assertThat(account.getPassword()).isEqualTo("password");
        assertThat(account.getNickname()).isEqualTo("nickname");
        assertThat(account.getRole()).isEqualTo(AccountRole.USER);
        assertThat(account.getSignUpDateTime()).isEqualTo(LocalDateTime.of(2024, 3, 28, 0, 0, 0));
    }

    @Test
    @DisplayName("of 메서드로 유저 객체를 생성 할 수 있다.")
    void ofTest() {
        // given
        // when
        final var account = Account.of(
            "id",
            "password",
            "nickname",
            "LIVED",
            "ROLE_USER",
            LocalDateTime.of(2024, 3, 26, 0, 0, 0),
            LocalDateTime.of(2024, 3, 26, 0, 0, 0)
        );

        // then
        assertThat(account.getId()).isEqualTo("id");
        assertThat(account.getPassword()).isEqualTo("password");
        assertThat(account.getNickname()).isEqualTo("nickname");
        assertThat(account.getStatus()).isEqualTo(AccountStatus.LIVED);
        assertThat(account.getRole()).isEqualTo(AccountRole.USER);
        assertThat(account.getSignUpDateTime()).isEqualTo(LocalDateTime.of(2024, 3, 26, 0, 0, 0));
        assertThat(account.getSignInDateTime()).isEqualTo(LocalDateTime.of(2024, 3, 26, 0, 0, 0));
    }

    @Test
    @DisplayName("ofNotExist 메서드로 빈 유저 객체를 생성 할 수 있다.")
    void ofNotExistTest() {
        // given
        // when
        final var account = Account.ofNotExist();

        // then
        assertThat(account.getId()).isNull();
        assertThat(account.getPassword()).isNull();
        assertThat(account.getNickname()).isNull();
        assertThat(account.getStatus()).isNull();
        assertThat(account.getRole()).isNull();
        assertThat(account.getSignUpDateTime()).isNull();
        assertThat(account.getSignInDateTime()).isNull();
    }

    @Test
    @DisplayName("getStatus 메서드는 CREATED 상태 일 떄 LIVED 상태를 반환한다.")
    void getStatusTest() {
        // given
        final var account = Account.of(
            "id",
            "password",
            "nickname",
            "CREATED",
            "ROLE_USER",
            LocalDateTime.of(2024, 3, 26, 0, 0, 0),
            LocalDateTime.of(2024, 3, 26, 0, 0, 0)
        );

        // when
        final var status = account.getStatus();

        // then
        assertThat(status).isEqualTo(AccountStatus.LIVED);
    }

    @Test
    @DisplayName("exist 메서드는 LIVED 상태이거나 LEFT 상태 일 때 true 를 반환한다.")
    void existTest() {
        // given
        final var createdAccount = Account.of(
            "id",
            "password",
            "nickname",
            "CREATED",
            "ROLE_USER",
            LocalDateTime.of(2024, 3, 26, 0, 0, 0),
            LocalDateTime.of(2024, 3, 26, 0, 0, 0)
        );

        final var livedAccount = Account.of(
            "id",
            "password",
            "nickname",
            "LIVED",
            "ROLE_USER",
            LocalDateTime.of(2024, 3, 26, 0, 0, 0),
            LocalDateTime.of(2024, 3, 26, 0, 0, 0)
        );

        // when
        final var createdExist = createdAccount.exist();
        final var livedExist = livedAccount.exist();

        // then
        assertThat(createdExist).isFalse();
        assertThat(livedExist).isTrue();
    }

    @Test
    @DisplayName("login 메서드 실행 시 signInDateTime 이 채워진다.")
    void loginTest() {
        // given
        final var createdAccount = Account.of(
            "id",
            "password",
            "nickname",
            "CREATED",
            "ROLE_USER",
            LocalDateTime.of(2024, 3, 26, 0, 0, 0),
            null
        );
        DateTimeHolder dateTimeHolder = new MockDateTimeHolder(LocalDateTime.of(2024, 3, 26, 0, 0, 0));
        // when
        final var loginAccount = createdAccount.login(dateTimeHolder);

        // then
        assertThat(loginAccount.getSignInDateTime()).isEqualTo(LocalDateTime.of(2024, 3, 26, 0, 0, 0));
    }
}
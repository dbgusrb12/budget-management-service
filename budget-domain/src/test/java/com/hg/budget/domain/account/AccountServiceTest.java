package com.hg.budget.domain.account;

import static org.assertj.core.api.Assertions.assertThat;

import com.hg.budget.domain.account.port.AccountRepository;
import com.hg.budget.domain.mock.MockAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AccountServiceTest {

    AccountService accountService;
    AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        accountRepository = new MockAccountRepository();
        accountService = new AccountService(accountRepository);
    }

    @Test
    @DisplayName("createAccount 로 유저를 생성 할 수 있다.")
    void createAccountTest() {
        // given
        // when
        final var account = accountService.createAccount("id", "password", "nickname");

        // then
        assertThat(account.getId()).isEqualTo("id");
        assertThat(account.getPassword()).isEqualTo("password");
        assertThat(account.getNickname()).isEqualTo("nickname");
        assertThat(account.getStatus()).isEqualTo(AccountStatus.LIVED);
        assertThat(account.isDuplicated()).isFalse();
    }

    @Test
    @DisplayName("createAccount 실행 시 유저가 이미 존재한다면 존재하는 유저를 반환한다.")
    void createAccountTest_already_exist() {
        // given
        final var account = accountService.createAccount("id", "password", "nickname");

        // when
        final var duplecatedAccount = accountService.createAccount("id", "password", "nickname");

        // then
        assertThat(duplecatedAccount.exist()).isTrue();
        assertThat(duplecatedAccount).isEqualTo(account);
    }

    @Test
    @DisplayName("findAccount 로 유저를 조회 할 수 있다.")
    void findAccountTest() {
        // given
        accountService.createAccount("id", "password", "nickname");

        // when
        final var account = accountService.findAccount("id");

        // then
        assertThat(account.getId()).isEqualTo("id");
        assertThat(account.getPassword()).isEqualTo("password");
        assertThat(account.getNickname()).isEqualTo("nickname");
        assertThat(account.getStatus()).isEqualTo(AccountStatus.LIVED);
    }

    @Test
    @DisplayName("findAccount 실행 시 유저가 존재하지 않는다면 빈 객체를 반환한다.")
    void findAccountTest_notExist() {
        // given
        // when
        final var account = accountService.findAccount("id");

        // then
        assertThat(account.notExist()).isTrue();
        assertThat(account.getId()).isNull();
    }
}
package com.hg.budget.domain.account;

import static org.assertj.core.api.Assertions.assertThat;

import com.hg.budget.domain.account.port.AccountRepository;
import com.hg.budget.domain.mock.MockAccountRepository;
import com.hg.budget.domain.mock.MockDateTimeHolder;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AccountServiceTest {

    AccountService accountService;
    AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        accountRepository = new MockAccountRepository();
        final var dateTimeHolder = new MockDateTimeHolder(LocalDateTime.of(2024, 7, 12, 0, 0, 0));
        accountService = new AccountService(dateTimeHolder, accountRepository);
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
        assertThat(account.getRole()).isEqualTo(AccountRole.USER);
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
        assertThat(account.getRole()).isEqualTo(AccountRole.USER);
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

    @Test
    @DisplayName("로그인 시 로그인 시간이 현재 시간으로 채워진다.")
    void loginTest() {
        // given
        accountService.createAccount("id", "password", "nickname");

        // when
        final var login = accountService.login("id");

        // then
        assertThat(login.getSignInDateTime()).isEqualTo(LocalDateTime.of(2024, 7, 12, 0, 0, 0));
    }

    @Test
    @DisplayName("로그인 시 유저가 존재하지 않는다면 빈 객체를 반환한다.")
    void loginTest_notExist() {
        // given
        // when
        final var login = accountService.login("id");

        // then
        assertThat(login.notExist()).isTrue();
        assertThat(login.getId()).isNull();
    }

    @Test
    @DisplayName("findAccounts 실행 시 모든 유저의 목록을 반환한다.")
    void findAccountsTest() {
        // given
        accountService.createAccount("id1", "password", "nickname1");
        accountService.createAccount("id2", "password", "nickname2");

        // when
        List<Account> accounts = accountService.findAccounts();

        // then
        assertThat(accounts.size()).isEqualTo(2);
        assertThat(accounts.get(0).getId()).isEqualTo("id1");
        assertThat(accounts.get(0).getNickname()).isEqualTo("nickname1");
        assertThat(accounts.get(1).getId()).isEqualTo("id2");
        assertThat(accounts.get(1).getNickname()).isEqualTo("nickname2");
    }
}
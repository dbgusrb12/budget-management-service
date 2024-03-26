package com.hg.budget.domain.persistence.account;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AccountEntityRepositoryTest {

    @Autowired
    private AccountEntityRepository accountEntityRepository;

    @BeforeEach
    void setUp() {
        accountEntityRepository.deleteAll();
    }

    @Test
    void findByAccountIdTest() {
        // given
        final var accountEntity = AccountEntity.of(
            "accountId",
            "password",
            "nickname",
            "LIVED",
            "ROLE_USER",
            LocalDateTime.of(2024, 3, 26, 0, 0, 0),
            LocalDateTime.of(2024, 3, 26, 0, 0, 0)
        );
        accountEntityRepository.save(accountEntity);

        // when
        final var account = accountEntityRepository.findByAccountId("accountId");

        // then
        assertThat(account.getAccountId()).isEqualTo("accountId");
        assertThat(account.getPassword()).isEqualTo("password");
        assertThat(account.getNickname()).isEqualTo("nickname");
        assertThat(account.getStatus()).isEqualTo("LIVED");
        assertThat(account.getRole()).isEqualTo("ROLE_USER");
        assertThat(account.getSignUpDateTime()).isEqualTo(LocalDateTime.of(2024, 3, 26, 0, 0, 0));
        assertThat(account.getSignInDateTime()).isEqualTo(LocalDateTime.of(2024, 3, 26, 0, 0, 0));
    }
}
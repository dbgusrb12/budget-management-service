package com.hg.budget.application.spend;

import static org.assertj.core.api.Assertions.assertThat;

import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.persistence.account.AccountEntity;
import com.hg.budget.domain.persistence.account.AccountEntityRepository;
import com.hg.budget.domain.persistence.category.CategoryEntity;
import com.hg.budget.domain.persistence.category.CategoryEntityRepository;
import com.hg.budget.domain.persistence.spend.SpendEntityRepository;
import com.hg.budget.domain.spend.Spend;
import com.hg.budget.domain.spend.SpendService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpendCommandServiceTest {

    @Autowired
    SpendCommandService spendCommandService;
    SpendCommandServiceTestHelper testHelper;

    @Autowired
    SpendEntityRepository spendEntityRepository;
    @Autowired
    AccountEntityRepository accountEntityRepository;
    @Autowired
    CategoryEntityRepository categoryEntityRepository;
    @Autowired
    SpendService spendService;

    @BeforeEach
    void setUp() {
        spendEntityRepository.deleteAllInBatch();
        accountEntityRepository.deleteAllInBatch();
        categoryEntityRepository.deleteAllInBatch();
        testHelper = new SpendCommandServiceTestHelper(accountEntityRepository, categoryEntityRepository);
    }

    @Test
    @DisplayName("createSpend 로 지출을 등록 할 수 있다.")
    void createSpendTest() {
        // given
        testHelper.createAccount("hg-yu", "hyungyu");
        testHelper.createCategory("식비");

        // when
        final var spend = spendCommandService.createSpend(
            1000L,
            "메모",
            1L,
            LocalDateTime.of(2024, 7, 12, 0, 0, 0),
            "hg-yu"
        );

        // then
        assertThat(spend.amount()).isEqualTo(1000L);
        assertThat(spend.memo()).isEqualTo("메모");
        assertThat(spend.category().name()).isEqualTo("식비");
        assertThat(spend.spentDateTime()).isEqualTo("2024-07-12T00:00");
        assertThat(spend.spentUser().nickname()).isEqualTo("hyungyu");
        assertThat(spend.excludeTotal()).isFalse();
    }

    @Test
    @DisplayName("update 로 지출을 수정 할 수 있다.")
    void updateTest() {
        // given
        testHelper.createAccount("hg-yu", "hyungyu");
        testHelper.createCategory("식비");
        final var saved = spendCommandService.createSpend(
            1000L,
            "메모",
            1L,
            LocalDateTime.of(2024, 7, 12, 0, 0, 0),
            "hg-yu"
        );

        // when
        final var updated = spendCommandService.update(
            saved.id(),
            2000L,
            "수정된메모",
            1L,
            LocalDateTime.of(2025, 7, 12, 0, 0, 0),
            "hg-yu"
        );

        // then
        assertThat(updated.amount()).isEqualTo(2000L);
        assertThat(updated.memo()).isEqualTo("수정된메모");
        assertThat(updated.category().name()).isEqualTo("식비");
        assertThat(updated.spentDateTime()).isEqualTo("2025-07-12T00:00");
        assertThat(updated.spentUser().nickname()).isEqualTo("hyungyu");
        assertThat(updated.excludeTotal()).isFalse();
    }

    @Test
    @DisplayName("delete 로 지출을 삭제 할 수 있다.")
    void deleteTest() {
        // given
        final var account = testHelper.createAccount("hg-yu", "hyungyu");
        testHelper.createCategory("식비");
        final var saved = spendCommandService.createSpend(
            1000L,
            "메모",
            1L,
            LocalDateTime.of(2024, 7, 12, 0, 0, 0),
            "hg-yu"
        );

        // when
        spendCommandService.delete(saved.id(), "hg-yu");

        // then
        List<Spend> spendList = spendService.findSpendList(account);
        assertThat(spendList.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("updateExcludeTotal 로 지출의 합계 제외 여부를 수정 할 수 있다.")
    void updateExcludeTotalTest() {
        // given
        testHelper.createAccount("hg-yu", "hyungyu");
        testHelper.createCategory("식비");
        final var saved = spendCommandService.createSpend(
            1000L,
            "메모",
            1L,
            LocalDateTime.of(2024, 7, 12, 0, 0, 0),
            "hg-yu"
        );

        // when
        final var updated = spendCommandService.updateExcludeTotal(saved.id(), true, "hg-yu");

        // then
        assertThat(updated.excludeTotal()).isTrue();
    }

    static class SpendCommandServiceTestHelper {

        private final AccountEntityRepository accountEntityRepository;
        private final CategoryEntityRepository categoryEntityRepository;
        private long categoryGeneratedId = 1L;
        private long accountGeneratedId = 1L;

        SpendCommandServiceTestHelper(AccountEntityRepository accountEntityRepository, CategoryEntityRepository categoryEntityRepository) {
            this.accountEntityRepository = accountEntityRepository;
            this.categoryEntityRepository = categoryEntityRepository;
        }

        void createCategory(String name) {
            final var entity = CategoryEntity.of(categoryGeneratedId++, name);
            categoryEntityRepository.save(entity);
        }

        Account createAccount(String id, String nickname) {
            final var entity = AccountEntity.ofUpdate(
                accountGeneratedId++,
                id,
                "PASSWORD",
                nickname,
                "LIVED",
                "ROLE_USER",
                LocalDateTime.of(2024, 7, 12, 0, 0, 0),
                LocalDateTime.of(2024, 7, 12, 0, 0, 0)
            );
            accountEntityRepository.save(entity);
            return Account.of(
                entity.getAccountId(),
                entity.getPassword(),
                entity.getNickname(),
                entity.getStatus(),
                entity.getRole(),
                entity.getSignUpDateTime(),
                entity.getSignUpDateTime()
            );
        }
    }
}
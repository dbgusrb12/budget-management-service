package com.hg.budget.domain.persistence.budget;

import static org.assertj.core.api.Assertions.assertThat;

import com.hg.budget.core.client.IdGenerator;
import com.hg.budget.domain.persistence.account.AccountEntity;
import com.hg.budget.domain.persistence.account.AccountEntityRepository;
import com.hg.budget.domain.persistence.category.CategoryEntity;
import com.hg.budget.domain.persistence.category.CategoryEntityRepository;
import com.hg.budget.domain.persistence.mock.MockIdGenerator;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class BudgetEntityRepositoryTest {

    @Autowired
    BudgetEntityRepository budgetEntityRepository;
    @Autowired
    AccountEntityRepository accountEntityRepository;
    @Autowired
    CategoryEntityRepository categoryEntityRepository;

    @Test
    void findByCategory_IdAndCreatedUser_AccountIdTest() {
        // given
        final var testHelper = new BudgetEntityRepositoryTestHelper(accountEntityRepository, categoryEntityRepository);
        final var accountEntity = testHelper.createAccount("hg-yu", "hyungyu");
        final var categoryEntity = testHelper.createCategory("식비");

        final var budgetEntity = BudgetEntity.of(
            1L,
            categoryEntity,
            1000L,
            accountEntity,
            LocalDateTime.of(2024, 7, 12, 0, 0, 0),
            LocalDateTime.of(2024, 7, 12, 0, 0, 0)
        );
        budgetEntityRepository.save(budgetEntity);

        // when
        final var budget = budgetEntityRepository.findByCategory_IdAndCreatedUser_AccountId(1L, "hg-yu");

        // then
        assertThat(budget.getId()).isEqualTo(1L);
        assertThat(budget.getAmount()).isEqualTo(1000);
        assertThat(budget.getCategory().getName()).isEqualTo("식비");
        assertThat(budget.getCreatedUser().getAccountId()).isEqualTo("hg-yu");
        assertThat(budget.getCreatedUser().getNickname()).isEqualTo("hyungyu");
    }

    @Test
    void findByCreatedUser_AccountIdTest() {
        // given
        final var testHelper = new BudgetEntityRepositoryTestHelper(accountEntityRepository, categoryEntityRepository);
        final var accountEntity = testHelper.createAccount("hg-yu", "hyungyu");
        final var categoryEntity = testHelper.createCategory("식비");

        final var budgetEntity = BudgetEntity.of(
            1L,
            categoryEntity,
            1000L,
            accountEntity,
            LocalDateTime.of(2024, 7, 12, 0, 0, 0),
            LocalDateTime.of(2024, 7, 12, 0, 0, 0)
        );
        budgetEntityRepository.save(budgetEntity);

        // when
        final var budget = budgetEntityRepository.findByCreatedUser_AccountId("hg-yu");

        // then
        assertThat(budget.size()).isEqualTo(1);
        assertThat(budget.get(0).getId()).isEqualTo(1L);
        assertThat(budget.get(0).getAmount()).isEqualTo(1000);
        assertThat(budget.get(0).getCategory().getName()).isEqualTo("식비");
        assertThat(budget.get(0).getCreatedUser().getAccountId()).isEqualTo("hg-yu");
        assertThat(budget.get(0).getCreatedUser().getNickname()).isEqualTo("hyungyu");
    }

    static class BudgetEntityRepositoryTestHelper {

        private final AccountEntityRepository accountEntityRepository;
        private final CategoryEntityRepository categoryEntityRepository;

        private long categoryGeneratedId = 1L;
        private long accountGeneratedId = 1L;

        BudgetEntityRepositoryTestHelper(AccountEntityRepository accountEntityRepository, CategoryEntityRepository categoryEntityRepository) {
            this.accountEntityRepository = accountEntityRepository;
            this.categoryEntityRepository = categoryEntityRepository;
        }

        CategoryEntity createCategory(String name) {
            return categoryEntityRepository.save(CategoryEntity.of(categoryGeneratedId++, name));
        }

        AccountEntity createAccount(String id, String nickname) {
            IdGenerator idGenerator = new MockIdGenerator(accountGeneratedId++);
            AccountEntity entity = AccountEntity.of(
                idGenerator,
                id,
                "PASSWORD",
                nickname,
                "LIVED",
                "ROLE_USER",
                LocalDateTime.of(2024, 7, 12, 0, 0, 0),
                LocalDateTime.of(2024, 7, 12, 0, 0, 0)
            );
            return accountEntityRepository.save(entity);
        }
    }
}
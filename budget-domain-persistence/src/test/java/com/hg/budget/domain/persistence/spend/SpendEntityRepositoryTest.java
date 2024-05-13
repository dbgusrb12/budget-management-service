package com.hg.budget.domain.persistence.spend;

import static org.assertj.core.api.Assertions.assertThat;

import com.hg.budget.core.client.IdGenerator;
import com.hg.budget.domain.persistence.account.AccountEntity;
import com.hg.budget.domain.persistence.account.AccountEntityRepository;
import com.hg.budget.domain.persistence.category.CategoryEntity;
import com.hg.budget.domain.persistence.category.CategoryEntityRepository;
import com.hg.budget.domain.persistence.mock.MockIdGenerator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class SpendEntityRepositoryTest {

    @Autowired
    SpendEntityRepository spendEntityRepository;
    @Autowired
    AccountEntityRepository accountEntityRepository;
    @Autowired
    CategoryEntityRepository categoryEntityRepository;

    @Test
    void findAllTest() {
        // given
        final var testHelper = new SpendEntityRepositoryTestHelper(accountEntityRepository, categoryEntityRepository);
        final var accountEntity = testHelper.createAccount("hg-yu", "hyungyu");
        final var categoryEntity = testHelper.createCategory("식비");
        final var spend = SpendEntity.of(
            1L,
            categoryEntity,
            1000L,
            "메모",
            accountEntity,
            LocalDateTime.of(2024, 7, 12, 0, 0, 0),
            false
        );
        spendEntityRepository.save(spend);

        // when
        final var spendList = spendEntityRepository.findAll(
            1,
            5,
            LocalDateTime.of(2024, 7, 11, 0, 0, 0),
            LocalDateTime.of(2024, 7, 13, 0, 0, 0),
            accountEntity,
            null,
            null,
            null
        );

        // then
        assertThat(spendList.getTotalElements()).isEqualTo(1L);
        final var spendEntity = spendList.getContent().get(0);
        assertThat(spendEntity.getId()).isEqualTo(1L);
        assertThat(spendEntity.getAmount()).isEqualTo(1000L);
        assertThat(spendEntity.getMemo()).isEqualTo("메모");
        assertThat(spendEntity.getCategory().getName()).isEqualTo("식비");
        assertThat(spendEntity.getSpentUser().getAccountId()).isEqualTo("hg-yu");
        assertThat(spendEntity.getSpentUser().getNickname()).isEqualTo("hyungyu");
        assertThat(spendEntity.getSpentDateTime()).isEqualTo(LocalDateTime.of(2024, 7, 12, 0, 0, 0));
        assertThat(spendEntity.getExcludeTotal()).isFalse();
    }

    @Test
    void findBySpentUser_AccountId() {
        // given
        final var testHelper = new SpendEntityRepositoryTestHelper(accountEntityRepository, categoryEntityRepository);
        final var accountEntity = testHelper.createAccount("hg-yu", "hyungyu");
        final var categoryEntity = testHelper.createCategory("식비");
        final var spend = SpendEntity.of(
            1L,
            categoryEntity,
            1000L,
            "메모",
            accountEntity,
            LocalDateTime.of(2024, 7, 12, 0, 0, 0),
            false
        );
        spendEntityRepository.save(spend);

        // when
        final var spendList = spendEntityRepository.findBySpentUser_AccountId("hg-yu");

        // then
        assertThat(spendList.size()).isEqualTo(1);
        final var spendEntity = spendList.get(0);
        assertThat(spendEntity.getId()).isEqualTo(1L);
        assertThat(spendEntity.getAmount()).isEqualTo(1000L);
        assertThat(spendEntity.getMemo()).isEqualTo("메모");
        assertThat(spendEntity.getCategory().getName()).isEqualTo("식비");
        assertThat(spendEntity.getSpentUser().getAccountId()).isEqualTo("hg-yu");
        assertThat(spendEntity.getSpentUser().getNickname()).isEqualTo("hyungyu");
        assertThat(spendEntity.getSpentDateTime()).isEqualTo(LocalDateTime.of(2024, 7, 12, 0, 0, 0));
        assertThat(spendEntity.getExcludeTotal()).isFalse();
    }

    @Test
    void findBySpentDateTimeBetween() {
        // given
        final var testHelper = new SpendEntityRepositoryTestHelper(accountEntityRepository, categoryEntityRepository);
        final var accountEntity = testHelper.createAccount("hg-yu", "hyungyu");
        final var accountEntity2 = testHelper.createAccount("hg-yu2", "hyungyu2");
        final var categoryEntity = testHelper.createCategory("식비");
        final var spend = SpendEntity.of(
            1L,
            categoryEntity,
            1000L,
            "메모",
            accountEntity,
            LocalDateTime.of(2024, 7, 12, 0, 0, 0),
            false
        );
        spendEntityRepository.save(spend);
        final var spend2 = SpendEntity.of(
            2L,
            categoryEntity,
            2000L,
            "메모2",
            accountEntity2,
            LocalDateTime.of(2024, 7, 12, 0, 0, 0),
            false
        );
        spendEntityRepository.save(spend2);

        // when
        final LocalDate today = LocalDate.of(2024, 7, 12);
        final LocalDateTime start = today.atStartOfDay();
        final LocalDateTime end = today.atTime(LocalTime.MAX);
        final var spendList = spendEntityRepository.findBySpentDateTimeBetween(start, end);

        // then
        assertThat(spendList.size()).isEqualTo(2);
        final var spendEntity = spendList.get(0);
        assertThat(spendEntity.getId()).isEqualTo(1L);
        assertThat(spendEntity.getAmount()).isEqualTo(1000L);
        assertThat(spendEntity.getMemo()).isEqualTo("메모");
        assertThat(spendEntity.getCategory().getName()).isEqualTo("식비");
        assertThat(spendEntity.getSpentUser().getAccountId()).isEqualTo("hg-yu");
        assertThat(spendEntity.getSpentUser().getNickname()).isEqualTo("hyungyu");
        assertThat(spendEntity.getSpentDateTime()).isEqualTo(LocalDateTime.of(2024, 7, 12, 0, 0, 0));
        assertThat(spendEntity.getExcludeTotal()).isFalse();

        final var spendEntity2 = spendList.get(1);
        assertThat(spendEntity2.getId()).isEqualTo(2L);
        assertThat(spendEntity2.getAmount()).isEqualTo(2000L);
        assertThat(spendEntity2.getMemo()).isEqualTo("메모2");
        assertThat(spendEntity2.getCategory().getName()).isEqualTo("식비");
        assertThat(spendEntity2.getSpentUser().getAccountId()).isEqualTo("hg-yu2");
        assertThat(spendEntity2.getSpentUser().getNickname()).isEqualTo("hyungyu2");
        assertThat(spendEntity2.getSpentDateTime()).isEqualTo(LocalDateTime.of(2024, 7, 12, 0, 0, 0));
        assertThat(spendEntity2.getExcludeTotal()).isFalse();
    }


    static class SpendEntityRepositoryTestHelper {

        private final AccountEntityRepository accountEntityRepository;
        private final CategoryEntityRepository categoryEntityRepository;

        private long categoryGeneratedId = 1L;
        private long accountGeneratedId = 1L;

        SpendEntityRepositoryTestHelper(AccountEntityRepository accountEntityRepository, CategoryEntityRepository categoryEntityRepository) {
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
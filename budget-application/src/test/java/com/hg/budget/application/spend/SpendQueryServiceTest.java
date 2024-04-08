package com.hg.budget.application.spend;

import static org.assertj.core.api.Assertions.assertThat;

import com.hg.budget.domain.persistence.account.AccountEntity;
import com.hg.budget.domain.persistence.account.AccountEntityRepository;
import com.hg.budget.domain.persistence.category.CategoryEntity;
import com.hg.budget.domain.persistence.category.CategoryEntityRepository;
import com.hg.budget.domain.persistence.spend.SpendEntity;
import com.hg.budget.domain.persistence.spend.SpendEntityRepository;
import com.hg.budget.domain.spend.SpendService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class SpendQueryServiceTest {

    @Autowired
    SpendQueryService spendQueryService;
    SpendQueryServiceTestHelper testHelper;

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
        testHelper = new SpendQueryServiceTestHelper(accountEntityRepository, categoryEntityRepository, spendEntityRepository);
    }

    @Test
    @DisplayName("getSpend 로 지출 상세 내역을 조회 할 수 있다.")
    void getSpendTest() {
        // given
        final var account = testHelper.createAccount("hg-yu", "hyungyu");
        final var category = testHelper.createCategory("식비");
        testHelper.createSpend(1000L, "메모", LocalDateTime.of(2024, 7, 12, 0, 0, 0), category, account);

        // when
        final var spend = spendQueryService.getSpend(1L, "hg-yu");

        // then
        assertThat(spend.getId()).isEqualTo(1L);
        assertThat(spend.getAmount()).isEqualTo(1000L);
        assertThat(spend.getMemo()).isEqualTo("메모");
        assertThat(spend.getCategory().getName()).isEqualTo("식비");
        assertThat(spend.getSpentDateTime()).isEqualTo("2024-07-12T00:00");
        assertThat(spend.getSpentUser().getNickname()).isEqualTo("hyungyu");
        assertThat(spend.isExcludeTotal()).isFalse();
    }

    @Test
    @DisplayName("pageSpend 로 지출 목록을 페이징해서 조회 할 수 있다.")
    void pageSpendTest() {
        // given
        final var account = testHelper.createAccount("hg-yu", "hyungyu");
        final var category = testHelper.createCategory("식비");
        testHelper.createSpend(1000L, "메모", LocalDateTime.of(2024, 7, 12, 0, 0, 0), category, account);

        // when
        final var spend = spendQueryService.pageSpend(
            1,
            5,
            LocalDateTime.of(2024, 7, 12, 0, 0, 0),
            LocalDateTime.of(2024, 7, 12, 0, 0, 0),
            null,
            null,
            null,
            "hg-yu"
        );

        // then
        final var contents = spend.getPage().getContent();
        assertThat(spend.getTotalAmount()).isEqualTo(1000);
        assertThat(spend.getTotalAmountByCategories().get(0).category().getName()).isEqualTo("식비");
        assertThat(spend.getTotalAmountByCategories().get(0).totalAmount()).isEqualTo(1000L);
        assertThat(contents.get(0).getId()).isEqualTo(1L);
        assertThat(contents.get(0).getAmount()).isEqualTo(1000L);
        assertThat(contents.get(0).getMemo()).isEqualTo("메모");
        assertThat(contents.get(0).getCategory().getName()).isEqualTo("식비");
        assertThat(contents.get(0).getSpentDateTime()).isEqualTo("2024-07-12T00:00");
        assertThat(contents.get(0).getSpentUser().getNickname()).isEqualTo("hyungyu");
        assertThat(contents.get(0).isExcludeTotal()).isFalse();
    }

    static class SpendQueryServiceTestHelper {

        private final AccountEntityRepository accountEntityRepository;
        private final CategoryEntityRepository categoryEntityRepository;
        private final SpendEntityRepository spendEntityRepository;
        private long categoryGeneratedId = 1L;
        private long accountGeneratedId = 1L;
        private long spendGeneratedId = 1L;

        SpendQueryServiceTestHelper(
            AccountEntityRepository accountEntityRepository,
            CategoryEntityRepository categoryEntityRepository,
            SpendEntityRepository spendEntityRepository
        ) {
            this.accountEntityRepository = accountEntityRepository;
            this.categoryEntityRepository = categoryEntityRepository;
            this.spendEntityRepository = spendEntityRepository;
        }

        CategoryEntity createCategory(String name) {
            final var entity = CategoryEntity.of(categoryGeneratedId++, name);
            return categoryEntityRepository.save(entity);
        }

        AccountEntity createAccount(String id, String nickname) {
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
            return accountEntityRepository.save(entity);
        }

        void createSpend(long amount, String memo, LocalDateTime spentDateTime, CategoryEntity category, AccountEntity account) {
            final var entity = SpendEntity.of(
                spendGeneratedId++,
                category,
                amount,
                memo,
                account,
                spentDateTime,
                false
            );

            spendEntityRepository.save(entity);
        }
    }
}
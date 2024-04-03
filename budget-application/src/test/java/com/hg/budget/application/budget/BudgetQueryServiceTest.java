package com.hg.budget.application.budget;

import com.hg.budget.application.budget.dto.RecommendBudgetDto;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.budget.Budget;
import com.hg.budget.domain.budget.BudgetService;
import com.hg.budget.domain.category.Category;
import com.hg.budget.domain.persistence.account.AccountEntity;
import com.hg.budget.domain.persistence.account.AccountEntityRepository;
import com.hg.budget.domain.persistence.budget.BudgetEntity;
import com.hg.budget.domain.persistence.budget.BudgetEntityRepository;
import com.hg.budget.domain.persistence.category.CategoryEntity;
import com.hg.budget.domain.persistence.category.CategoryEntityRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BudgetQueryServiceTest {

    @Autowired
    BudgetQueryService budgetQueryService;
    BudgetQueryServiceTestHelper testHelper;

    @Autowired
    BudgetEntityRepository budgetEntityRepository;
    @Autowired
    AccountEntityRepository accountEntityRepository;
    @Autowired
    CategoryEntityRepository categoryEntityRepository;
    @Autowired
    BudgetService budgetService;

    @BeforeEach
    void setUp() {
        budgetEntityRepository.deleteAllInBatch();
        accountEntityRepository.deleteAllInBatch();
        categoryEntityRepository.deleteAllInBatch();
        testHelper = new BudgetQueryServiceTestHelper(accountEntityRepository, categoryEntityRepository, budgetEntityRepository);
    }

    @Test
    void recommendBudget() {
        // given
        final var hgyu = testHelper.createAccount("hg-yu", "hyungyu");
        final var hgyu2 = testHelper.createAccount("hg-yu2", "hyungyu2");
        final var hgyu3 = testHelper.createAccount("hg-yu3", "hyungyu3");
        final var 식비 = testHelper.createCategory("식비");
        final var 교통 = testHelper.createCategory("교통");
        final var 주거 = testHelper.createCategory("주거");
        final var 문화여가 = testHelper.createCategory("문화/여가");
        final var 카페 = testHelper.createCategory("카페");
        final var 여행 = testHelper.createCategory("여행");
        final var 교육 = testHelper.createCategory("교육");
        final var 기타 = testHelper.createCategory("기타");

        final var budgets = List.of(
            testHelper.createBudget(식비, 10, hgyu),
            testHelper.createBudget(교통, 20, hgyu),
            testHelper.createBudget(주거, 30, hgyu),
            testHelper.createBudget(문화여가, 10, hgyu),
            testHelper.createBudget(카페, 5, hgyu),
            testHelper.createBudget(여행, 10, hgyu),
            testHelper.createBudget(교육, 10, hgyu),
            testHelper.createBudget(기타, 5, hgyu),

            testHelper.createBudget(식비, 5, hgyu2),
            testHelper.createBudget(교통, 5, hgyu2),
            testHelper.createBudget(주거, 10, hgyu2),
            testHelper.createBudget(문화여가, 10, hgyu2),
            testHelper.createBudget(카페, 30, hgyu2),
            testHelper.createBudget(여행, 10, hgyu2),
            testHelper.createBudget(교육, 10, hgyu2),
            testHelper.createBudget(기타, 20, hgyu2)

//            testHelper.createBudget(식비, 30, hgyu3),
//            testHelper.createBudget(교통, 10, hgyu3),
//            testHelper.createBudget(주거, 10, hgyu3),
//            testHelper.createBudget(문화여가, 10, hgyu3),
//            testHelper.createBudget(카페, 10, hgyu3),
//            testHelper.createBudget(여행, 10, hgyu3),
//            testHelper.createBudget(교육, 10, hgyu3),
//            testHelper.createBudget(기타, 10, hgyu3)
        );

        // when
        List<RecommendBudgetDto> recommend = budgetQueryService.recommend(100);

        recommend.forEach(System.out::println);
    }

    static class BudgetQueryServiceTestHelper {

        private final AccountEntityRepository accountEntityRepository;
        private final CategoryEntityRepository categoryEntityRepository;
        private final BudgetEntityRepository budgetEntityRepository;
        private long categoryGeneratedId = 1L;
        private long accountGeneratedId = 1L;
        private long budgetGeneratedId = 1L;

        BudgetQueryServiceTestHelper(AccountEntityRepository accountEntityRepository, CategoryEntityRepository categoryEntityRepository,
            BudgetEntityRepository budgetEntityRepository) {
            this.accountEntityRepository = accountEntityRepository;
            this.categoryEntityRepository = categoryEntityRepository;
            this.budgetEntityRepository = budgetEntityRepository;
        }

        Budget createBudget(CategoryEntity category, long amount, AccountEntity account) {
            final var entity = BudgetEntity.of(
                budgetGeneratedId++,
                category,
                amount,
                account,
                LocalDateTime.of(2024, 7, 12, 0, 0, 0),
                LocalDateTime.of(2024, 7, 12, 0, 0, 0)
            );
            budgetEntityRepository.save(entity);
            return Budget.of(
                entity.getId(),
                Category.of(category.getId(), category.getName()),
                entity.getAmount(),
                Account.of(
                    account.getAccountId(),
                    account.getPassword(),
                    account.getNickname(),
                    account.getStatus(),
                    account.getRole(),
                    account.getSignUpDateTime(),
                    account.getSignUpDateTime()
                ),
                entity.getCreatedDateTime(),
                entity.getUpdatedDateTime()
            );
        }

        CategoryEntity createCategory(String name) {
            final var entity = CategoryEntity.of(categoryGeneratedId++, name);
            return categoryEntityRepository.save(entity);
//            return Category.of(entity.getId(), entity.getName());
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
//            return Account.of(
//                entity.getAccountId(),
//                entity.getPassword(),
//                entity.getNickname(),
//                entity.getStatus(),
//                entity.getRole(),
//                entity.getSignUpDateTime(),
//                entity.getSignUpDateTime()
//            );
        }
    }
}
package com.hg.budget.application.budget;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import com.hg.budget.application.budget.dto.CreateBudget;
import com.hg.budget.application.core.code.ApplicationCode;
import com.hg.budget.application.core.exception.ApplicationException;
import com.hg.budget.domain.budget.Budget;
import com.hg.budget.domain.budget.BudgetService;
import com.hg.budget.domain.persistence.account.AccountEntity;
import com.hg.budget.domain.persistence.account.AccountEntityRepository;
import com.hg.budget.domain.persistence.budget.BudgetEntityRepository;
import com.hg.budget.domain.persistence.category.CategoryEntity;
import com.hg.budget.domain.persistence.category.CategoryEntityRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BudgetCommandServiceTest {

    @Autowired
    BudgetCommandService budgetCommandService;
    BudgetCommandServiceTestHelper testHelper;

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
        testHelper = new BudgetCommandServiceTestHelper(accountEntityRepository, categoryEntityRepository);
    }

    @Test
    @DisplayName("createBudgets 로 특정 유저의 예산 리스트를 등록 할 수 있다.")
    void createBudgetsTest() {
        // given
        testHelper.createAccount("hg-yu", "hyungyu");
        testHelper.createCategory("식비");
        final var createBudgets = List.of(new CreateBudget(1L, 1000L));

        // when
        budgetCommandService.createBudgets(createBudgets, "hg-yu");

        // then
        Budget saved = budgetService.findBudgets().get(0);
        assertThat(saved.getAmount()).isEqualTo(1000);
        assertThat(saved.getCategory().getName()).isEqualTo("식비");
        assertThat(saved.getCreatedUser().getNickname()).isEqualTo("hyungyu");
    }

    @Test
    @DisplayName("예산 등록 시 유저가 존재하지 않으면 에러가 발생한다.")
    void createBudgetsTest_NotExistUser() {
        // given
        testHelper.createCategory("식비");
        final var createBudgets = List.of(new CreateBudget(1L, 1000L));

        // when
        ApplicationException applicationException = catchThrowableOfType(
            () -> budgetCommandService.createBudgets(createBudgets, "hg-yu"),
            ApplicationException.class
        );

        // then
        assertThat(applicationException.getApplicationCode()).isEqualTo(ApplicationCode.BAD_REQUEST);
        assertThat(applicationException.getLogMessage()).isEqualTo("유저가 존재하지 않습니다.");
        assertThat(applicationException.getMessage()).isEqualTo("잘못된 요청입니다.");
    }

    @Test
    @DisplayName("예산 등록 시 카테고리가 존재하지 않으면 에러가 발생한다.")
    void createBudgetsTest_NotExistCategory() {
        // given
        testHelper.createAccount("hg-yu", "hyungyu");
        final var createBudgets = List.of(new CreateBudget(1L, 1000L));

        // when
        ApplicationException applicationException = catchThrowableOfType(
            () -> budgetCommandService.createBudgets(createBudgets, "hg-yu"),
            ApplicationException.class
        );

        // then
        assertThat(applicationException.getApplicationCode()).isEqualTo(ApplicationCode.BAD_REQUEST);
        assertThat(applicationException.getLogMessage()).isEqualTo("카테고리가 존재하지 않습니다.");
        assertThat(applicationException.getMessage()).isEqualTo("잘못된 요청입니다.");
    }

    @Test
    @DisplayName("updateAmount 로 특정 유저의 예산을 수정 할 수 있다.")
    void updateAmountTest() {
        // given
        testHelper.createAccount("hg-yu", "hyungyu");
        testHelper.createCategory("식비");
        budgetCommandService.createBudgets(List.of(new CreateBudget(1L, 1000L)), "hg-yu");
        Budget budget = budgetService.findBudgets().get(0);

        // when
        budgetCommandService.updateAmount(budget.getId(), 2000L, "hg-yu");

        // then
        Budget updated = budgetService.findBudgets().get(0);
        assertThat(updated.getAmount()).isEqualTo(2000);
    }

    @Test
    @DisplayName("예산 수정 시 수정 할 예산이 존재하지 않으면 에러가 발생한다.")
    void updateAmountTest_NotExistBudget() {
        // given

        // when
        ApplicationException applicationException = catchThrowableOfType(
            () -> budgetCommandService.updateAmount(1L, 2000L, "hg-yu"),
            ApplicationException.class
        );

        // then
        assertThat(applicationException.getApplicationCode()).isEqualTo(ApplicationCode.BAD_REQUEST);
        assertThat(applicationException.getLogMessage()).isEqualTo("예산이 존재하지 않습니다.");
        assertThat(applicationException.getMessage()).isEqualTo("잘못된 요청입니다.");
    }

    @Test
    @DisplayName("예산 수정 시 예산을 등록한 유저와 수정 할 유저가 다르면 에러가 발생한다.")
    void updateAmountTest_NotEqualsCreatedUser() {
        // given
        testHelper.createAccount("hg-yu", "hyungyu");
        testHelper.createCategory("식비");
        budgetCommandService.createBudgets(List.of(new CreateBudget(1L, 1000L)), "hg-yu");
        Budget budget = budgetService.findBudgets().get(0);

        // when
        ApplicationException applicationException = catchThrowableOfType(
            () -> budgetCommandService.updateAmount(budget.getId(), 2000L, "hg-yu2"),
            ApplicationException.class
        );

        // then
        assertThat(applicationException.getApplicationCode()).isEqualTo(ApplicationCode.BAD_REQUEST);
        assertThat(applicationException.getLogMessage()).isEqualTo("예산 설정자가 아닙니다.");
        assertThat(applicationException.getMessage()).isEqualTo("잘못된 요청입니다.");
    }

    static class BudgetCommandServiceTestHelper {

        private final AccountEntityRepository accountEntityRepository;
        private final CategoryEntityRepository categoryEntityRepository;
        private long categoryGeneratedId = 1L;
        private long accountGeneratedId = 1L;

        BudgetCommandServiceTestHelper(AccountEntityRepository accountEntityRepository, CategoryEntityRepository categoryEntityRepository) {
            this.accountEntityRepository = accountEntityRepository;
            this.categoryEntityRepository = categoryEntityRepository;
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
    }
}
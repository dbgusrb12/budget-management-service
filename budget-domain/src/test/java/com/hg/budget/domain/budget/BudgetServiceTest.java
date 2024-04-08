package com.hg.budget.domain.budget;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.category.Category;
import com.hg.budget.domain.mock.MockBudgetRepository;
import com.hg.budget.domain.mock.MockDateTimeHolder;
import com.hg.budget.domain.mock.MockIdGenerator;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BudgetServiceTest {

    BudgetService budgetService;
    MockIdGenerator idGenerator;

    @BeforeEach
    void setUp() {
        idGenerator = new MockIdGenerator(1L);
        final var dateTimeHolder = new MockDateTimeHolder(LocalDateTime.of(2024, 7, 12, 0, 0, 0));
        budgetService = new BudgetService(idGenerator, dateTimeHolder, new MockBudgetRepository());
    }

    @Test
    @DisplayName("createBudget 으로 예산을 설정 할 수 있다.")
    void createBudgetTest() {
        // given
        final var testHelper = new BudgetServiceTestHelper();
        final var category = testHelper.createCategory("식비");
        final var account = testHelper.createAccount("hg-yu", "hyungyu");

        // when
        final var budget = budgetService.createBudget(1000, category, account);

        // then
        assertThat(budget.getId()).isEqualTo(1L);
        assertThat(budget.getAmount()).isEqualTo(1000);
        assertThat(budget.getCategory().getName()).isEqualTo("식비");
        assertThat(budget.getCreatedUser().getId()).isEqualTo("hg-yu");
        assertThat(budget.getCreatedDateTime()).isEqualTo(LocalDateTime.of(2024, 7, 12, 0, 0, 0));
        assertThat(budget.getUpdatedDateTime()).isEqualTo(LocalDateTime.of(2024, 7, 12, 0, 0, 0));
    }

    @Test
    @DisplayName("이미 같은 카테고리의 예산이 있을 경우 에러가 발생한다.")
    void createBudgetTest_Duplicated() {
        // given
        final var testHelper = new BudgetServiceTestHelper();
        final var category = testHelper.createCategory("식비");
        final var account = testHelper.createAccount("hg-yu", "hyungyu");
        budgetService.createBudget(1000, category, account);

        // when
        final var exception = catchThrowableOfType(
            () -> budgetService.createBudget(1000, category, account),
            IllegalArgumentException.class
        );

        // then
        assertThat(exception.getMessage()).isEqualTo("중복된 예산 입니다.");
    }

    @Test
    @DisplayName("updateAmount 로 예산 금액을 수정 할 수 있다.")
    void updateAmountTest() {
        // given
        final var testHelper = new BudgetServiceTestHelper();
        final var category = testHelper.createCategory("식비");
        final var account = testHelper.createAccount("hg-yu", "hyungyu");
        budgetService.createBudget(1000, category, account);

        // when
        final var budget = budgetService.updateAmount(1L, 2000);

        // then
        assertThat(budget.getAmount()).isEqualTo(2000);
    }

    @Test
    @DisplayName("수정 할 예산이 없다면 빈 객체를 반환한다.")
    void updateAmountTest_NotExist() {
        // given
        // when
        final var budget = budgetService.updateAmount(1L, 2000);

        // then
        assertThat(budget.notExist()).isTrue();
    }

    @Test
    @DisplayName("id 기반으로 예산을 조회 할 수 있다.")
    void findBudgetTest_ById() {
        // given
        final var testHelper = new BudgetServiceTestHelper();
        final var category = testHelper.createCategory("식비");
        final var account = testHelper.createAccount("hg-yu", "hyungyu");
        budgetService.createBudget(1000, category, account);

        // when
        final var budget = budgetService.findBudget(1L);

        // then
        assertThat(budget.getId()).isEqualTo(1L);
        assertThat(budget.getAmount()).isEqualTo(1000);
        assertThat(budget.getCategory().getName()).isEqualTo("식비");
        assertThat(budget.getCreatedUser().getId()).isEqualTo("hg-yu");
        assertThat(budget.getCreatedDateTime()).isEqualTo(LocalDateTime.of(2024, 7, 12, 0, 0, 0));
        assertThat(budget.getUpdatedDateTime()).isEqualTo(LocalDateTime.of(2024, 7, 12, 0, 0, 0));
    }

    @Test
    @DisplayName("카테고리,생성자 기반으로 예산을 조회 할 수 있다.")
    void findBudgetTest_ByCategoryAndCreatedUser() {
        // given
        final var testHelper = new BudgetServiceTestHelper();
        final var category = testHelper.createCategory("식비");
        final var account = testHelper.createAccount("hg-yu", "hyungyu");
        budgetService.createBudget(1000, category, account);

        // when
        final var budget = budgetService.findBudget(category, account);

        // then
        assertThat(budget.getId()).isEqualTo(1L);
        assertThat(budget.getAmount()).isEqualTo(1000);
        assertThat(budget.getCategory().getName()).isEqualTo("식비");
        assertThat(budget.getCreatedUser().getId()).isEqualTo("hg-yu");
        assertThat(budget.getCreatedDateTime()).isEqualTo(LocalDateTime.of(2024, 7, 12, 0, 0, 0));
        assertThat(budget.getUpdatedDateTime()).isEqualTo(LocalDateTime.of(2024, 7, 12, 0, 0, 0));
    }

    @Test
    @DisplayName("생성자 기반으로 예산 리스트를 조회 할 수 있다.")
    void findBudgetsTest_ByCreatedUser() {
        // given
        final var testHelper = new BudgetServiceTestHelper();
        final var category = testHelper.createCategory("식비");
        final var account = testHelper.createAccount("hg-yu", "hyungyu");
        budgetService.createBudget(1000, category, account);

        idGenerator.setId(2L);
        final var category2 = testHelper.createCategory("여가");
        budgetService.createBudget(2000, category2, account);

        // when
        final var budgets = budgetService.findBudgets(account);

        // then
        assertThat(budgets.size()).isEqualTo(2);
        assertThat(budgets.get(0).getAmount()).isEqualTo(1000);
        assertThat(budgets.get(0).getCategory().getName()).isEqualTo("식비");
        assertThat(budgets.get(1).getAmount()).isEqualTo(2000);
        assertThat(budgets.get(1).getCategory().getName()).isEqualTo("여가");
    }

    @Test
    @DisplayName("모든 예산 리스트를 조회 할 수 있다.")
    void findBudgetsTest() {
        // given
        final var testHelper = new BudgetServiceTestHelper();
        final var category = testHelper.createCategory("식비");
        final var account = testHelper.createAccount("hg-yu", "hyungyu");
        budgetService.createBudget(1000, category, account);

        idGenerator.setId(2L);
        final var category2 = testHelper.createCategory("여가");
        final var account2 = testHelper.createAccount("hg.yu", "hyun-gyu");
        budgetService.createBudget(2000, category2, account2);

        // when
        final var budgets = budgetService.findBudgets();

        // then
        assertThat(budgets.size()).isEqualTo(2);
        assertThat(budgets.get(0).getAmount()).isEqualTo(1000);
        assertThat(budgets.get(0).getCategory().getName()).isEqualTo("식비");
        assertThat(budgets.get(1).getAmount()).isEqualTo(2000);
        assertThat(budgets.get(1).getCategory().getName()).isEqualTo("여가");
    }

    static class BudgetServiceTestHelper {

        Category createCategory(String name) {
            return Category.of(1L, name);
        }

        Account createAccount(String id, String nickname) {
            return Account.of(
                id,
                "PASSWORD",
                nickname,
                "LIVED",
                "ROLE_USER",
                LocalDateTime.of(2024, 7, 12, 0, 0, 0),
                LocalDateTime.of(2024, 7, 12, 0, 0, 0)
            );
        }
    }
}
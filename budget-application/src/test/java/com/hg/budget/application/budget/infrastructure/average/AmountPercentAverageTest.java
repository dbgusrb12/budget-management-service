package com.hg.budget.application.budget.infrastructure.average;

import static org.assertj.core.api.Assertions.assertThat;

import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.budget.Budget;
import com.hg.budget.domain.category.Category;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AmountPercentAverageTest {

    @Test
    @DisplayName("AmountPercentAverage 로 각 카테고리 별 평균 퍼센트를 구할 수 있다.")
    void getAveragesTest() {
        // given
        final var testHelper = new AmountPercentAverageTestHelper();
        final var 식비 = testHelper.createCategory("식비");
        final var 교통 = testHelper.createCategory("교통");
        final var 주거 = testHelper.createCategory("주거");
        final var 문화여가 = testHelper.createCategory("문화/여가");
        final var 카페 = testHelper.createCategory("카페");
        final var 여행 = testHelper.createCategory("여행");
        final var 교육 = testHelper.createCategory("교육");
        final var 기타 = testHelper.createCategory("기타");

        final var hyungyu = testHelper.createAccount("hg-yu", "hyungyu");

        final var budgets = List.of(
            testHelper.createBudget(식비, 1000L, hyungyu),
            testHelper.createBudget(교통, 2000L, hyungyu),
            testHelper.createBudget(주거, 3000L, hyungyu),
            testHelper.createBudget(문화여가, 4000L, hyungyu),
            testHelper.createBudget(카페, 3000L, hyungyu),
            testHelper.createBudget(여행, 2000L, hyungyu),
            testHelper.createBudget(교육, 1000L, hyungyu),
            testHelper.createBudget(기타, 2000L, hyungyu)
        );
        final var amountPercentAverage = new AmountPercentAverage(budgets);

        // when
        final var averages = amountPercentAverage.getAverages();

        // then
        assertThat(averages.get(0).percent()).isEqualTo(6);
        assertThat(averages.get(0).category().getName()).isEqualTo("식비");
        assertThat(averages.get(1).percent()).isEqualTo(12);
        assertThat(averages.get(1).category().getName()).isEqualTo("교통");
        assertThat(averages.get(2).percent()).isEqualTo(17);
        assertThat(averages.get(2).category().getName()).isEqualTo("주거");
        assertThat(averages.get(3).percent()).isEqualTo(22);
        assertThat(averages.get(3).category().getName()).isEqualTo("문화/여가");
        assertThat(averages.get(4).percent()).isEqualTo(16);
        assertThat(averages.get(4).category().getName()).isEqualTo("카페");
        assertThat(averages.get(5).percent()).isEqualTo(11);
        assertThat(averages.get(5).category().getName()).isEqualTo("여행");
        assertThat(averages.get(6).percent()).isEqualTo(5);
        assertThat(averages.get(6).category().getName()).isEqualTo("교육");
        assertThat(averages.get(7).percent()).isEqualTo(11);
        assertThat(averages.get(7).category().getName()).isEqualTo("기타");
    }

    static class AmountPercentAverageTestHelper {

        private long categoryGeneratedId = 1L;
        private long budgetGeneratedId = 1L;

        AmountPercentAverageTestHelper() {
        }

        Category createCategory(String name) {
            return Category.of(categoryGeneratedId++, name);
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

        Budget createBudget(Category category, long amount, Account account) {
            return Budget.of(
                budgetGeneratedId++,
                category,
                amount,
                account,
                LocalDateTime.of(2024, 7, 12, 0, 0, 0),
                LocalDateTime.of(2024, 7, 12, 0, 0, 0)
            );
        }
    }
}
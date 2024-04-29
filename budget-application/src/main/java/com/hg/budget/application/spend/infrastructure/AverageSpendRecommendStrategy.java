package com.hg.budget.application.spend.infrastructure;

import com.hg.budget.application.spend.client.SpendRecommendStrategy;
import com.hg.budget.application.spend.client.dto.RecommendSpend;
import com.hg.budget.domain.budget.Budget;
import com.hg.budget.domain.category.Category;
import com.hg.budget.domain.spend.Spend;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AverageSpendRecommendStrategy implements SpendRecommendStrategy {

    @Override
    public List<RecommendSpend> recommend(List<Budget> budgets, List<Spend> spends) {
        CategoryFilter filter = new CategoryFilter(budgets, spends);
        List<Recommend> recommends = filter.filter();
        return new ArrayList<>();
    }

    private static class CategoryFilter {

        private Map<Category, Budget> budgetByCategory;
        private Map<Category, List<Spend>> spendsByCategory;

        public CategoryFilter(List<Budget> budgets, List<Spend> spends) {
            this.budgetByCategory = budgets.stream()
                .collect(Collectors.toMap(Budget::getCategory, Function.identity()));
            this.spendsByCategory = spends.stream()
                .collect(Collectors.groupingBy(
                    Spend::getCategory,
                    Collectors.mapping(Function.identity(), Collectors.toList())
                ));
        }

        public List<Recommend> filter() {
            return budgetByCategory.entrySet().stream()
                .map(categoryBudgetEntry -> {
                    final List<Spend> spends = this.spendsByCategory.getOrDefault(categoryBudgetEntry.getKey(), new ArrayList<>());
                    return new Recommend(categoryBudgetEntry.getValue(), spends);
                }).toList();
        }
    }

    private static class Recommend {

        private final int dayOfMonth;  // 현재 날짜 (지난 일 수)
        private final int lengthOfMonth; // 해당 월의 총 일 수
        private final int remainingDayOfMonth; // 남은 일 수
        private final Budget budget;
        private final long totalSpentAmount; // 지금까지 사용한 지출 총액

        public Recommend(Budget budget, List<Spend> spends) {
            final LocalDate now = LocalDate.now();
            this.dayOfMonth = now.getDayOfMonth();
            this.lengthOfMonth = now.lengthOfMonth();
            this.remainingDayOfMonth = lengthOfMonth - dayOfMonth;
            this.budget = budget;
            this.totalSpentAmount = spends.stream()
                .mapToLong(Spend::getAmount)
                .sum();
        }

        public long recommendAmount() {
            if (isEmptyBudgetAmount()) {
                // 남은 예산이 없을 경우
                return getMinimumRecommendSpendAmount(); // 예산 초과 문구 노출
            }
            if (isAffordBudgetAmount()) {
                // 하루 평균 지출 금액이 권장하는 지출 금액보다 적을 때
                return getSpentAmountOfDay(); // 잘 아끼고 있을 때 문구 노출
            }
            // 하루 평균 지출 금액이 권장하는 지출 금액보다 높을 때
            return Math.max(getMinimumRecommendSpendAmount(), getRecommendSpentAmount()); // 기준을 넘었을 때 문구 노출
        }

        /*
         * 남은 예산이 없는지 판별
         */
        private boolean isEmptyBudgetAmount() {
            return getRemainingBudget() <= 0;
        }

        /*
         * 예산의 여유가 있는지 판별
         */
        private boolean isAffordBudgetAmount() {
            return getSpentAmountOfDay() > getAverageSpentAmount();
        }

        /*
         * 최소 추천 금액
         */
        private long getMinimumRecommendSpendAmount() {
            return budget.getAmount() / (lengthOfMonth * 2L);
        }

        /*
         * 추천 지출 금액
         */
        private long getRecommendSpentAmount() {
            return getRemainingBudget() / remainingDayOfMonth;
        }

        /*
         * 권장하는 지출 금액
         */
        private long getSpentAmountOfDay() {
            return budget.getAmount() / lengthOfMonth;
        }

        /*
         * 현재 하루 평균 지출 금액
         */
        private long getAverageSpentAmount() {
            return totalSpentAmount / dayOfMonth;
        }

        /*
         * 남은 예산 조회
         */
        private long getRemainingBudget() {
            return budget.getAmount() - totalSpentAmount;
        }
    }

    enum RecommendComment {
        GOOD,

    }
}

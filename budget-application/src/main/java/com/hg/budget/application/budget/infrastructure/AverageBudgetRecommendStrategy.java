package com.hg.budget.application.budget.infrastructure;

import com.hg.budget.application.budget.client.BudgetRecommendStrategy;
import com.hg.budget.application.budget.client.dto.RecommendBudget;
import com.hg.budget.application.budget.infrastructure.average.AmountPercentAverage;
import com.hg.budget.application.budget.infrastructure.average.Average;
import com.hg.budget.application.budget.infrastructure.average.AverageCalculator;
import com.hg.budget.domain.budget.Budget;
import com.hg.budget.domain.category.Category;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AverageBudgetRecommendStrategy implements BudgetRecommendStrategy {

    // FIXME 기타 카테고리를 조회 할 수 있는 방법 고려하기
    private final Category etcCategory = Category.of(8L, "기타");

    @Override
    public List<RecommendBudget> recommend(long totalAmount, List<Budget> budgets) {
        final AmountPercentAverage amountPercentAverage = new AmountPercentAverage(budgets);
        final List<Average> averages = amountPercentAverage.getAverages();
        final AverageCalculator averageCalculator = new AverageCalculator(totalAmount, averages, etcCategory);
        return averageCalculator.calculate();
    }
}

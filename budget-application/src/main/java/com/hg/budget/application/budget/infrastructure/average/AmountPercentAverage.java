package com.hg.budget.application.budget.infrastructure.average;

import com.hg.budget.domain.budget.Budget;
import com.hg.budget.domain.category.Category;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AmountPercentAverage {

    private final Map<Category, Double> amountPercentAverageByCategory;

    public AmountPercentAverage(List<Budget> budgets, TotalAmount totalAmount) {
        this.amountPercentAverageByCategory = budgets.stream()
            .collect(Collectors.groupingBy(
                Budget::getCategory,
                Collectors.mapping(totalAmount::findPercent, Collectors.averagingLong(Long::longValue))
            ));
    }

    public List<Average> getAverage() {
        return amountPercentAverageByCategory.entrySet().stream()
            .map(amountPercentAverage -> new Average(amountPercentAverage.getKey(), Math.round(amountPercentAverage.getValue())))
            .toList();
    }
}

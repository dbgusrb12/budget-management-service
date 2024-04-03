package com.hg.budget.application.budget.infrastructure.average;

import com.hg.budget.domain.budget.Budget;
import com.hg.budget.domain.category.Category;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class AmountPercentAverage {

    private final long totalAmount;
    private final Map<Category, Long> totalAmountByCategory;

    public AmountPercentAverage(List<Budget> budgets) {
        final List<Budget> cloneBudgets = budgets == null ? new ArrayList<>() : budgets;
        // 전체 예산 금액 합산
        this.totalAmount = cloneBudgets.stream()
            .mapToLong(Budget::getAmount)
            .sum();
        // Category 별 예산 금액 합산
        this.totalAmountByCategory = cloneBudgets.stream()
            .collect(Collectors.groupingBy(
                Budget::getCategory,
                Collectors.mapping(Budget::getAmount, Collectors.summingLong(Long::longValue))
            ));
    }

    public List<Average> getAverages() {
        return totalAmountByCategory.entrySet().stream()
            .map(this::convertAverage)
            .peek(System.out::println)
            .toList();
    }

    private Average convertAverage(Entry<Category, Long> amountPercentAverage) {
        return new Average(amountPercentAverage.getKey(), findPercent(amountPercentAverage.getValue()));
    }

    private double findPercent(long amount) {
        if (totalAmount == 0) {
            return 0;
        }
        return amount / (double) totalAmount * 100;
    }
}

package com.hg.budget.application.budget.infrastructure.average;

import com.hg.budget.domain.budget.Budget;
import com.hg.budget.domain.category.Category;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class AmountPercentAverage {

    private final static int HUNDRED_PERCENT = 100;
    private final static int ZERO_PERCENT = 0;
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
        final List<Average> averages = convertAverage();
        // 오차 보정
        return calculateDiff(averages);
    }

    private List<Average> convertAverage() {
        return totalAmountByCategory.entrySet().stream()
            .map(this::convertAverage)
            .collect(Collectors.toList());
    }

    private List<Average> calculateDiff(List<Average> averages) {
        List<Average> cloneAverage = new ArrayList<>(averages);
        if (cloneAverage.isEmpty()) {
            return cloneAverage;
        }
        cloneAverage.sort(Comparator.comparing(average -> average.category().getId()));
        final long diff = getDiff(cloneAverage);
        for (int i = 0; i < diff; i++) {
            final int index = i % cloneAverage.size();
            final Average average = cloneAverage.remove(index).plus(1);
            cloneAverage.add(index, average);
        }
        return cloneAverage;
    }

    private long getDiff(List<Average> averages) {
        final long sumPercentage = averages.stream()
            .mapToLong(Average::percent)
            .sum();
        return HUNDRED_PERCENT - sumPercentage;
    }

    private Average convertAverage(Entry<Category, Long> amountPercentAverage) {
        final Category category = amountPercentAverage.getKey();
        final long percent = findPercent(amountPercentAverage.getValue());
        return new Average(category, percent);
    }

    private long findPercent(long amount) {
        if (totalAmount == ZERO_PERCENT) {
            return ZERO_PERCENT;
        }
        return HUNDRED_PERCENT * amount / totalAmount;
    }
}

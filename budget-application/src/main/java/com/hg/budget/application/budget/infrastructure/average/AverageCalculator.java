package com.hg.budget.application.budget.infrastructure.average;

import com.hg.budget.application.budget.dto.RecommendBudget;
import com.hg.budget.application.category.dto.CategoryDto;
import com.hg.budget.domain.category.Category;
import java.util.ArrayList;
import java.util.List;

public class AverageCalculator {

    private static final long MIN_PERCENT = 10;
    private final long totalAmount;
    private final List<Average> averages;
    private final Category etcCategory;

    public AverageCalculator(long totalAmount, List<Average> averages, Category etcCategory) {
        this.totalAmount = totalAmount;
        this.averages = averages;
        this.etcCategory = etcCategory;
    }

    public List<RecommendBudget> calculate() {
        final Average etcAverage = getTotalEtcAverage();
        final List<Average> averages = getNotEtcAverage();

        List<Average> totalAverages = new ArrayList<>(averages);
        totalAverages.add(etcAverage);

        return totalAverages.stream()
            .map(average -> {
                final Category category = average.category();
                final long amount = findAmount(average.percent());
                return new RecommendBudget(new CategoryDto(category.getId(), category.getName()), amount);
            }).toList();
    }

    private long findAmount(long percent) {
        return Math.round((double) totalAmount * percent / 100);
    }

    private Average getTotalEtcAverage() {
        final Average etcAverage = getEtcAverage();
        final long etcPercent = getTotalEtcPercent();
        return etcAverage.plus(etcPercent);
    }

    private long getTotalEtcPercent() {
        return this.averages.stream()
            .filter(average -> !etcCategory.equals(average.category()))
            .filter(average -> average.percent() < MIN_PERCENT)
            .mapToLong(Average::percent)
            .sum();
    }

    private Average getEtcAverage() {
        return this.averages.stream()
            .filter(average -> etcCategory.equals(average.category()))
            .findFirst()
            .orElse(new Average(etcCategory, 0));
    }

    private List<Average> getNotEtcAverage() {
        return this.averages.stream()
            .filter(average -> !etcCategory.equals(average.category()))
            .filter(average -> average.percent() >= MIN_PERCENT)
            .toList();
    }
}

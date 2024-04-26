package com.hg.budget.application.budget.infrastructure.average;

import com.hg.budget.application.budget.client.dto.RecommendBudget;
import com.hg.budget.domain.category.Category;
import java.util.ArrayList;
import java.util.List;

public class AverageCalculator {

    private static final int HUNDRED_PERCENT = 100;
    private static final int ZERO_PERCENT = 0;
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
        final List<Average> averages = mergeAverage();
        return averages.stream()
            .map(average -> {
                final long amount = findAmount(average.percent());
                return new RecommendBudget(average.category(), amount);
            })
            .toList();
    }

    private List<Average> mergeAverage() {
        final Average etcAverage = getTotalEtcAverage();
        final List<Average> averages = getNotEtcAverage();

        List<Average> totalAverages = new ArrayList<>(averages);
        if (etcAverage.percent() > ZERO_PERCENT) {
            totalAverages.add(etcAverage);
        }
        return totalAverages;
    }

    private long findAmount(long percent) {
        return totalAmount / HUNDRED_PERCENT * percent;
    }

    private Average getTotalEtcAverage() {
        final Average etcAverage = getEtcAverage();
        final long etcPercent = getTotalEtcPercent();
        return etcAverage.plus(etcPercent);
    }

    private List<Average> getNotEtcAverage() {
        return this.averages.stream()
            .filter(average -> !etcCategory.equals(average.category()))
            .filter(average -> average.percent() >= MIN_PERCENT)
            .toList();
    }

    private Average getEtcAverage() {
        return this.averages.stream()
            .filter(average -> etcCategory.equals(average.category()))
            .findFirst()
            .orElse(new Average(etcCategory, 0));
    }

    private long getTotalEtcPercent() {
        return this.averages.stream()
            .filter(average -> !etcCategory.equals(average.category()))
            .filter(average -> average.percent() < MIN_PERCENT)
            .mapToLong(Average::percent)
            .sum();
    }
}

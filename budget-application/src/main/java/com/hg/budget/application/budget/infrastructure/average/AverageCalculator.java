package com.hg.budget.application.budget.infrastructure.average;

import com.hg.budget.application.budget.client.dto.RecommendBudget;
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
        final List<Average> averages = mergeAverage();
        return averages.stream()
            .map(average -> {
                // FIXME 반올림 시 1 오차 나는 부분 수정 필요
                final long amount = findAmount(average.percent());
                return new RecommendBudget(average.category(), amount);
            })
            .toList();
    }

    private List<Average> mergeAverage() {
        final Average etcAverage = getTotalEtcAverage();
        final List<Average> averages = getNotEtcAverage();

        List<Average> totalAverages = new ArrayList<>(averages);
        if (etcAverage.percent() > 0) {
            totalAverages.add(etcAverage);
        }
        return totalAverages;
    }

    private long findAmount(double percent) {
        return Math.round(totalAmount * percent / 100);
    }

    private Average getTotalEtcAverage() {
        final Average etcAverage = getEtcAverage();
        final double etcPercent = getTotalEtcPercent();
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

    private double getTotalEtcPercent() {
        return this.averages.stream()
            .filter(average -> !etcCategory.equals(average.category()))
            .filter(average -> average.percent() < MIN_PERCENT)
            .mapToDouble(Average::percent)
            .sum();
    }
}

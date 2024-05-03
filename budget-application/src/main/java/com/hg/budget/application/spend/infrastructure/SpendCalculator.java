package com.hg.budget.application.spend.infrastructure;

import com.hg.budget.domain.budget.Budget;
import com.hg.budget.domain.spend.Spend;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

public class SpendCalculator {

    private final int dayOfMonth;  // 현재 날짜 (지난 일 수)
    private final int lengthOfMonth; // 해당 월의 총 일 수
    private final int remainingDayOfMonth; // 남은 일 수
    private final Budget budget;
    @Getter
    private final long totalSpentAmount; // 지금까지 사용한 지출 총액

    public SpendCalculator(LocalDate today, Budget budget, List<Spend> spends) {
        this.dayOfMonth = today.getDayOfMonth();
        this.lengthOfMonth = today.lengthOfMonth();
        this.remainingDayOfMonth = lengthOfMonth - dayOfMonth + 1;
        this.budget = budget;
        this.totalSpentAmount = spends.stream()
            .mapToLong(Spend::getAmount)
            .sum();
    }

    /*
     * 남은 예산이 없는지 판별
     */
    public boolean isEmptyBudgetAmount() {
        return getRemainingBudget() <= 0;
    }

    /*
     * 예산의 여유가 있는지 판별
     */
    public boolean isAffordBudgetAmount() {
        return getSpentAmountOfDay() > getAverageSpentAmount();
    }

    /*
     * 최소 추천 금액
     */
    public long getMinimumRecommendSpendAmount() {
        return budget.getAmount() / (lengthOfMonth * 2L);
    }

    /*
     * 추천 지출 금액
     */
    public long getRecommendSpentAmount() {
        return getRemainingBudget() / remainingDayOfMonth;
    }

    /*
     * 권장하는 지출 금액
     */
    public long getSpentAmountOfDay() {
        return budget.getAmount() / lengthOfMonth;
    }

    /*
     * 현재 하루 평균 지출 금액
     */
    public long getAverageSpentAmount() {
        return totalSpentAmount / dayOfMonth;
    }

    /*
     * 남은 예산 조회
     */
    public long getRemainingBudget() {
        return budget.getAmount() - totalSpentAmount;
    }
}

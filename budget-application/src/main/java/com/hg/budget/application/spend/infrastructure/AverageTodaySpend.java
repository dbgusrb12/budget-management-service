package com.hg.budget.application.spend.infrastructure;

import com.hg.budget.application.spend.client.dto.TodaySpend;
import com.hg.budget.domain.category.Category;

public class AverageTodaySpend implements Comparable<AverageTodaySpend> {

    private final Category category;
    private final SpendCalculator spendCalculator;

    public AverageTodaySpend(Category category, SpendCalculator spendCalculator) {
        this.category = category;
        this.spendCalculator = spendCalculator;
    }

    public TodaySpend getTodaySpend() {
        final long appropriateAmount = spendCalculator.getSpentAmountOfDay();
        final long spentAmount = spendCalculator.getTotalSpentAmount();
        final long risk = getRisk(appropriateAmount, spentAmount);
        return new TodaySpend(category, appropriateAmount, spentAmount, risk);
    }

    /*
     * 위험도 조회
     */
    private long getRisk(long appropriateAmount, long spentAmount) {
        if (appropriateAmount == 0) {
            // 예산이 없다면 사용한 금액만큼 위험도를 표시함.
            return spentAmount;
        }
        if (spentAmount < appropriateAmount) {
            // 사용한 금액이 사용해야 할 금액보다 적다면 위험도 없음으로 표시함.
            return 0;
        }

        return spentAmount * 100 / appropriateAmount;
    }

    @Override
    public int compareTo(AverageTodaySpend o) {
        return this.category.getId().compareTo(o.category.getId());
    }
}

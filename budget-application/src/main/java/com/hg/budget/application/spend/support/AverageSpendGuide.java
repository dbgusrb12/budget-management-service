package com.hg.budget.application.spend.support;

import com.hg.budget.application.category.dto.CategoryDto;
import com.hg.budget.application.spend.dto.SpendGuideDto;
import com.hg.budget.domain.category.Category;

public class AverageSpendGuide implements Comparable<AverageSpendGuide> {

    private final Category category;
    private final SpendConsultingCalculator spendConsultingCalculator;

    public AverageSpendGuide(Category category, SpendConsultingCalculator spendConsultingCalculator) {
        this.category = category;
        this.spendConsultingCalculator = spendConsultingCalculator;
    }

    public SpendGuideDto getSpendGuide() {
        final long appropriateAmount = round(spendConsultingCalculator.getSpentAmountOfDay());
        final long spentAmount = spendConsultingCalculator.getTotalSpentAmount();
        final long risk = getRisk(appropriateAmount, spentAmount);
        return new SpendGuideDto(CategoryDto.from(category), appropriateAmount, spentAmount, risk);
    }

    private long round(long amount) {
        if (amount > 100) {
            return Math.round((float) amount / 100) * 100L;
        }
        if (amount > 10) {
            return Math.round((float) amount / 10) * 10L;
        }
        return amount;
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
    public int compareTo(AverageSpendGuide o) {
        return this.category.getId().compareTo(o.category.getId());
    }
}

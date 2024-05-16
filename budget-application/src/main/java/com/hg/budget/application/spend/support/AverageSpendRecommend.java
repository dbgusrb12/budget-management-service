package com.hg.budget.application.spend.support;

import com.hg.budget.application.category.dto.CategoryDto;
import com.hg.budget.application.spend.dto.RecommendDto;
import com.hg.budget.domain.category.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class AverageSpendRecommend implements Comparable<AverageSpendRecommend> {

    private final Category category;
    private final SpendConsultingCalculator spendConsultingCalculator;

    public AverageSpendRecommend(Category category, SpendConsultingCalculator spendConsultingCalculator) {
        this.category = category;
        this.spendConsultingCalculator = spendConsultingCalculator;
    }

    public RecommendDto recommendAmount() {
        if (spendConsultingCalculator.isEmptyBudgetAmount()) {
            // 남은 예산이 없을 경우
            final long recommendAmount = spendConsultingCalculator.getMinimumRecommendSpendAmount();
            return generateRecommendSpend(recommendAmount, RecommendComment.DANGEROUS); // 예산 초과 문구 노출
        }
        if (spendConsultingCalculator.isAffordBudgetAmount()) {
            // 하루 평균 지출 금액이 권장하는 지출 금액보다 적을 때
            final long recommendAmount = spendConsultingCalculator.getSpentAmountOfDay();
            return generateRecommendSpend(recommendAmount, RecommendComment.EXCELLENT); // 잘 아끼고 있을 때 문구 노출
        }
        // 하루 평균 지출 금액이 권장하는 지출 금액보다 높을 때
        final long recommendAmount = Math.max(
            spendConsultingCalculator.getMinimumRecommendSpendAmount(),
            spendConsultingCalculator.getRecommendSpentAmount()
        );
        return generateRecommendSpend(recommendAmount, RecommendComment.BAD); // 기준을 넘었을 때 문구 노출
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

    private RecommendDto generateRecommendSpend(long recommendAmount, RecommendComment comment) {
        return new RecommendDto(CategoryDto.from(category), round(recommendAmount), comment.getComment());
    }

    @Override
    public int compareTo(AverageSpendRecommend o) {
        return this.category.getId().compareTo(o.category.getId());
    }

    @Getter
    @AllArgsConstructor
    enum RecommendComment {
        EXCELLENT("절약을 잘 실천하고 있어요."),
        BAD("평균보다 더 지출중이에요."),
        DANGEROUS("예산을 초과해서 지출중이에요.");

        private final String comment;
    }

}

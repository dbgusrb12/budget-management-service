package com.hg.budget.application.spend.infrastructure;

import com.hg.budget.application.spend.client.dto.Recommend;
import com.hg.budget.application.spend.client.dto.RecommendComment;
import com.hg.budget.domain.category.Category;

public class AverageSpendRecommend {

    private final Category category;
    private final SpendCalculator spendCalculator;

    public AverageSpendRecommend(Category category, SpendCalculator spendCalculator) {
        this.category = category;
        this.spendCalculator = spendCalculator;
    }

    public Recommend recommendAmount() {
        if (spendCalculator.isEmptyBudgetAmount()) {
            // 남은 예산이 없을 경우
            final long recommendAmount = spendCalculator.getMinimumRecommendSpendAmount();
            return generateRecommendSpend(recommendAmount, RecommendComment.DANGEROUS); // 예산 초과 문구 노출
        }
        if (spendCalculator.isAffordBudgetAmount()) {
            // 하루 평균 지출 금액이 권장하는 지출 금액보다 적을 때
            final long recommendAmount = spendCalculator.getSpentAmountOfDay();
            return generateRecommendSpend(recommendAmount, RecommendComment.EXCELLENT); // 잘 아끼고 있을 때 문구 노출
        }
        // 하루 평균 지출 금액이 권장하는 지출 금액보다 높을 때
        final long recommendAmount = Math.max(spendCalculator.getMinimumRecommendSpendAmount(), spendCalculator.getRecommendSpentAmount());
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

    private Recommend generateRecommendSpend(long recommendAmount, RecommendComment comment) {
        return new Recommend(category, round(recommendAmount), comment);
    }
}

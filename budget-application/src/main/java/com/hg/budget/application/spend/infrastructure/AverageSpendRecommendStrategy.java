package com.hg.budget.application.spend.infrastructure;

import com.hg.budget.application.spend.client.SpendRecommendStrategy;
import com.hg.budget.application.spend.client.dto.RecommendSpend;
import com.hg.budget.core.client.DateTimeHolder;
import com.hg.budget.domain.budget.Budget;
import com.hg.budget.domain.spend.Spend;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AverageSpendRecommendStrategy implements SpendRecommendStrategy {

    private final DateTimeHolder dateTimeHolder;

    @Override
    public List<RecommendSpend> recommend(List<Budget> budgets, List<Spend> spends) {
        final CategoryFilter filter = new CategoryFilter(dateTimeHolder.now().toLocalDate(), budgets, spends);
        final List<AverageSpendRecommend> averageSpendRecommends = filter.filter();
        return averageSpendRecommends.stream()
            .map(AverageSpendRecommend::recommendAmount)
            .toList();
    }
}

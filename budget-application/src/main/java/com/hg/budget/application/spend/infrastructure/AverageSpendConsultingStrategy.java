package com.hg.budget.application.spend.infrastructure;

import com.hg.budget.application.spend.client.SpendConsultingStrategy;
import com.hg.budget.application.spend.client.dto.Recommend;
import com.hg.budget.application.spend.client.dto.TodaySpend;
import com.hg.budget.core.client.DateTimeHolder;
import com.hg.budget.domain.budget.Budget;
import com.hg.budget.domain.spend.Spend;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AverageSpendConsultingStrategy implements SpendConsultingStrategy {

    private final DateTimeHolder dateTimeHolder;

    @Override
    public List<Recommend> recommend(List<Budget> budgets, List<Spend> spends) {
        final CategoryFilter filter = new CategoryFilter(dateTimeHolder.now().toLocalDate(), budgets, spends);
        final List<AverageSpendRecommend> averageSpendRecommends = filter.recommendFilter();
        return averageSpendRecommends.stream()
            .map(AverageSpendRecommend::recommendAmount)
            .toList();
    }

    @Override
    public List<TodaySpend> getTodaySpend(List<Budget> budgets, List<Spend> spends) {
        final CategoryFilter filter = new CategoryFilter(dateTimeHolder.now().toLocalDate(), budgets, spends);
        final List<AverageTodaySpend> averageTodaySpends = filter.todayFilter();
        return averageTodaySpends.stream()
            .map(AverageTodaySpend::getTodaySpend)
            .toList();
    }
}

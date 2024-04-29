package com.hg.budget.application.spend.infrastructure;

import com.hg.budget.application.spend.client.SpendRecommendStrategy;
import com.hg.budget.application.spend.client.dto.RecommendSpend;
import com.hg.budget.domain.budget.Budget;
import com.hg.budget.domain.spend.Spend;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AverageSpendRecommendStrategy implements SpendRecommendStrategy {

    @Override
    public List<RecommendSpend> recommend(List<Budget> budgets, List<Spend> spends) {
        return new ArrayList<>();
    }
}

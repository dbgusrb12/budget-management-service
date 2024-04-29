package com.hg.budget.application.spend.client;

import com.hg.budget.application.spend.client.dto.RecommendSpend;
import com.hg.budget.domain.budget.Budget;
import com.hg.budget.domain.spend.Spend;
import java.util.List;

public interface SpendRecommendStrategy {

    List<RecommendSpend> recommend(List<Budget> budgets, List<Spend> spends);
}

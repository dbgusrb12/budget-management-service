package com.hg.budget.application.budget.client;

import com.hg.budget.application.budget.client.dto.RecommendBudget;
import com.hg.budget.domain.budget.Budget;
import java.util.List;

public interface BudgetRecommendStrategy {

    List<RecommendBudget> recommend(long totalAmount, List<Budget> budgets);
}

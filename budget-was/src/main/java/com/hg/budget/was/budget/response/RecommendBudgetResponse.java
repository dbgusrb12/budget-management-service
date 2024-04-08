package com.hg.budget.was.budget.response;

import com.hg.budget.application.budget.dto.RecommendBudgetDto;

public record RecommendBudgetResponse(BudgetCategory category, long amount) {

    public static RecommendBudgetResponse from(RecommendBudgetDto recommendBudget) {
        final BudgetCategory category = BudgetCategory.from(recommendBudget.category());
        return new RecommendBudgetResponse(category, recommendBudget.amount());
    }
}

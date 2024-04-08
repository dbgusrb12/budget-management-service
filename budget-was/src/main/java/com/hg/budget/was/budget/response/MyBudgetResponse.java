package com.hg.budget.was.budget.response;

import com.hg.budget.application.budget.dto.BudgetDto;

public record MyBudgetResponse(
    Long id,
    BudgetCategory category,
    long amount,
    String createdDateTime,
    String updatedDateTime
) {

    public static MyBudgetResponse from(BudgetDto budget) {
        final BudgetCategory category = BudgetCategory.from(budget.category());
        return new MyBudgetResponse(budget.id(), category, budget.amount(), budget.createdDateTime(), budget.updatedDateTime());
    }
}

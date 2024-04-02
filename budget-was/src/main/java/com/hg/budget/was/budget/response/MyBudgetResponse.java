package com.hg.budget.was.budget.response;

public record MyBudgetResponse(
    Long id,
    BudgetCategory category,
    long amount,
    String createdDateTime,
    String updatedDateTime
) {


    public record BudgetCategory(Long id, String name) {

    }
}
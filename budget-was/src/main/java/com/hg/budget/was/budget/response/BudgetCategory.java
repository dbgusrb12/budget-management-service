package com.hg.budget.was.budget.response;

import com.hg.budget.application.category.dto.CategoryDto;

public record BudgetCategory(Long id, String name) {

    public static BudgetCategory from(CategoryDto category) {
        return new BudgetCategory(category.getId(), category.getName());
    }
}

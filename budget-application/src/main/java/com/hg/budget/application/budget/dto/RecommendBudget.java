package com.hg.budget.application.budget.dto;

import com.hg.budget.application.category.dto.CategoryDto;

public record RecommendBudget(CategoryDto category, long amount) {

}

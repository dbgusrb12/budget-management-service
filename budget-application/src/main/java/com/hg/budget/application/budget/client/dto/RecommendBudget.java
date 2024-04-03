package com.hg.budget.application.budget.client.dto;

import com.hg.budget.domain.category.Category;

public record RecommendBudget(Category category, long amount) {

}

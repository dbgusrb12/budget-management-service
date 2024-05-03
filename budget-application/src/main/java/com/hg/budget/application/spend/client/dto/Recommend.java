package com.hg.budget.application.spend.client.dto;

import com.hg.budget.domain.category.Category;

public record Recommend(Category category, long amount, RecommendComment comment) {

}

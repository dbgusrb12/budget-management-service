package com.hg.budget.application.budget.dto;

import com.hg.budget.application.budget.client.dto.RecommendBudget;
import com.hg.budget.application.category.dto.CategoryDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RecommendBudgetDto {

    private final CategoryDto category;
    private final long amount;

    public static RecommendBudgetDto from(RecommendBudget recommendBudget) {
        final CategoryDto category = CategoryDto.from(recommendBudget.category());
        return new RecommendBudgetDto(category, recommendBudget.amount());
    }
}

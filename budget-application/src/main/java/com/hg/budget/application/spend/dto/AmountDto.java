package com.hg.budget.application.spend.dto;

import com.hg.budget.application.category.dto.CategoryDto;
import com.hg.budget.domain.category.Category;

public record AmountDto(CategoryDto category, long amount) {

    public static AmountDto of(Category category, long amount) {
        final CategoryDto categoryDto = CategoryDto.from(category);
        return new AmountDto(categoryDto, amount);
    }
}

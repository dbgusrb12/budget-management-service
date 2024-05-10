package com.hg.budget.application.spend.dto;

import com.hg.budget.application.category.dto.CategoryDto;
import com.hg.budget.domain.category.Category;

public record SpendGuideDto(CategoryDto category, long appropriateAmount, long spentAmount, long risk) {

    public static SpendGuideDto of(Category category, long appropriateAmount, long spentAmount, long risk) {
        final CategoryDto categoryDto = CategoryDto.from(category);
        return new SpendGuideDto(categoryDto, appropriateAmount, spentAmount, risk);
    }
}

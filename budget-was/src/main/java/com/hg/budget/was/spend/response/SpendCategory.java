package com.hg.budget.was.spend.response;

import com.hg.budget.application.category.dto.CategoryDto;

public record SpendCategory(Long id, String name) {

    public static SpendCategory from(CategoryDto category) {
        return new SpendCategory(category.getId(), category.getName());
    }
}
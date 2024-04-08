package com.hg.budget.was.category.response;

import com.hg.budget.application.category.dto.CategoryDto;

public record CategoryResponse(Long id, String name) {

    public static CategoryResponse from(CategoryDto category) {
        return new CategoryResponse(category.getId(), category.getName());
    }
}

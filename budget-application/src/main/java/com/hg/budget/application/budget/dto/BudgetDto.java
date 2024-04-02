package com.hg.budget.application.budget.dto;

import com.hg.budget.application.category.dto.CategoryDto;

public record BudgetDto(
    Long id,
    CategoryDto category,
    long amount,
    CreatedUser createdUser,
    String createdDateTime,
    String updatedDateTime
) {

}

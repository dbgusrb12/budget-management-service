package com.hg.budget.application.budget.dto;

import com.hg.budget.application.category.dto.CategoryDto;
import com.hg.budget.core.client.DateTimeHolder;
import com.hg.budget.domain.budget.Budget;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BudgetDto {

    private final Long id;
    private final CategoryDto category;
    private final long amount;
    private final CreatedUser createdUser;
    private final String createdDateTime;
    private final String updatedDateTime;

    public static BudgetDto of(Budget budget, DateTimeHolder dateTimeHolder) {
        final CategoryDto category = CategoryDto.from(budget.getCategory());
        final CreatedUser createdUser = CreatedUser.from(budget.getCreatedUser());
        return new BudgetDto(
            budget.getId(),
            category,
            budget.getAmount(),
            createdUser,
            dateTimeHolder.toString(budget.getCreatedDateTime()),
            dateTimeHolder.toString(budget.getUpdatedDateTime())
        );
    }
}

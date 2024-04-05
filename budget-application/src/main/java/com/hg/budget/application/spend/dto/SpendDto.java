package com.hg.budget.application.spend.dto;

import com.hg.budget.application.category.dto.CategoryDto;

public record SpendDto(
    Long id,
    CategoryDto category,
    long amount,
    String memo,
    SpentUser spentUser,
    String spentDateTime,
    boolean excludeTotal
) {

}

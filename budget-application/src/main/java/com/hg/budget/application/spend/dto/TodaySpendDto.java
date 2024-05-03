package com.hg.budget.application.spend.dto;

import com.hg.budget.application.category.dto.CategoryDto;

public record TodaySpendDto(CategoryDto category, long appropriateAmount, long spendAmount, long risk) {

}

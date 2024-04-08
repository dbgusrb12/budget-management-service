package com.hg.budget.application.spend.dto;

import com.hg.budget.application.category.dto.CategoryDto;

public record TotalAmount(CategoryDto category, long totalAmount) {

}

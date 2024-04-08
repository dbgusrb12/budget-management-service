package com.hg.budget.application.spend.dto;

import com.hg.budget.application.category.dto.CategoryDto;
import java.util.List;

public record TotalAmount(long totalAmount, List<TotalAmountByCategory> totalAmountByCategory) {

    public record TotalAmountByCategory(CategoryDto category, long totalAmount) {

    }
}

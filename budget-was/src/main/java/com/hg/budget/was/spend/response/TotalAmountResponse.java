package com.hg.budget.was.spend.response;

import com.hg.budget.application.spend.dto.TotalAmount;
import com.hg.budget.application.spend.dto.TotalAmount.TotalAmountByCategory;
import java.util.List;

public record TotalAmountResponse(long totalAmount, List<TotalAmountByCategoryResponse> totalAmountByCategory) {

    public static TotalAmountResponse from(TotalAmount totalAmount) {
        final List<TotalAmountByCategoryResponse> totalAmountByCategories = totalAmount.totalAmountByCategory().stream()
            .map(TotalAmountByCategoryResponse::from)
            .toList();
        return new TotalAmountResponse(totalAmount.totalAmount(), totalAmountByCategories);
    }

    public record TotalAmountByCategoryResponse(SpendCategory category, long totalAmount) {

        public static TotalAmountByCategoryResponse from(TotalAmountByCategory totalAmountByCategory) {
            final SpendCategory category = SpendCategory.from(totalAmountByCategory.category());
            return new TotalAmountByCategoryResponse(category, totalAmountByCategory.totalAmount());
        }
    }

}

package com.hg.budget.was.spend.response;

import com.hg.budget.application.spend.dto.TotalAmount;

public record TotalAmountResponse(SpendCategory category, long totalAmount) {

    public static TotalAmountResponse from(TotalAmount totalAmountByCategory) {
        final SpendCategory category = SpendCategory.from(totalAmountByCategory.category());
        return new TotalAmountResponse(category, totalAmountByCategory.totalAmount());
    }

}

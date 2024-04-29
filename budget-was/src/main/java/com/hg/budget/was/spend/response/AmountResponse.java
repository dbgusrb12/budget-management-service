package com.hg.budget.was.spend.response;

import com.hg.budget.application.spend.dto.AmountDto;

public record AmountResponse(SpendCategory category, long totalAmount) {

    public static AmountResponse from(AmountDto amountByCategory) {
        final SpendCategory category = SpendCategory.from(amountByCategory.category());
        return new AmountResponse(category, amountByCategory.amount());
    }
}

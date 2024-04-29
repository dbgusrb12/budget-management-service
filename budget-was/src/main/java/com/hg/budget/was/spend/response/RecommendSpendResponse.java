package com.hg.budget.was.spend.response;

import com.hg.budget.application.spend.dto.AmountDto;
import java.util.List;

public record RecommendSpendResponse(long totalAmount, List<AmountResponse> amountByCategories) {

    public static RecommendSpendResponse from(List<AmountDto> recommendSpendList) {
        final long totalAmount = recommendSpendList.stream()
            .mapToLong(AmountDto::amount)
            .sum();
        final List<AmountResponse> amountResponse = recommendSpendList.stream()
            .map(AmountResponse::from)
            .toList();
        return new RecommendSpendResponse(totalAmount, amountResponse);
    }
}

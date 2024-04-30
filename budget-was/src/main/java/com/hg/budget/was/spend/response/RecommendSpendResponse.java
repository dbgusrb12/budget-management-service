package com.hg.budget.was.spend.response;

import com.hg.budget.application.spend.dto.RecommendDto;
import java.util.List;

public record RecommendSpendResponse(long totalAmount, List<RecommendResponse> amountByCategories) {

    public static RecommendSpendResponse from(List<RecommendDto> recommendSpendList) {
        final long totalAmount = recommendSpendList.stream()
            .mapToLong(RecommendDto::amount)
            .sum();
        final List<RecommendResponse> amountResponse = recommendSpendList.stream()
            .map(RecommendResponse::from)
            .toList();
        return new RecommendSpendResponse(totalAmount, amountResponse);
    }

    public record RecommendResponse(SpendCategory category, long totalAmount, String comment) {

        public static RecommendResponse from(RecommendDto amountByCategory) {
            final SpendCategory category = SpendCategory.from(amountByCategory.category());
            return new RecommendResponse(category, amountByCategory.amount(), amountByCategory.comment());
        }
    }
}

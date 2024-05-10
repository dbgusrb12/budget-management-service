package com.hg.budget.was.spend.response;

import com.hg.budget.application.spend.dto.SpendGuideDto;
import java.util.List;

public record SpendGuideResponse(long totalAmount, List<GuideResponse> amountByCategories) {

    public static SpendGuideResponse from(List<SpendGuideDto> spendGuides) {
        final long totalAmount = spendGuides.stream()
            .mapToLong(SpendGuideDto::spentAmount)
            .sum();
        final List<GuideResponse> amountByCategories = spendGuides.stream()
            .map(GuideResponse::from)
            .toList();
        return new SpendGuideResponse(totalAmount, amountByCategories);
    }


    public record GuideResponse(SpendCategory category, long appropriateAmount, long spentAmount, long risk) {

        public static GuideResponse from(SpendGuideDto spendGuide) {
            final SpendCategory category = SpendCategory.from(spendGuide.category());
            return new GuideResponse(category, spendGuide.appropriateAmount(), spendGuide.spentAmount(), spendGuide.risk());
        }
    }
}

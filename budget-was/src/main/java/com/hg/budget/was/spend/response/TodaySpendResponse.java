package com.hg.budget.was.spend.response;

import com.hg.budget.application.spend.dto.TodaySpendDto;
import java.util.List;

public record TodaySpendResponse(long totalAmount, List<TodayResponse> amountByCategories) {

    public static TodaySpendResponse from(List<TodaySpendDto> todaySpendList) {
        final long totalAmount = todaySpendList.stream()
            .mapToLong(TodaySpendDto::spendAmount)
            .sum();
        final List<TodayResponse> amountByCategories = todaySpendList.stream()
            .map(TodayResponse::from)
            .toList();
        return new TodaySpendResponse(totalAmount, amountByCategories);
    }


    public record TodayResponse(SpendCategory category, long appropriateAmount, long spendAmount, long risk) {

        public static TodayResponse from(TodaySpendDto todaySpend) {
            final SpendCategory category = SpendCategory.from(todaySpend.category());
            return new TodayResponse(category, todaySpend.appropriateAmount(), todaySpend.spendAmount(), todaySpend.risk());
        }
    }
}

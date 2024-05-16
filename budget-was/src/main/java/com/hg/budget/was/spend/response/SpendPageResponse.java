package com.hg.budget.was.spend.response;

import com.hg.budget.application.spend.dto.AmountDto;
import com.hg.budget.application.spend.dto.SpendPage;
import com.hg.budget.core.dto.Page;
import java.util.List;

public record SpendPageResponse(
    long totalAmount,
    List<AmountResponse> totalAmountByCategories,
    Page<MySpendResponse> page
) {

    public static SpendPageResponse from(SpendPage spendPage) {
        final long totalAmount = spendPage.getTotalAmountByCategories().stream()
            .mapToLong(AmountDto::amount)
            .sum();
        final List<AmountResponse> totalAmountResponses = spendPage.getTotalAmountByCategories().stream()
            .map(AmountResponse::from)
            .toList();
        final Page<MySpendResponse> pageSpend = spendPage.getPage().map(MySpendResponse::from);
        return new SpendPageResponse(totalAmount, totalAmountResponses, pageSpend);
    }
}

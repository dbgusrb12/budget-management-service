package com.hg.budget.was.spend.response;

import com.hg.budget.application.spend.dto.SpendPage;
import com.hg.budget.core.dto.Page;
import java.util.List;

public record SpendPageResponse(
    long totalAmount,
    List<TotalAmountResponse> totalAmountByCategories,
    Page<MySpendResponse> page
) {

    public static SpendPageResponse from(SpendPage spendPage) {
        final List<TotalAmountResponse> totalAmountResponses = spendPage.getTotalAmountByCategories().stream()
            .map(TotalAmountResponse::from)
            .toList();
        final Page<MySpendResponse> pageSpend = spendPage.getPage().map(MySpendResponse::from);
        return new SpendPageResponse(spendPage.getTotalAmount(), totalAmountResponses, pageSpend);
    }
}

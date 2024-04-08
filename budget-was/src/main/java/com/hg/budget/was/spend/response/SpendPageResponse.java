package com.hg.budget.was.spend.response;

import com.hg.budget.application.spend.dto.SpendPage;
import com.hg.budget.core.dto.Page;

public record SpendPageResponse(
    TotalAmountResponse totalAmounts,
    Page<MySpendResponse> page
) {

    public static SpendPageResponse from(SpendPage spendPage) {
        return new SpendPageResponse(
            TotalAmountResponse.from(spendPage.totalAmounts()),
            spendPage.page().map(MySpendResponse::from)
        );
    }
}

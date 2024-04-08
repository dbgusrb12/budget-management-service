package com.hg.budget.was.spend.response;

import com.hg.budget.application.spend.dto.SpendDto;

public record MySpendResponse(
    Long id,
    SpendCategory category,
    long amount,
    String memo,
    String spentDateTime,
    boolean excludeTotal
) {

    public static MySpendResponse from(SpendDto spend) {
        final SpendCategory category = SpendCategory.from(spend.getCategory());
        return new MySpendResponse(
            spend.getId(),
            category,
            spend.getAmount(),
            spend.getMemo(),
            spend.getSpentDateTime(),
            spend.isExcludeTotal()
        );
    }
}

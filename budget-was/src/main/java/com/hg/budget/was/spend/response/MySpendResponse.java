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
        return new MySpendResponse(
            spend.id(),
            SpendCategory.from(spend.category()),
            spend.amount(),
            spend.memo(),
            spend.spentDateTime(),
            spend.excludeTotal()
        );
    }
}

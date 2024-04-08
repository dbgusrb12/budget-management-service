package com.hg.budget.was.spend.response;

public record MySpendResponse(
    Long id,
    SpendCategory category,
    long amount,
    String memo,
    String spentDateTime,
    boolean excludeTotal
) {

}

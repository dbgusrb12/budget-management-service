package com.hg.budget.domain.spend.port.specification;

import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.category.Category;
import java.time.LocalDateTime;

public record SpendSpecification(
    int page,
    int size,
    LocalDateTime startSpentDateTime,
    LocalDateTime endSpentDateTime,
    Account account,
    Category category,
    Long minAmount,
    Long maxAmount
) {

}

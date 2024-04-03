package com.hg.budget.application.budget.infrastructure.average;

import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.budget.Budget;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TotalAmount {

    private final Map<Account, Long> totalAmountByAccount;

    public TotalAmount(List<Budget> budgets) {
        List<Budget> cloneBudgets = budgets == null ? new ArrayList<>() : budgets;
        this.totalAmountByAccount = cloneBudgets.stream()
            .collect(Collectors.groupingBy(
                Budget::getCreatedUser,
                Collectors.mapping(Budget::getAmount, Collectors.summingLong(Long::longValue))
            ));
    }

    public long findPercent(Budget budget) {
        return findPercent(budget.getCreatedUser(), budget.getAmount());
    }

    private long findPercent(Account account, long amount) {
        if (!totalAmountByAccount.containsKey(account)) {
            return 0;
        }
        final long totalAmount = totalAmountByAccount.get(account);
        return Math.round(amount / (double) totalAmount * 100);
    }
}

package com.hg.budget.application.budget;

import com.hg.budget.application.budget.client.BudgetRecommendStrategy;
import com.hg.budget.application.budget.dto.BudgetDto;
import com.hg.budget.application.budget.dto.RecommendBudgetDto;
import com.hg.budget.application.core.code.ApplicationCode;
import com.hg.budget.application.core.exception.ApplicationException;
import com.hg.budget.core.client.DateTimeHolder;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.account.AccountService;
import com.hg.budget.domain.budget.Budget;
import com.hg.budget.domain.budget.BudgetService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BudgetQueryService {

    private final DateTimeHolder dateTimeHolder;
    private final BudgetService budgetService;
    private final AccountService accountService;
    private final BudgetRecommendStrategy budgetRecommendStrategy;

    public List<BudgetDto> getBudgets(String accountId) {
        final Account account = accountService.findAccount(accountId);
        if (account.notExist()) {
            throw new ApplicationException(ApplicationCode.BAD_REQUEST, "유저가 존재하지 않습니다.");
        }
        return budgetService.findBudgets(account).stream()
            .map(budget -> BudgetDto.from(budget, dateTimeHolder))
            .toList();
    }

    public List<RecommendBudgetDto> recommend(long totalAmount) {
        final List<Budget> budgets = budgetService.findBudgets();
        return budgetRecommendStrategy.recommend(totalAmount, budgets).stream()
            .map(RecommendBudgetDto::from)
            .toList();
    }
}

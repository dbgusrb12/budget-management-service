package com.hg.budget.application.budget;

import com.hg.budget.application.budget.dto.CreateBudget;
import com.hg.budget.application.core.code.ApplicationCode;
import com.hg.budget.application.core.exception.ApplicationException;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.account.AccountService;
import com.hg.budget.domain.budget.Budget;
import com.hg.budget.domain.budget.BudgetService;
import com.hg.budget.domain.category.Category;
import com.hg.budget.domain.category.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BudgetCommandService {

    private final BudgetService budgetService;
    private final CategoryService categoryService;
    private final AccountService accountService;

    @Transactional
    public void createBudgets(List<CreateBudget> createBudgets, String accountId) {
        final Account account = accountService.findAccount(accountId);
        if (account.notExist()) {
            throw new ApplicationException(ApplicationCode.BAD_REQUEST, "유저가 존재하지 않습니다.");
        }

        final CategoryFilter categoryFilter = new CategoryFilter(categoryService.findCategories());

        for (CreateBudget createBudget : createBudgets) {
            final Category category = categoryFilter.get(createBudget.categoryId());
            createBudget(createBudget.amount(), category, account);
        }
    }

    @Transactional
    public void updateAmount(Long id, Long amount, String accountId) {
        final Budget budget = budgetService.updateAmount(id, amount);
        if (budget.notExist()) {
            throw new ApplicationException(ApplicationCode.BAD_REQUEST, "예산이 존재하지 않습니다.");
        }
        if (!accountId.equals(budget.getCreatedUser().getId())) {
            throw new ApplicationException(ApplicationCode.BAD_REQUEST, "예산 설정자가 아닙니다.");
        }
    }

    private void createBudget(Long amount, Category category, Account account) {
        try {
            budgetService.createBudget(amount, category, account);
        } catch (IllegalArgumentException e) {
            throw new ApplicationException(ApplicationCode.BAD_REQUEST, e.getMessage(), e);
        }
    }
}

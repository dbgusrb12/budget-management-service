package com.hg.budget.application.budget;

import com.hg.budget.application.budget.client.BudgetRecommendStrategy;
import com.hg.budget.application.budget.dto.BudgetDto;
import com.hg.budget.application.budget.dto.CreatedUser;
import com.hg.budget.application.budget.dto.RecommendBudget;
import com.hg.budget.application.category.dto.CategoryDto;
import com.hg.budget.application.core.code.ApplicationCode;
import com.hg.budget.application.core.exception.ApplicationException;
import com.hg.budget.core.client.DateTimeHolder;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.account.AccountService;
import com.hg.budget.domain.budget.BudgetService;
import com.hg.budget.domain.category.Category;
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
        final CreatedUser createdUser = new CreatedUser(account.getId(), account.getNickname());
        return budgetService.findBudgets(account).stream()
            .map(budget -> {
                final Category category = budget.getCategory();
                return new BudgetDto(
                    budget.getId(),
                    new CategoryDto(category.getId(), category.getName()),
                    budget.getAmount(),
                    createdUser,
                    dateTimeHolder.toString(budget.getCreatedDateTime()),
                    dateTimeHolder.toString(budget.getUpdatedDateTime())
                );
            })
            .toList();
    }

    public List<RecommendBudget> recommend(long totalAmount) {
        return budgetRecommendStrategy.recommend(totalAmount, budgetService.findBudgets());
    }
}

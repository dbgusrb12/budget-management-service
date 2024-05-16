package com.hg.budget.application.spend;

import com.hg.budget.application.core.code.ApplicationCode;
import com.hg.budget.application.core.exception.ApplicationException;
import com.hg.budget.application.spend.dto.RecommendDto;
import com.hg.budget.application.spend.dto.SpendGuideDto;
import com.hg.budget.application.spend.support.AverageSpendGuide;
import com.hg.budget.application.spend.support.AverageSpendRecommend;
import com.hg.budget.application.spend.support.CategoryAndMonthFilter;
import com.hg.budget.core.client.DateTimeHolder;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.account.AccountService;
import com.hg.budget.domain.budget.Budget;
import com.hg.budget.domain.budget.BudgetService;
import com.hg.budget.domain.spend.Spend;
import com.hg.budget.domain.spend.SpendService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpendConsultingService {

    private final SpendService spendService;
    private final AccountService accountService;
    private final BudgetService budgetService;
    private final DateTimeHolder dateTimeHolder;

    public List<RecommendDto> recommendSpend(String accountId) {
        final CategoryAndMonthFilter filter = generateFilter(accountId);
        final List<AverageSpendRecommend> averageSpendRecommends = filter.recommend();
        return averageSpendRecommends.stream()
            .map(AverageSpendRecommend::recommendAmount)
            .toList();
    }

    public List<SpendGuideDto> getSpendGuide(String accountId) {
        final CategoryAndMonthFilter filter = generateFilter(accountId);
        final List<AverageSpendGuide> averageSpendGuides = filter.guide();
        return averageSpendGuides.stream()
            .map(AverageSpendGuide::getSpendGuide)
            .toList();
    }

    private CategoryAndMonthFilter generateFilter(String accountId) {
        final Account account = getAccount(accountId);
        final List<Budget> budgets = budgetService.findBudgets(account);
        final List<Spend> spendList = spendService.findSpendList(account);
        return new CategoryAndMonthFilter(dateTimeHolder.now().toLocalDate(), budgets, spendList);
    }

    private Account getAccount(String accountId) {
        final Account account = accountService.findAccount(accountId);
        if (account.notExist()) {
            throw new ApplicationException(ApplicationCode.BAD_REQUEST, "유저가 존재하지 않습니다.");
        }
        return account;
    }
}

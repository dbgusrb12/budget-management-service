package com.hg.budget.application.spend;

import com.hg.budget.application.category.dto.CategoryDto;
import com.hg.budget.application.core.code.ApplicationCode;
import com.hg.budget.application.core.exception.ApplicationException;
import com.hg.budget.application.spend.dto.SpendSummaryDto;
import com.hg.budget.application.spend.dto.SpendSummaryDto.CategoryComparisonDto;
import com.hg.budget.application.spend.dto.SpendSummaryDto.DayOfWeekSpentComparisonDto;
import com.hg.budget.application.spend.dto.SpendSummaryDto.MonthSpentComparisonDto;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.account.AccountService;
import com.hg.budget.domain.budget.BudgetService;
import com.hg.budget.domain.category.Category;
import com.hg.budget.domain.category.CategoryService;
import com.hg.budget.domain.spend.SpendService;
import java.time.DayOfWeek;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpendStatisticService {

    private final SpendService spendService;
    private final AccountService accountService;
    private final CategoryService categoryService;
    private final BudgetService budgetService;

    public SpendSummaryDto getSpendSummary(String accountId) {
        final Account account = getAccount(accountId);
        return getMockSpendSummary();
    }

    public SpendSummaryDto getMockSpendSummary() {
        final CategoryComparisonDto categoryComparisonDto = new CategoryComparisonDto(CategoryDto.from(Category.of(1L, "식비")), 50);
        final MonthSpentComparisonDto monthSpentComparisonDto = new MonthSpentComparisonDto(50, List.of(categoryComparisonDto));
        final DayOfWeekSpentComparisonDto dayOfWeekSpentComparisonDto = new DayOfWeekSpentComparisonDto(DayOfWeek.MONDAY, 50);
        return new SpendSummaryDto(monthSpentComparisonDto, dayOfWeekSpentComparisonDto, 50);
    }

    private Account getAccount(String accountId) {
        final Account account = accountService.findAccount(accountId);
        if (account.notExist()) {
            throw new ApplicationException(ApplicationCode.BAD_REQUEST, "유저가 존재하지 않습니다.");
        }
        return account;
    }
}

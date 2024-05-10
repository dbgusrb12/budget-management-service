package com.hg.budget.application.spend;

import com.hg.budget.application.category.dto.CategoryDto;
import com.hg.budget.application.core.code.ApplicationCode;
import com.hg.budget.application.core.exception.ApplicationException;
import com.hg.budget.application.spend.client.SpendConsultingStrategy;
import com.hg.budget.application.spend.dto.RecommendDto;
import com.hg.budget.application.spend.dto.SpendDto;
import com.hg.budget.application.spend.dto.SpendGuideDto;
import com.hg.budget.application.spend.dto.SpendPage;
import com.hg.budget.application.spend.dto.SpendSummaryDto;
import com.hg.budget.application.spend.dto.SpendSummaryDto.CategoryComparisonDto;
import com.hg.budget.application.spend.dto.SpendSummaryDto.DayOfWeekSpentComparisonDto;
import com.hg.budget.application.spend.dto.SpendSummaryDto.MonthSpentComparisonDto;
import com.hg.budget.core.client.DateTimeHolder;
import com.hg.budget.core.dto.Page;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.account.AccountService;
import com.hg.budget.domain.budget.Budget;
import com.hg.budget.domain.budget.BudgetService;
import com.hg.budget.domain.category.Category;
import com.hg.budget.domain.category.CategoryService;
import com.hg.budget.domain.spend.Spend;
import com.hg.budget.domain.spend.SpendService;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpendQueryService {

    private final SpendService spendService;
    private final AccountService accountService;
    private final CategoryService categoryService;
    private final BudgetService budgetService;
    private final SpendValidator spendValidator;
    private final DateTimeHolder dateTimeHolder;
    private final SpendConsultingStrategy spendConsultingStrategy;

    public SpendDto getSpend(Long id, String accountId) {
        final Account account = getAccount(accountId);
        final Spend spend = spendService.findSpend(id);
        spendValidator.validateExist(spend);
        spendValidator.validateOwner(spend, account);
        return SpendDto.of(spend, dateTimeHolder);
    }

    public SpendPage pageSpend(
        int page,
        int size,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        Long categoryId,
        Long minAmount,
        Long maxAmount,
        String accountId
    ) {
        final Account account = getAccount(accountId);
        final Category category = getCategory(categoryId);
        final Page<Spend> spendPage = spendService.pageSpendList(page, size, startDateTime, endDateTime, account, category, minAmount, maxAmount);
        final SpendAmountCalculator spendAmountCalculator = new SpendAmountCalculator(spendPage.getContent());
        return SpendPage.of(spendAmountCalculator.getTotalAmountByCategory(), spendPage.map(spend -> SpendDto.of(spend, dateTimeHolder)));
    }

    public List<RecommendDto> recommendSpend(String accountId) {
        final Account account = getAccount(accountId);
        final List<Budget> budgets = budgetService.findBudgets(account);
        final List<Spend> spends = spendService.findSpendList(account);
        return spendConsultingStrategy.recommend(budgets, spends).stream()
            .map(recommendSpend -> RecommendDto.of(recommendSpend.category(), recommendSpend.amount(), recommendSpend.comment().getComment()))
            .toList();
    }

    public List<SpendGuideDto> getSpendGuide(String accountId) {
        final Account account = getAccount(accountId);
        final List<Budget> budgets = budgetService.findBudgets(account);
        final List<Spend> spends = spendService.findSpendList(account);
        return spendConsultingStrategy.guide(budgets, spends).stream()
            .map(spendGuide -> SpendGuideDto.of(spendGuide.category(), spendGuide.appropriateAmount(), spendGuide.spentAmount(), spendGuide.risk()))
            .toList();
    }

    public SpendSummaryDto getSpendSummary(String accountId) {
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

    private Category getCategory(Long categoryId) {
        if (categoryId == null) {
            return Category.ofNotExist();
        }
        final Category category = categoryService.findCategory(categoryId);
        if (category.notExist()) {
            throw new ApplicationException(ApplicationCode.BAD_REQUEST, "카테고리가 존재하지 않습니다.");
        }
        return category;
    }
}

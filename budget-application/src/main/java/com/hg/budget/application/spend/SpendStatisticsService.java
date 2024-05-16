package com.hg.budget.application.spend;

import com.hg.budget.application.category.dto.CategoryDto;
import com.hg.budget.application.core.code.ApplicationCode;
import com.hg.budget.application.core.exception.ApplicationException;
import com.hg.budget.application.spend.dto.SpendSummaryDto;
import com.hg.budget.application.spend.dto.SpendSummaryDto.CategoryComparisonDto;
import com.hg.budget.application.spend.dto.SpendSummaryDto.DayOfWeekSpentComparisonDto;
import com.hg.budget.application.spend.dto.SpendSummaryDto.MonthSpentComparisonDto;
import com.hg.budget.core.client.DateTimeHolder;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.account.AccountService;
import com.hg.budget.domain.budget.Budget;
import com.hg.budget.domain.budget.BudgetService;
import com.hg.budget.domain.category.Category;
import com.hg.budget.domain.spend.Spend;
import com.hg.budget.domain.spend.SpendService;
import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpendStatisticsService {

    private final SpendService spendService;
    private final AccountService accountService;
    private final BudgetService budgetService;
    private final DateTimeHolder dateTimeHolder;

    public SpendSummaryDto getSpendSummary(String accountId) {
        final Account account = getAccount(accountId);
        final List<Spend> spendList = spendService.findSpendList(account);
        final LocalDate today = dateTimeHolder.now().toLocalDate();
        final MonthSpentComparisonDto monthSpentComparison = calculateMonthSpentComparison(today, spendList);
        final DayOfWeekSpentComparisonDto dayOfWeekSpentComparison = calculateDayOfWeekSpentComparison(today, spendList);
        final long consumptionRateByOtherUsers = calculateOtherUsersComparison(account);
        return new SpendSummaryDto(
            monthSpentComparison,
            dayOfWeekSpentComparison,
            consumptionRateByOtherUsers
        );
    }

    private long calculateOtherUsersComparison(Account targetAccount) {
        final List<Account> accounts = accountService.findAccounts();
        final Map<Account, Long> consumptionRateByAccount = accounts.stream()
            .map(account -> {
                final long budgetTotalAmount = budgetService.findBudgets(account).stream()
                    .mapToLong(Budget::getAmount)
                    .sum();
                final long spendTotalAmount = spendService.findSpendList(account).stream()
                    .mapToLong(Spend::getAmount)
                    .sum();

                final long consumptionRate = calculateConsumptionRate(budgetTotalAmount, spendTotalAmount);
                return new Pair(account, consumptionRate);
            }).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
        final long targetConsumptionRate = consumptionRateByAccount.getOrDefault(targetAccount, 0L);
        final long otherUserConsumptionRateAverage = (long) consumptionRateByAccount.values().stream()
            .mapToLong(l -> l)
            .average()
            .orElse(0);
        return calculateConsumptionRate(otherUserConsumptionRateAverage, targetConsumptionRate);
    }

    @Getter
    @AllArgsConstructor
    static class Pair {

        private Account first;
        private long second;
    }

    private Account getAccount(String accountId) {
        final Account account = accountService.findAccount(accountId);
        if (account.notExist()) {
            throw new ApplicationException(ApplicationCode.BAD_REQUEST, "유저가 존재하지 않습니다.");
        }
        return account;
    }

    // FIXME 책임 분리 필요
    private MonthSpentComparisonDto calculateMonthSpentComparison(LocalDate today, List<Spend> spendList) {
        final List<Spend> spendListByPreviousMonth = spendList.stream()
            .filter(spend -> isPreviousMonth(today, spend))
            .toList();
        final List<Spend> spendListByCurrentMonth = spendList.stream()
            .filter(spend -> isSameMonth(today, spend))
            .toList();
        return new MonthSpentComparisonDto(
            calculateComparison(spendListByPreviousMonth, spendListByCurrentMonth),
            calculateComparisonByCategory(spendListByPreviousMonth, spendListByCurrentMonth)
        );
    }

    private boolean isPreviousMonth(LocalDate today, Spend spend) {
        final int year = today.getYear();
        if (year != spend.getSpentDateTime().getYear()) {
            return false;
        }
        final Month previousMonth = today.getMonth().minus(1);
        if (previousMonth != spend.getSpentDateTime().getMonth()) {
            return false;
        }
        final int day = today.getDayOfMonth();
        return spend.getSpentDateTime().getDayOfMonth() <= day;
    }

    private boolean isSameMonth(LocalDate today, Spend spend) {
        final int year = today.getYear();
        if (year != spend.getSpentDateTime().getYear()) {
            return false;
        }
        final Month month = today.getMonth();
        return month.equals(spend.getSpentDateTime().getMonth());
    }

    private List<CategoryComparisonDto> calculateComparisonByCategory(List<Spend> spendListByPreviousMonth, List<Spend> spendListByCurrentMonth) {
        final Map<Category, Long> previousTotalAmountByCategory = spendListByPreviousMonth.stream()
            .collect(Collectors.groupingBy(
                Spend::getCategory,
                Collectors.mapping(Spend::getAmount, Collectors.summingLong(Long::longValue))
            ));

        final Map<Category, Long> currentTotalAmountByCategory = spendListByCurrentMonth.stream()
            .collect(Collectors.groupingBy(
                Spend::getCategory,
                Collectors.mapping(Spend::getAmount, Collectors.summingLong(Long::longValue))
            ));
        return currentTotalAmountByCategory.entrySet().stream()
            .map(currentTotalAmount -> {
                final Long previousTotalAmount = previousTotalAmountByCategory.getOrDefault(currentTotalAmount.getKey(), 0L);
                long consumptionRate = calculateConsumptionRate(previousTotalAmount, currentTotalAmount.getValue());
                return new CategoryComparisonDto(CategoryDto.from(currentTotalAmount.getKey()), consumptionRate);
            })
            .sorted(Comparator.comparing(categoryComparison -> categoryComparison.category().getId()))
            .toList();
    }

    // FIXME 책임 분리 필요
    private DayOfWeekSpentComparisonDto calculateDayOfWeekSpentComparison(LocalDate today, List<Spend> spendList) {
        final List<Spend> spendListByLastWeek = spendList.stream()
            .filter(spend -> isLastWeek(today, spend))
            .toList();
        final List<Spend> spendListByToday = spendList.stream()
            .filter(spend -> isToday(today, spend))
            .toList();
        return new DayOfWeekSpentComparisonDto(
            today.getDayOfWeek(),
            calculateComparison(spendListByLastWeek, spendListByToday)
        );
    }

    private boolean isLastWeek(LocalDate today, Spend spend) {
        final LocalDate lastWeek = today.minusWeeks(1);
        return lastWeek.equals(spend.getSpentDateTime().toLocalDate());
    }

    private boolean isToday(LocalDate today, Spend spend) {
        return today.equals(spend.getSpentDateTime().toLocalDate());
    }

    private long calculateComparison(List<Spend> spendListByPrevious, List<Spend> spendListByCurrent) {
        final long previousTotalAmount = spendListByPrevious.stream()
            .mapToLong(Spend::getAmount)
            .sum();
        final long currentTotalAmount = spendListByCurrent.stream()
            .mapToLong(Spend::getAmount)
            .sum();
        return calculateConsumptionRate(previousTotalAmount, currentTotalAmount);
    }

    private long calculateConsumptionRate(long previousAmount, long currentAmount) {
        if (previousAmount == 0) {
            return currentAmount;
        }
        return currentAmount * 100 / previousAmount;
    }

    private long getMockConsumptionRateByOtherUsers() {
        return 50;
    }
}
